package org.vorobel.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaUpdate;
import java.math.BigDecimal;
import java.util.List;

@Entity
@NamedQueries({@NamedQuery(name = "Account.listAll", query = "SELECT a FROM Account a"),
        @NamedQuery(name = "Account.deleteAll", query = "DELETE FROM Account a")})
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Account extends BaseEntity {
    @NonNull
    @JsonProperty(required = true)
    public String balance;

    public static List<Account> listAll() {
        EntityTransaction tx = emCache.get().getTransaction();
        tx.begin();
        var list = emCache.get().createNamedQuery("Account.listAll").getResultList();
        tx.commit();
        emCache.get().clear();
        return list;

    }

    public static Account findById(Long id) {
        EntityTransaction tx = emCache.get().getTransaction();
        tx.begin();
        var account = emCache.get().find(Account.class, id);
        tx.commit();
        emCache.get().clear();
        return account;
    }

    public static Account findById(Long id, LockModeType lockModeType) {
        EntityTransaction tx = emCache.get().getTransaction();
        tx.begin();
        var account = emCache.get().find(Account.class, id, lockModeType);
        tx.commit();
        emCache.get().clear();
        return account;
    }

    public static void update(Account account) {
        EntityTransaction tx = emCache.get().getTransaction();
        tx.begin();
        emCache.get().merge(account);
        tx.commit();
        emCache.get().clear();
    }

    public static void deleteAll() {
        EntityTransaction tx = emCache.get().getTransaction();
        tx.begin();
        emCache.get().createNamedQuery("Account.deleteAll").executeUpdate();
        tx.commit();
        emCache.get().clear();
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
