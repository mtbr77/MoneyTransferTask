package org.vorobel.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@MappedSuperclass
public abstract class BaseEntity {
    protected static EntityManagerFactory emf = Persistence.createEntityManagerFactory("db-manager");
    protected static ThreadLocal<EntityManager> emCache = ThreadLocal.withInitial(() -> emf.createEntityManager());

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public void save() {
        EntityTransaction tx = emCache.get().getTransaction();
        tx.begin();
        emCache.get().persist(this);
        tx.commit();
    }

    public void delete() {
        EntityTransaction tx = emCache.get().getTransaction();
        tx.begin();
        emCache.get().remove(this);
        tx.commit();
    }
}
