package org.vorobel.moneytransfer;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

public class EntityManagerProducer
{
    @PersistenceUnit
    private EntityManagerFactory emf;

    @Produces
    protected EntityManager createEntityManager()
    {
        return emf.createEntityManager();
    }

    protected void closeEntityManager(@Disposes EntityManager entityManager)
    {
        if (entityManager.isOpen())
        {
            entityManager.close();
        }
    }
}
