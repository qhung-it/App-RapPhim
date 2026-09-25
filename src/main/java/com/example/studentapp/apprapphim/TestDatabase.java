package com.example.studentapp.apprapphim;

import com.example.studentapp.apprapphim.model.util.JpaUtil;
import jakarta.persistence.EntityManagerFactory;

public class TestDatabase {

    public static void main(String[] args) {
        System.out.println("Kết nối SQL Server...");

        EntityManagerFactory emf = JpaUtil.getEntityManagerFactory();

        System.out.println("Kết nối SQL Server thành công!");

        emf.close();
    }
}
