package org.vorobel.moneytransfer.model;

import com.fasterxml.jackson.annotation.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Transfer extends PanacheEntity {
    @NonNull public long sourceId;
    @NonNull public long destinationId;
    @NonNull public String amount;
    public boolean success = false;

    @JsonIgnore
    public Boolean isValid() {
        return sourceId >=0 && destinationId >= 0 && sourceId != destinationId && new BigDecimal(amount).compareTo(BigDecimal.ZERO) >= 0;
    }
}
