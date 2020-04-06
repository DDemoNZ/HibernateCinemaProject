package com.dev.cinema.dao.impl;

import com.dev.cinema.dao.MovieDao;
import com.dev.cinema.model.Movie;
import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class MovieDaoImpl implements MovieDao {

    private final SessionFactory sessionFactory;

    public MovieDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Movie add(Movie movie) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Long movieId = (Long) session.save(movie);
            transaction.commit();
            movie.setId(movieId);
            return movie;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't add Movie entity to database", e);
        }
    }

    @Override
    public Movie update(Movie movie) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(movie);
            transaction.commit();
            return movie;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't update movie with id " + movie.getId());
        }
    }

    @Override
    public List<Movie> getAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<Movie> criteriaQuery = session.getCriteriaBuilder()
                    .createQuery(Movie.class);
            criteriaQuery.from(Movie.class);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieve all movies", e);
        }
    }

    @Override
    public Movie getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Movie.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Can't get user by id " + id);
        }
    }

}
