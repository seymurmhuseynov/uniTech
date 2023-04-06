package com.uniTech.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_user", referencedColumnName = "id", nullable=false)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_currency", referencedColumnName = "id", nullable=false)
    private Currency currency;
    private String accountNumber;
    private double balance;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean active;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean selected;

}
