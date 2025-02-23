package org.example.demo.hibernateControllers;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import org.example.demo.model.*;
import org.example.demo.model.enums.PublicationStatus;
import org.example.demo.utils.FxUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class GenericHibernate {

    EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;

    public GenericHibernate(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public <T> void create(T entity) {
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Hibernate Error", "Error during INSERT operation");
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public <T> void update(T entity) {
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Hibernate Error", "Error during UPDATE operation");
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public <T> List<T> getRecordsWithCondition(Class<T> entityClass, String fieldName, Object value) {
        List<T> list = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(entityClass);
            Root<T> root = query.from(entityClass);

            query.select(root).where(cb.equal(root.get(fieldName), value));
            Query q = entityManager.createQuery(query);
            list = q.getResultList();

        } catch (Exception e) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Hibernate Error", "Error during SELECT operation");
        } finally {
            if (entityManager != null) entityManager.close();
        }
        return list;
    }


    public <T> List<T> getAllRecords(Class<T> entityClass) {
        List<T> list = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaQuery query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(entityClass));
            Query q = entityManager.createQuery(query);
            list = q.getResultList();

        } catch (Exception e) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Hibernate Error", "Error during SELECT operation");
        } finally {
            if (entityManager != null) entityManager.close();
        }
        return list;
    }

    public <T> T getEntityById(Class<T> entityClass, int id) {
        T result = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            result = entityManager.find(entityClass, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Hibernate Error", "Error during SELECT ... WHERE operation");
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
        return result;
    }

    public <T> void delete(Class<T> entityClass, int id) {
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            T object = entityManager.find(entityClass, id);
            entityManager.remove(object);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Hibernate Error", "Error during DELETE operation");
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }



}