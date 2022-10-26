package com.peigo.domain.infrastructure.model;

import lombok.Builder;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public record TransactionInfo(long idOriginAccount,
                              long idDestinyAccount,
                              @Positive
                              BigDecimal amount) {

    @Builder
    public TransactionInfo {
    }
}
