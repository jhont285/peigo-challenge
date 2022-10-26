package com.peigo.domain.infrastructure.persistence.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    private long id;

    @NonNull
    @Column(nullable = false)
    private BigDecimal balance;

    @ManyToOne
    private User user;
}
