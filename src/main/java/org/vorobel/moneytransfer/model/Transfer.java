package org.vorobel.moneytransfer.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.*;

@Entity
@Table(name = "transfers")
public class Transfer extends PanacheEntity {
    public long sourceId, destinationId;
    public String amount;
}
