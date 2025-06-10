package com.colares.projeto.dao;

import com.colares.projeto.models.Schedule;
import com.colares.projeto.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleDAO {

  public void add(Schedule schedule) {
    EntityManager em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      em.persist(schedule);
      tx.commit();
    } catch (Exception e) {
      if (tx.isActive())
        tx.rollback();
      System.err.println("Erro ao adicionar Schedule: " + e.getMessage());
    } finally {
      em.close();
    }
  }

  public List<Schedule> getAll() {
    EntityManager em = JPAUtil.getEntityManager();
    List<Schedule> list = em.createQuery("FROM Schedule", Schedule.class).getResultList();
    em.close();
    return list;
  }

  public Schedule findById(int id) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      return em.find(Schedule.class, id);
    } catch (Exception e) {
      System.err.println("Erro ao buscar Schedule por ID: " + e.getMessage());
      return null;
    } finally {
      em.close();
    }
  }

  public void update(Schedule schedule) {
    EntityManager em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      em.merge(schedule);
      tx.commit();
    } catch (Exception e) {
      if (tx.isActive())
        tx.rollback();
      System.err.println("Erro ao atualizar Schedule: " + e.getMessage());
    } finally {
      em.close();
    }
  }

  public void delete(int id) {
    EntityManager em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      Schedule schedule = em.find(Schedule.class, id);
      if (schedule != null)
        em.remove(schedule);
      tx.commit();
    } catch (Exception e) {
      if (tx.isActive())
        tx.rollback();
      System.err.println("Erro ao excluir Schedule: " + e.getMessage());
    } finally {
      em.close();
    }
  }

  public List<Schedule> getByProfessionalId(int professionalId) {
    EntityManager em = JPAUtil.getEntityManager();
    List<Schedule> list = em.createQuery(
        "FROM Schedule s WHERE s.professional.id = :professionalId", Schedule.class)
        .setParameter("professionalId", professionalId)
        .getResultList();
    em.close();
    return list;
  }

  public boolean existsOverlappingSchedule(int professionalId, LocalDateTime start, LocalDateTime end) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      Long count = em.createQuery(
          "SELECT COUNT(s) " +
              "FROM Schedule s " +
              "WHERE s.professional.id = :professionalId " +
              "AND s.datetime < :end " +
              "AND FUNCTION('TIMESTAMPADD', SECOND, (s.service.duration * 60 + 900), s.datetime) > :start",
          Long.class)
          .setParameter("professionalId", professionalId)
          .setParameter("end", end)
          .setParameter("start", start)
          .getSingleResult();
      return count != null && count > 0;
    } catch (NoResultException e) {
      return false;
    } catch (Exception e) {
      System.err.println("Erro ao verificar sobreposição de Schedule: " + e.getMessage());
      return false;
    } finally {
      em.close();
    }
  }
}
