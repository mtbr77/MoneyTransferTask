package org.vorobel.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

public class BaseEntity {
    protected static final EntityManager em = Persistence.createEntityManagerFactory("").createEntityManager();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @JsonIgnore
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    public static <T extends BaseEntity> List<T> listAll() {
        return em.createNamedQuery("listAll").getResultList();
    }

    public void save() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(this);
        tx.commit();
    }

    /*public static void flush() {
        em.flush();
    }*/

    public void delete() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.remove(this);
        tx.commit();
    }

    public static void deleteAll() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.createNamedQuery("deleteAll").executeUpdate();
        tx.commit();
    }
}
