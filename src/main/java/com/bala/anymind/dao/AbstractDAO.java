package com.bala.anymind.dao;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class AbstractDAO<T> {

    @PersistenceContext
    @Autowired
    protected EntityManager entityManager;

    private boolean useTabSuffix = false;

    protected abstract Class<T> getEntityClass();

    protected T findOne(long id) {
        return entityManager.find(getEntityClass(), id);
    }

    protected List<T> findAll() {
        return entityManager.createQuery("from " + getEntityClass().getName()).getResultList();
    }

    protected T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    protected T update(T entity) {
        return entityManager.merge(entity);
    }

    protected void delete(T entity) {
        entityManager.remove(entity);
    }

    protected void deleteById(long entityId) {
        T entity = findOne(entityId);
        delete(entity);
    }

    protected void saveAll(List<T> entityValues) {
        for (T entity : entityValues) {
            entityManager.persist(entity);
        }
    }

}