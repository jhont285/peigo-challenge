package com.peigo.domain.infrastructure.service;

import com.peigo.domain.infrastructure.model.TransactionInfo;
import com.peigo.domain.infrastructure.persistence.mysql.entity.Account;
import com.peigo.domain.infrastructure.persistence.mysql.entity.Payment;
import com.peigo.domain.infrastructure.persistence.mysql.entity.User;
import com.peigo.domain.infrastructure.persistence.mysql.repository.AccountRepository;
import com.peigo.domain.infrastructure.persistence.mysql.repository.PaymentRepository;
import com.peigo.domain.infrastructure.persistence.mysql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.compare.ComparableUtils.is;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Optional<Payment> transaction(String username, TransactionInfo transactionInfo) {
        var user = userRepository.findByUsername(username);
        Optional<Account> optionalAccountOrigin = accountRepository.findById(transactionInfo.idOriginAccount())
                .filter(account -> account.getUser().getId() == user.getId())
                .filter(account -> is(account.getBalance()).greaterThanOrEqualTo(transactionInfo.amount()));

        Optional<Account> optionalAccountDestiny = accountRepository.findById(transactionInfo.idDestinyAccount());

        return optionalAccountOrigin.flatMap(accountOrigin ->
                optionalAccountDestiny.map(accountDestiny -> executePaymentWithTransactions(transactionInfo,
                        user, accountOrigin, accountDestiny))
        );
    }

    private Payment executePaymentWithTransactions(TransactionInfo transactionInfo,
                                                     User user,
                                                     Account accountOrigin,
                                                     Account accountDestiny) {
        var payment = Payment.builder()
                .origin(accountOrigin)
                .destiny(accountDestiny)
                .user(user)
                .amount(transactionInfo.amount())
                .build();
        var paymentSaved = paymentRepository.save(payment);

        accountOrigin.setBalance(accountOrigin.getBalance().subtract(transactionInfo.amount()));
        accountDestiny.setBalance(accountDestiny.getBalance().add(transactionInfo.amount()));
        accountRepository.saveAll(List.of(accountOrigin, accountDestiny));
        return paymentSaved;
    }
}
