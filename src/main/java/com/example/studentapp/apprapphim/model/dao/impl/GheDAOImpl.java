package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.dao.GheDAO;
import com.example.studentapp.apprapphim.model.entity.Ghe;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.util.List;

public class GheDAOImpl implements GheDAO {

    @Override
    public Ghe findById(String maGhe) {
        if (maGhe == null || maGhe.isBlank()) return null;
        return JpaDaoSupport.execute(em -> {
            List<Ghe> result = em.createQuery("""
                    SELECT DISTINCT g FROM Ghe g
                    LEFT JOIN FETCH g.danhSachVe
                    WHERE g.maGhe = :maGhe
                    """, Ghe.class)
                    .setParameter("maGhe", maGhe)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    @Override
    public List<Ghe> findByPhongChieu(String maPhong) {
        if (maPhong == null || maPhong.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT g FROM Ghe g
                        WHERE g.phongChieu.maPhong = :maPhong
                        ORDER BY g.hangGhe, g.soGhe
                        """, Ghe.class)
                        .setParameter("maPhong", maPhong)
                        .getResultList());
    }

    @Override
    public List<Ghe> findAll() {
        return JpaDaoSupport.execute(em ->
                em.createQuery("SELECT g FROM Ghe g ORDER BY g.phongChieu.maPhong, g.hangGhe, g.soGhe", Ghe.class)
                        .getResultList());
    }

    @Override
    public Ghe save(Ghe ghe) {
        if (ghe == null) throw new IllegalArgumentException("Ghế không được null");
        return JpaDaoSupport.executeInTransaction(em -> {
            if (ghe.getMaGhe() != null && em.find(Ghe.class, ghe.getMaGhe()) != null) return em.merge(ghe);
            em.persist(ghe);
            return ghe;
        });
    }

    @Override
    public void delete(Ghe ghe) {
        if (ghe == null) return;
        JpaDaoSupport.executeInTransaction(em -> {
            Ghe managed = ghe.getMaGhe() == null ? null : em.find(Ghe.class, ghe.getMaGhe());
            if (managed != null) em.remove(managed);
            return null;
        });
    }
}
