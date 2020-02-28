package org.vorobel.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class AccountsForLocking extends BaseEntity {
    public long sourceId;
    public long destinationId;

    private static final String accountsAlreadyInvolved = "sourceId = ?1 OR destinationId = ?1 OR sourceId = ?2 OR destinationId = ?2";

    public static AccountsForLocking getFirstRegisteredAccountsForLockingWithTheSameIds(Transfer transfer) {
        return (AccountsForLocking) em.createQuery("SELECT a FROM AccountsForLocking a WHERE " + accountsAlreadyInvolved).setParameter("1",transfer.sourceId).setParameter("2",transfer.destinationId).getSingleResult();
    }

    public static AccountsForLocking findById(Long id) {
        return em.find(AccountsForLocking.class, id);
    }
}
