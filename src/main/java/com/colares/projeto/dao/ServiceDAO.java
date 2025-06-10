package com.colares.projeto.dao;

import com.colares.projeto.models.Service;
import com.colares.projeto.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import java.util.List;

public class ServiceDAO {

  public void add(Service service) {
    EntityManager em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      em.persist(service);
      tx.commit();
    } catch (Exception e) {
      if (tx.isActive())
        tx.rollback();
      System.err.println("Erro ao adicionar Service: " + e.getMessage());
    } finally {
      em.close();
    }
  }

  public List<Service> getAll() {
    EntityManager em = JPAUtil.getEntityManager();
    List<Service> list = em.createQuery("FROM Service", Service.class).getResultList();
    em.close();
    return list;
  }

  public Service findById(int id) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      return em.find(Service.class, id);
    } catch (Exception e) {
      System.err.println("Erro ao buscar Service por ID: " + e.getMessage());
      return null;
    } finally {
      em.close();
    }
  }

  public void update(Service service) {
    EntityManager em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      em.merge(service);
      tx.commit();
    } catch (Exception e) {
      if (tx.isActive())
        tx.rollback();
      System.err.println("Erro ao atualizar Service: " + e.getMessage());
    } finally {
      em.close();
    }
  }

  public void delete(int id) {
    EntityManager em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      Service service = em.find(Service.class, id);
      if (service != null) {
        em.remove(service);
      }
      tx.commit();
    } catch (Exception e) {
      if (tx.isActive())
        tx.rollback();
      System.err.println("Erro ao excluir Service: " + e.getMessage());
    } finally {
      em.close();
    }
  }

  public Service findByName(String name) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      return em.createQuery(
          "FROM Service s WHERE LOWER(s.name) = :name", Service.class)
          .setParameter("name", name.toLowerCase())
          .getResultStream()
          .findFirst()
          .orElse(null);
    } finally {
      em.close();
    }
  }

  public List<Service> findByDescription(String description) {
    EntityManager em = JPAUtil.getEntityManager();
    List<Service> result;
    try {
      result = em.createQuery(
          "FROM Service s WHERE LOWER(s.description) LIKE :description", Service.class)
          .setParameter("description", "%" + description.toLowerCase() + "%")
          .getResultList();
    } finally {
      em.close();
    }
    return result;
  }

  public List<Service> findByPrice(double price) {
    EntityManager em = JPAUtil.getEntityManager();
    List<Service> result;
    try {
      result = em.createQuery(
          "FROM Service s WHERE s.price = :price", Service.class)
          .setParameter("price", price)
          .getResultList();
    } finally {
      em.close();
    }
    return result;
  }

  public boolean existsByName(String name) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      Long count = em.createQuery(
          "SELECT COUNT(s) FROM Service s WHERE LOWER(s.name) = :name", Long.class)
          .setParameter("name", name.toLowerCase())
          .getSingleResult();
      return count != null && count > 0;
    } finally {
      em.close();
    }
  }

  public int getServiceIdByName(String name) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      return em.createQuery(
          "SELECT s.id FROM Service s WHERE s.name = :name", Integer.class)
          .setParameter("name", name)
          .getSingleResult();
    } catch (NoResultException e) {
      return -1;
    } catch (Exception e) {
      System.err.println("Erro ao buscar ID do Service: " + e.getMessage());
      return -1;
    } finally {
      em.close();
    }
  }

  public String getServiceNameById(int id) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      Service servico = em.find(Service.class, id);
      return servico != null ? servico.getName() : "Desconhecido";
    } finally {
      em.close();
    }
  }

  public int getServiceDurationByName(String serviceName) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      Integer duration = em.createQuery(
          "SELECT s.duration FROM Service s WHERE s.name = :serviceName", Integer.class)
          .setParameter("serviceName", serviceName)
          .getSingleResult();
      return duration != null ? duration : 0;
    } catch (NoResultException e) {
      return 0;
    } catch (Exception e) {
      System.err.println("Erro ao buscar duração do Service: " + e.getMessage());
      return 0;
    } finally {
      em.close();
    }
  }

  public int getServiceDurationById(int serviceId) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      Integer duration = em.createQuery(
          "SELECT s.duration FROM Service s WHERE s.id = :serviceId", Integer.class)
          .setParameter("serviceId", serviceId)
          .getSingleResult();
      return duration != null ? duration : 0;
    } catch (NoResultException e) {
      return 0;
    } catch (Exception e) {
      System.err.println("Erro ao buscar duração do Service: " + e.getMessage());
      return 0;
    } finally {
      em.close();
    }
  }

  public Service getById(int id) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      return em.find(Service.class, id);
    } finally {
      em.close();
    }
  }
}
