package com.example.studentapp.apprapphim.model.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("AppRapPhim");

    private JpaUtil() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void close() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
