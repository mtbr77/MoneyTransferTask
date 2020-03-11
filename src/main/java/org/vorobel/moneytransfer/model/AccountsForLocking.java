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
    @NonNull public long sourceId;
    @NonNull public long destinationId;

    public static AccountsForLocking getFirstRegisteredAccountsForLockingWithTheSameIds(Transfer transfer) {
        return (AccountsForLocking) emCache.get()
                .createQuery("SELECT a FROM AccountsForLocking a WHERE a.sourceId = ?1 OR a.destinationId = ?1 OR a.sourceId = ?2 OR a.destinationId = ?2")
                .setParameter(1,transfer.sourceId)
                .setParameter(2,transfer.destinationId)
                .getSingleResult();
    }

    public static AccountsForLocking findById(Long id) {
        return emCache.get().find(AccountsForLocking.class, id);
    }
}
