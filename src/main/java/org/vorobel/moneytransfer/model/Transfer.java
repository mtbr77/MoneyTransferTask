package org.vorobel.moneytransfer.model;

import com.fasterxml.jackson.annotation.*;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@NamedQueries({
@NamedQuery(name = "Transfer.listAll", query = "SELECT t FROM Transfer t"),
@NamedQuery(name = "Transfer.deleteAll", query = "DELETE FROM Transfer t")})
@NoArgsConstructor
@RequiredArgsConstructor
public class Transfer extends BaseEntity {
    @NonNull @JsonProperty(required = true) public long sourceId;
    @NonNull @JsonProperty(required = true) public long destinationId;
    @NonNull @JsonProperty(required = true) public String amount;
    public boolean success = false;

    public static List<Transfer> listAll() {
        EntityTransaction tx = emCache.get().getTransaction();
        tx.begin();
        var list = emCache.get().createNamedQuery("Transfer.listAll").getResultList();
        tx.commit();
        emCache.get().clear();
        return list;
    }

    public static Transfer findById(Long id) {
        EntityTransaction tx = emCache.get().getTransaction();
        tx.begin();
        var transfer = emCache.get().find(Transfer.class, id);
        tx.commit();
        emCache.get().clear();
        return transfer;
    }

    public static void deleteAll() {
        EntityTransaction tx = emCache.get().getTransaction();
        tx.begin();
        emCache.get().createNamedQuery("Transfer.deleteAll").executeUpdate();
        tx.commit();
        emCache.get().clear();
    }

    @JsonIgnore
    public Boolean isValid() {
        return sourceId >=0 && destinationId >= 0 && sourceId != destinationId && new BigDecimal(amount).compareTo(BigDecimal.ZERO) >= 0;
    }
}
