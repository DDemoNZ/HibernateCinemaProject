package com.dev.cinema.dao.impl;

import com.dev.cinema.dao.OrderDao;
import com.dev.cinema.model.Orders;
import com.dev.cinema.model.User;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl implements OrderDao {

    private final SessionFactory sessionFactory;

    public OrderDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Orders add(Orders order) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Long orderId = (Long) session.save(order);
            transaction.commit();
            order.setId(orderId);
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't add order in DB", e);
        }
    }

    @Override
    public List<Orders> getUserOrderHistory(User user) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Orders> criteriaQuery = criteriaBuilder.createQuery(Orders.class);
            Root<Orders> ordersRoot = criteriaQuery.from(Orders.class);
            ordersRoot.fetch("tickets", JoinType.LEFT);
            criteriaQuery.select(ordersRoot)
                    .where(criteriaBuilder.equal(ordersRoot.get("user"), user));
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't get user's orders from DB", e);
        }
    }

    @Override
    public Orders getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Orders.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Can't get user by id " + id);
        }
    }
}
