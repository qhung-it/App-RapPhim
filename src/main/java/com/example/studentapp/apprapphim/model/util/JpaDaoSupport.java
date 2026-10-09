package com.example.studentapp.apprapphim.model.util;

import jakarta.persistence.EntityManager;
import java.util.function.Function;

/**
 * Tiện ích dùng chung cho DAO.
 *
 * Khi Service mở transaction, các DAO được gọi bên trong sẽ dùng chung
 * EntityManager hiện tại. Nhờ đó một nghiệp vụ có nhiều DAO chỉ commit
 * một lần và rollback toàn bộ nếu có lỗi.
 */
public final class JpaDaoSupport {

    private static final ThreadLocal<EntityManager> CURRENT_ENTITY_MANAGER = new ThreadLocal<>();

    private JpaDaoSupport() {
    }

    public static <T> T execute(Function<EntityManager, T> action) {
        EntityManager current = CURRENT_ENTITY_MANAGER.get();
        if (current != null && current.isOpen()) {
            return action.apply(current);
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return action.apply(em);
        } finally {
            close(em);
        }
    }

    /**
     * Thực thi một transaction ở tầng Service.
     * Nếu transaction đã được mở ở tầng ngoài thì không tạo/commit transaction mới.
     */
    public static <T> T executeInTransaction(Function<EntityManager, T> action) {
        EntityManager current = CURRENT_ENTITY_MANAGER.get();

        if (current != null && current.isOpen()) {
            return action.apply(current);
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        var tx = em.getTransaction();
        CURRENT_ENTITY_MANAGER.set(em);

        try {
            tx.begin();
            T result = action.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            CURRENT_ENTITY_MANAGER.remove();
            close(em);
        }
    }

    private static void close(EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
