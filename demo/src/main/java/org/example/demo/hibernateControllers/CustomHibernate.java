package org.example.demo.hibernateControllers;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.JoinType;
import javafx.scene.control.Alert;
import org.example.demo.model.*;
import org.example.demo.model.enums.PublicationStatus;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.demo.utils.FxUtils;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomHibernate extends GenericHibernate {
    public CustomHibernate(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public User getUserByCredentials(String username, String inputPassword) {
        User user = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root).where(cb.equal(root.get("login"), username));
            Query q = entityManager.createQuery(query);

            user = (User) q.getSingleResult();

            if (user != null) {
                String storedSalt = user.getSalt();
                String storedHash = user.getPassword();

                String computedHash = PasswordHasher.hashPassword(inputPassword, storedSalt);

                if (!storedHash.equals(computedHash)) {
                    user = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return user;
    }


    public List<Publication> getAvailablePublications(User user) {

        List<Publication> publications = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Publication> query = cb.createQuery(Publication.class);
            Root<Publication> root = query.from(Publication.class);

            query.select(root).where(cb.and(cb.equal(root.get("publicationStatus"), PublicationStatus.AVAILABLE), cb.notEqual(root.get("owner"), user)));
            Query q = entityManager.createQuery(query);
            publications = q.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return publications;
    }

    public void deleteComment(int id) {
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            var comment = entityManager.find(Comment.class, id);

            if (comment != null) {
                if (comment.getParentComment() != null) {
                    Comment parentComment = comment.getParentComment();
                    parentComment.getReplies().remove(comment);
                    entityManager.merge(parentComment);
                }

                comment.getReplies().clear();
                entityManager.remove(comment);
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }


    public List<Publication> getOwnPublications(User user) {

        List<Publication> publications = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Publication> query = cb.createQuery(Publication.class);
            Root<Publication> root = query.from(Publication.class);

            query.select(root).where(cb.equal(root.get("owner"), user));
            query.orderBy(cb.desc(root.get("requestDate")));

            Query q = entityManager.createQuery(query);
            publications = q.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return publications;
    }

    public List<PeriodicRecord> getPeriodicById(int id) {
        List<PeriodicRecord> periodicRecords = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<PeriodicRecord> query = cb.createQuery(PeriodicRecord.class);
            Root<PeriodicRecord> root = query.from(PeriodicRecord.class);
            Publication publication = entityManager.find(Publication.class, id);

            query.select(root).where(cb.equal(root.get("publication"), publication));
            query.orderBy(cb.desc(root.get("transactionDate")));

            Query q = entityManager.createQuery(query);
            periodicRecords = q.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return periodicRecords;
    }

    public Chat getChatByBook(Book book) {
        Chat chat = null;

        if (book == null) {
            System.err.println("Book parameter is null!");
            return null;
        }

        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Chat> query = cb.createQuery(Chat.class);
            Root<Chat> root = query.from(Chat.class);

            root.fetch("messages", JoinType.LEFT);
            query.select(root).where(cb.equal(root.get("book"), book));

            Query q = entityManager.createQuery(query);
            chat = (Chat) q.getSingleResult();

        } catch (NoResultException e) {
            System.out.println("No Chat found for the given Book.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return chat;
    }

    public <T> List<T> getAllRecordsSorted(Class<T> entityClass, String sortBy, boolean ascending) {
        List<T> list = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(entityClass);
            Root<T> root = query.from(entityClass);
            if (sortBy != null && !sortBy.isEmpty()) {
                if (ascending) {
                    query.orderBy(cb.asc(root.get(sortBy)));
                } else {
                    query.orderBy(cb.desc(root.get(sortBy)));
                }
            }
            Query q = entityManager.createQuery(query);
            list = q.getResultList();

        } catch (Exception e) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Hibernate Error", "Error during SELECT operation");
        } finally {
            if (entityManager != null) entityManager.close();
        }
        return list;
    }

    public PeriodicRecord getPeriodicRecordByClientAndPublication(Client client, Publication publication) {
        PeriodicRecord periodicRecord = null;

        if (client == null || publication == null) {
            System.err.println("Client or Publication parameter is null!");
            return null;
        }

        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<PeriodicRecord> query = cb.createQuery(PeriodicRecord.class);
            Root<PeriodicRecord> root = query.from(PeriodicRecord.class);

            query.select(root).where(
                    cb.and(
                            cb.equal(root.get("user"), client),
                            cb.equal(root.get("publication"), publication)
                    )
            );

            Query q = entityManager.createQuery(query);
            periodicRecord = (PeriodicRecord) q.getSingleResult();

        } catch (NoResultException e) {
            System.out.println("No PeriodicRecord found for the given Client and Publication.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return periodicRecord;
    }

    public List<Comment> getUnreadMessages(Client user) {
        List<Comment> unreadMessages = new ArrayList<>();

        if (user == null) {
            System.err.println("User parameter is null!");
            return unreadMessages;
        }

        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Comment> query = cb.createQuery(Comment.class);
            Root<Comment> root = query.from(Comment.class);

            query.select(root).where(
                    cb.and(
                            cb.equal(root.get("client"), user),
                            cb.equal(root.get("isRead"), false)
                    )
            );

            Query q = entityManager.createQuery(query);
            unreadMessages = q.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return unreadMessages;
    }

    public void deletePeriodicRecord(int id) {
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            PeriodicRecord record = entityManager.find(PeriodicRecord.class, id);

            if (record != null) {
                entityManager.remove(record);
            } else {
                System.out.println("No PeriodicRecord found with ID: " + id);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public long getTotalUsers() {
        long totalUsers = 0;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<Client> root = query.from(Client.class);

            query.select(cb.count(root));
            Query q = entityManager.createQuery(query);
            totalUsers = (Long) q.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return totalUsers;
    }

    public long getTotalBorrowedPublications() {
        long totalBorrowed = 0;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<PeriodicRecord> root = query.from(PeriodicRecord.class);

            query.select(cb.count(root))
                    .where(cb.equal(root.get("status"), PublicationStatus.TAKEN));

            Query q = entityManager.createQuery(query);
            totalBorrowed = (Long) q.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return totalBorrowed;
    }

    public long getTotalSoldPublications() {
        long totalSold = 0;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<PeriodicRecord> root = query.from(PeriodicRecord.class);

            query.select(cb.count(root))
                    .where(cb.equal(root.get("status"), PublicationStatus.SOLD));

            Query q = entityManager.createQuery(query);
            totalSold = (Long) q.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return totalSold;
    }

    public Map<String, Long> getMonthlyStatsByStatus(PublicationStatus status) {
        Map<String, Long> stats = new HashMap<>();

        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<PeriodicRecord> query = cb.createQuery(PeriodicRecord.class);
            Root<PeriodicRecord> root = query.from(PeriodicRecord.class);

            query.select(root).where(cb.equal(root.get("status"), status));
            Query q = entityManager.createQuery(query);
            List<PeriodicRecord> records = q.getResultList();

            for (PeriodicRecord record : records) {
                int month = record.getTransactionDate().getMonthValue();  // Get the month of the transaction date
                String monthStr = String.valueOf(month);  // Convert to string for easy handling

                stats.put(monthStr, stats.getOrDefault(monthStr, 0L) + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return stats;
    }

    public PeriodicRecord getLatestPeriodicRecordByPublication(Publication publication) {
        PeriodicRecord latestPeriodicRecord = null;

        if (publication == null) {
            System.err.println("Publication parameter is null!");
            return null;
        }

        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<PeriodicRecord> query = cb.createQuery(PeriodicRecord.class);
            Root<PeriodicRecord> root = query.from(PeriodicRecord.class);

            query.select(root).where(
                    cb.and(
                            cb.equal(root.get("publication"), publication),
                            cb.isNull(root.get("returnDate"))
                    )
            );

            query.orderBy(cb.desc(root.get("transactionDate")));

            Query q = entityManager.createQuery(query);
            q.setMaxResults(1);

            List<PeriodicRecord> result = q.getResultList();
            if (!result.isEmpty()) {
                latestPeriodicRecord = result.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return latestPeriodicRecord;
    }


}