package org.vorobel.moneytransfer.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
public class Account extends PanacheEntity {
    public String balance;
}
