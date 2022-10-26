package com.peigo.domain.infrastructure.persistence.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long id;

    @OneToOne
    private Account origin;

    @OneToOne
    private Account destiny;

    @OneToOne
    private User user;

    @Column(nullable = false)
    private BigDecimal amount;
}
