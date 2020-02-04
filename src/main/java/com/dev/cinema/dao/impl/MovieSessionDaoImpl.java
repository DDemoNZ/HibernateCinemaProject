package com.dev.cinema.dao.impl;

import com.dev.cinema.dao.MovieSessionDao;
import com.dev.cinema.lib.Dao;
import com.dev.cinema.model.MovieSession;
import com.dev.cinema.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class MovieSessionDaoImpl implements MovieSessionDao {

    private static final Logger LOGGER = LogManager.getLogger(MovieSessionDaoImpl.class);

    @Override
    public MovieSession add(MovieSession movieSession) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Long movieSessionId = (Long) session.save(movieSession);
            transaction.commit();
            movieSession.setId(movieSessionId);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Can't add movie session to DB", e);
            throw new RuntimeException("Can't add movie session to DB");
        }
        return movieSession;
    }

    @Override
    public List<MovieSession> findAvailableSessions(Long movieId, LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<MovieSession> criteriaQuery = criteriaBuilder
                    .createQuery(MovieSession.class);
            Root<MovieSession> movieSessionRoot = criteriaQuery.from(MovieSession.class);
            Predicate datePredicate = criteriaBuilder.between(movieSessionRoot.get("showTime"),
                    date.atStartOfDay(), date.plusDays(1).atStartOfDay());
            Predicate idPredicate = criteriaBuilder.equal(movieSessionRoot.get("movie"), movieId);
            criteriaQuery.select(movieSessionRoot)
                    .where(criteriaBuilder.and(datePredicate, idPredicate));
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            LOGGER.error("Can't find available sessions in DB", e);
            throw new RuntimeException("an't find available sessions in DB");
        }
    }
}
