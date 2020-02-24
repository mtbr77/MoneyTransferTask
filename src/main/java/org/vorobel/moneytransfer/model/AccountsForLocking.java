package org.vorobel.moneytransfer.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class AccountsForLocking extends PanacheEntity {
    @NonNull public long sourceId;
    @NonNull public long destinationId;
}
