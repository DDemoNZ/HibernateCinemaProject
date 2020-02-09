package com.dev.cinema.dao.impl;

import com.dev.cinema.dao.ShoppingCartDao;
import com.dev.cinema.lib.Dao;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.Ticket;
import com.dev.cinema.model.User;
import com.dev.cinema.util.HibernateUtil;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Long shoppingCartId = (Long) session.save(shoppingCart);
            transaction.commit();
            shoppingCart.setId(shoppingCartId);
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't add shopping cart to DB", e);
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ShoppingCart> criteriaQuery =
                    criteriaBuilder.createQuery(ShoppingCart.class);
            Root<ShoppingCart> shoppingCartRoot = criteriaQuery.from(ShoppingCart.class);
            shoppingCartRoot.fetch("tickets", JoinType.LEFT);
            criteriaQuery.select(shoppingCartRoot)
                    .where(criteriaBuilder.equal(shoppingCartRoot.get("user"), user));
            return session.createQuery(criteriaQuery).uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Can't get shopping cart by user from DB", e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't update shopping cart", e);
        }
    }
}
