package org.vorobel.moneytransfer.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
public class Account extends PanacheEntity {
    public String balance;

    public String deposit(String amount) {
        balance = new BigDecimal(balance).add(new BigDecimal(amount)).toString();
        return balance;
    }

    public String withdraw(String amount) {
        balance = new BigDecimal(balance).subtract(new BigDecimal(amount)).toString();
        return balance;
    }

    public boolean enoughForWithdraw(String amount) {
        return new BigDecimal(balance).compareTo(new BigDecimal(amount)) >= 0;
    }

    public boolean isValidBalance() {
        return new BigDecimal(balance).compareTo(BigDecimal.ZERO) >= 0;
    }
}
