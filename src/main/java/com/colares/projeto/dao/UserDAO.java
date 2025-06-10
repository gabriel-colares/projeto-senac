package com.colares.projeto.dao;

import com.colares.projeto.models.User;
import com.colares.projeto.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import java.util.List;

public class UserDAO {

  public void add(User user) {
    EntityManager em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      em.persist(user);
      tx.commit();
    } catch (Exception e) {
      if (tx.isActive()) {
        tx.rollback();
      }
      System.err.println("Erro ao adicionar User: " + e.getMessage());
    } finally {
      em.close();
    }
  }

  public List<User> getAll() {
    EntityManager em = JPAUtil.getEntityManager();
    List<User> list = em.createQuery("FROM User", User.class).getResultList();
    em.close();
    return list;
  }

  public User findById(int id) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      return em.find(User.class, id);
    } catch (Exception e) {
      System.err.println("Erro ao buscar User por ID: " + e.getMessage());
      return null;
    } finally {
      em.close();
    }
  }

  public void update(User user) {
    EntityManager em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      em.merge(user);
      tx.commit();
    } catch (Exception e) {
      if (tx.isActive()) {
        tx.rollback();
      }
      System.err.println("Erro ao atualizar User: " + e.getMessage());
    } finally {
      em.close();
    }
  }

  public void delete(int id) {
    EntityManager em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      User user = em.find(User.class, id);
      if (user != null) {
        em.remove(user);
      }
      tx.commit();
    } catch (Exception e) {
      if (tx.isActive()) {
        tx.rollback();
      }
      System.err.println("Erro ao excluir User: " + e.getMessage());
    } finally {
      em.close();
    }
  }

  public User authenticate(String username, String password) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      return em.createQuery(
          "SELECT u FROM User u WHERE u.username = :username AND u.password = :password",
          User.class)
          .setParameter("username", username)
          .setParameter("password", password)
          .getSingleResult();
    } catch (NoResultException e) {
      return null;
    } catch (Exception e) {
      System.err.println("Erro na autenticação: " + e.getMessage());
      return null;
    } finally {
      em.close();
    }
  }
}
