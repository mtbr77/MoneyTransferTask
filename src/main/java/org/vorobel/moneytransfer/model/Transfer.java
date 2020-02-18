package org.vorobel.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transfers")
@RequiredArgsConstructor
@JsonIgnoreProperties(value={ "status", "time" }, allowGetters= true)
public class Transfer extends PanacheEntity {
    @NonNull public long source;
    @NonNull public long destination;
    @NonNull public String amount;
    public boolean success = false;
    public Timestamp time;
}
