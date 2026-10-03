package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.dao.PhongChieuDAO;
import com.example.studentapp.apprapphim.model.entity.PhongChieu;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.util.List;

public class PhongChieuDAOImpl implements PhongChieuDAO {

    @Override
    public PhongChieu findById(String maPhong) {
        if (maPhong == null || maPhong.isBlank()) return null;
        return JpaDaoSupport.execute(em -> em.find(PhongChieu.class, maPhong));
    }

    @Override
    public List<PhongChieu> findAll() {
        return JpaDaoSupport.execute(em ->
                em.createQuery("SELECT p FROM PhongChieu p ORDER BY p.maPhong", PhongChieu.class).getResultList());
    }

    @Override
    public List<PhongChieu> findByRap(String maRap) {
        if (maRap == null || maRap.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT p FROM PhongChieu p
                        WHERE p.rap.maRap = :maRap
                        ORDER BY p.tenPhong
                        """, PhongChieu.class)
                        .setParameter("maRap", maRap)
                        .getResultList());
    }

    @Override
    public PhongChieu save(PhongChieu phongChieu) {
        if (phongChieu == null) throw new IllegalArgumentException("Phòng chiếu không được null");
        return JpaDaoSupport.executeInTransaction(em -> {
            if (phongChieu.getMaPhong() != null && em.find(PhongChieu.class, phongChieu.getMaPhong()) != null) return em.merge(phongChieu);
            em.persist(phongChieu);
            return phongChieu;
        });
    }

    @Override
    public void delete(PhongChieu phongChieu) {
        if (phongChieu == null) return;
        JpaDaoSupport.executeInTransaction(em -> {
            PhongChieu managed = phongChieu.getMaPhong() == null ? null : em.find(PhongChieu.class, phongChieu.getMaPhong());
            if (managed != null) em.remove(managed);
            return null;
        });
    }
}
