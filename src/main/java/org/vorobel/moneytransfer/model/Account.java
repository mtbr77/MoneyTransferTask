package org.vorobel.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaUpdate;
import java.math.BigDecimal;

@Entity
@NamedQueries({@NamedQuery(name = "listAll", query = "SELECT a FROM Account a"),
        @NamedQuery(name = "deleteAll", query = "DELETE FROM Account a")})
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseEntity {
    @JsonProperty(required = true)
    public String balance;

    public static Account findById(Long id) {
        return em.find(Account.class, id);
    }

    public static Account findById(Long id, LockModeType lockModeType) {
        return em.find(Account.class, id, lockModeType);
    }

    public static void update(Account account) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(account);
        tx.commit();
    }

    public static String getTotalBalance(Account[] accounts) {
        BigDecimal sum = BigDecimal.ZERO;
        for(Account acc : accounts){
            sum = sum.add(new BigDecimal(acc.balance));
        }
        return sum.toString();
    }

    public Account deposit(String amount) {
        balance = new BigDecimal(balance).add(new BigDecimal(amount)).toString();
        return this;
    }

    public Account withdraw(String amount) {
        balance = new BigDecimal(balance).subtract(new BigDecimal(amount)).toString();
        return this;
    }

    public boolean enoughForWithdraw(String amount) {
        return new BigDecimal(balance).compareTo(new BigDecimal(amount)) >= 0;
    }

    @JsonIgnore
    public boolean isValidBalance() {
        return new BigDecimal(balance).compareTo(BigDecimal.ZERO) >= 0;
    }
}
