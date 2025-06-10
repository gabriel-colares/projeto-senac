package com.colares.projeto.dao;

import com.colares.projeto.models.MoneyBox;
import com.colares.projeto.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class MoneyBoxDAO {

    public void add(MoneyBox moneyBox) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(moneyBox);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            System.err.println("Erro ao adicionar MoneyBox: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public List<MoneyBox> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        List<MoneyBox> list = em.createQuery("FROM MoneyBox", MoneyBox.class).getResultList();
        em.close();
        return list;
    }

    public MoneyBox getById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(MoneyBox.class, id);
        } catch (Exception e) {
            System.err.println("Erro ao buscar MoneyBox por ID: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    public void update(MoneyBox moneyBox) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(moneyBox);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            System.err.println("Erro ao atualizar MoneyBox: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            MoneyBox moneyBox = em.find(MoneyBox.class, id);
            if (moneyBox != null) {
                em.remove(moneyBox);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            System.err.println("Erro ao excluir MoneyBox: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
