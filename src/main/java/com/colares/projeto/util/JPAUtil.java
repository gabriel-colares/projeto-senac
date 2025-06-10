package com.colares.projeto.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
  private static final EntityManagerFactory emf;

  static {
    emf = Persistence.createEntityManagerFactory("venustBusinessPU");
  }

  public static EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public static void close() {
    if (emf != null && emf.isOpen()) {
      emf.close();
    }
  }
}
