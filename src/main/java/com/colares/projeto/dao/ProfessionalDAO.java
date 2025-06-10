package com.colares.projeto.dao;

import com.colares.projeto.models.Professional;
import com.colares.projeto.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import java.util.List;

public class ProfessionalDAO {

  public void add(Professional professional) {
    EntityManager em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      em.persist(professional);
      tx.commit();
    } catch (Exception e) {
      if (tx.isActive())
        tx.rollback();
      System.err.println("Erro ao adicionar Professional: " + e.getMessage());
    } finally {
      em.close();
    }
  }

  public List<Professional> getAll() {
    EntityManager em = JPAUtil.getEntityManager();
    List<Professional> list = em.createQuery("FROM Professional", Professional.class).getResultList();
    em.close();
    return list;
  }

  public void update(Professional professional) {
    EntityManager em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      em.merge(professional);
      tx.commit();
    } catch (Exception e) {
      if (tx.isActive())
        tx.rollback();
      System.err.println("Erro ao atualizar Professional: " + e.getMessage());
    } finally {
      em.close();
    }
  }

  public void delete(int id) {
    EntityManager em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      Professional professional = em.find(Professional.class, id);
      if (professional != null) {
        em.remove(professional);
      }
      tx.commit();
    } catch (Exception e) {
      if (tx.isActive())
        tx.rollback();
      System.err.println("Erro ao excluir Professional: " + e.getMessage());
    } finally {
      em.close();
    }
  }

  public Professional findByName(String name) {
    EntityManager em = JPAUtil.getEntityManager();
    Professional result = null;
    try {
      result = em.createQuery(
          "FROM Professional p WHERE LOWER(p.name) = :name", Professional.class)
          .setParameter("name", name.toLowerCase())
          .getResultStream()
          .findFirst()
          .orElse(null);
    } finally {
      em.close();
    }
    return result;
  }

  public Professional findByCpf(String cpf) {
    EntityManager em = JPAUtil.getEntityManager();
    Professional result = null;
    try {
      result = em.createQuery(
          "FROM Professional p WHERE p.cpf = :cpf", Professional.class)
          .setParameter("cpf", cpf)
          .getResultStream()
          .findFirst()
          .orElse(null);
    } finally {
      em.close();
    }
    return result;
  }

  public int getProfessionalIdByName(String name) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      return em.createQuery(
          "SELECT p.id FROM Professional p WHERE p.name = :name", Integer.class)
          .setParameter("name", name)
          .getSingleResult();
    } catch (NoResultException e) {
      return -1;
    } catch (Exception e) {
      System.err.println("Erro ao buscar ID do Professional: " + e.getMessage());
      return -1;
    } finally {
      em.close();
    }
  }

  public String getProfessionalNameById(int id) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      Professional prof = em.find(Professional.class, id);
      return prof != null ? prof.getName() : "Desconhecido";
    } finally {
      em.close();
    }
  }

  public Professional getById(int id) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      return em.find(Professional.class, id);
    } finally {
      em.close();
    }
  }
}
