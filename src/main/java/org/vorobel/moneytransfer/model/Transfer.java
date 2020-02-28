package org.vorobel.moneytransfer.model;

import com.fasterxml.jackson.annotation.*;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@NamedQuery(name = "listAll", query = "SELECT t FROM Transfer t")
@NoArgsConstructor
@RequiredArgsConstructor
public class Transfer extends BaseEntity {
    @NonNull @JsonProperty(required = true) public long sourceId;
    @NonNull @JsonProperty(required = true) public long destinationId;
    @NonNull @JsonProperty(required = true) public String amount;
    public boolean success = false;

    public static Transfer findById(Long id) {
        return em.find(Transfer.class, id);
    }

    @JsonIgnore
    public Boolean isValid() {
        return sourceId >=0 && destinationId >= 0 && sourceId != destinationId && new BigDecimal(amount).compareTo(BigDecimal.ZERO) >= 0;
    }
}
