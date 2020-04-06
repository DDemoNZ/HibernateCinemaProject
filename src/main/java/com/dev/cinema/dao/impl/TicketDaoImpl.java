package com.dev.cinema.dao.impl;

import com.dev.cinema.dao.TicketDao;
import com.dev.cinema.model.Ticket;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class TicketDaoImpl implements TicketDao {

    private final SessionFactory sessionFactory;

    public TicketDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Ticket add(Ticket ticket) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Long ticketId = (Long) session.save(ticket);
            transaction.commit();
            ticket.setId(ticketId);
            return ticket;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't add ticket to DB", e);
        }
    }

    @Override
    public Ticket getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Ticket.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Can't get user by id " + id);
        }
    }
}
