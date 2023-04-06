package com.uniTech.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_account_from", referencedColumnName = "id", nullable=false)
    private Account accountFrom;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_account_to", referencedColumnName = "id", nullable=false)
    private Account accountTo;
    private double amount;
    private double currency;
    private double totalAmount;
}
