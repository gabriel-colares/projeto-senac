package com.colares.projeto.dao;

import com.colares.projeto.models.Client;
import com.colares.projeto.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import java.util.List;

public class ClientDAO {

    public void add(Client client) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(client);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Client> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Client> list = em.createQuery("FROM Client", Client.class).getResultList();
        em.close();
        return list;
    }

    public void update(Client client) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(client);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Client client = em.find(Client.class, id);
            if (client != null) {
                em.remove(client);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public int getClientIdByName(String name) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Client client = em.createQuery(
                    "SELECT c FROM Client c WHERE c.name = :name", Client.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return client.getId();
        } catch (NoResultException e) {
            // Cliente não encontrado
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            em.close();
        }
    }

    public String getClientNameById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Client client = em.find(Client.class, id);
            return client != null ? client.getName() : "Desconhecido";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro";
        } finally {
            em.close();
        }
    }

    public Client getById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }

}
