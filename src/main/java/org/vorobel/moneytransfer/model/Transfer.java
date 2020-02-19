package org.vorobel.moneytransfer.model;

import com.fasterxml.jackson.annotation.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transfers")
@NoArgsConstructor
@RequiredArgsConstructor
//@JsonPropertyOrder({"source",})
public class Transfer extends PanacheEntity {
    @NonNull public long source;
    @NonNull public long destination;
    @NonNull public String amount;
    public boolean success = false;

    @JsonIgnore
    public boolean isSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @JsonIgnore
    public Boolean isValid() {
        return source >=0 && destination >= 0 && source != destination && new BigDecimal(amount).compareTo(BigDecimal.ZERO) >= 0;
    }
}
