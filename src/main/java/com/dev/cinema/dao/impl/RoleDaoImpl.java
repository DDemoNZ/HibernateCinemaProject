package com.dev.cinema.dao.impl;

import com.dev.cinema.dao.RoleDao;
import com.dev.cinema.model.Role;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl implements RoleDao {

    private final SessionFactory sessionFactory;

    public RoleDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Role add(Role role) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Long roleId = (Long) session.save(role);
            transaction.commit();
            role.setId(roleId);
            return role;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't add to DB role - " + role, e);
        }
    }

    @Override
    public Role getByRoleName(String roleName) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
            Root<Role> roleRoot = criteriaQuery.from(Role.class);
            criteriaQuery.select(roleRoot).where(criteriaBuilder.equal(roleRoot.get("roleName"),
                    roleName));
            return session.createQuery(criteriaQuery).uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Can't get from DB role - " + roleName, e);
        }
    }
}
