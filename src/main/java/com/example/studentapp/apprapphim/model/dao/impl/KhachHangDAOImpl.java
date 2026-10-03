package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.dao.KhachHangDAO;
import com.example.studentapp.apprapphim.model.entity.KhachHang;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.util.List;

public class KhachHangDAOImpl implements KhachHangDAO {

    @Override
    public KhachHang findById(String maKhachHang) {
        if (maKhachHang == null || maKhachHang.isBlank()) return null;
        return JpaDaoSupport.execute(em -> em.find(KhachHang.class, maKhachHang));
    }

    @Override
    public KhachHang findByEmail(String email) {
        if (email == null || email.isBlank()) return null;
        return JpaDaoSupport.execute(em -> {
            List<KhachHang> result = em.createQuery("""
                    SELECT k FROM KhachHang k
                    LEFT JOIN FETCH k.loaiKhachHang
                    WHERE LOWER(k.email) = LOWER(:email)
                    """, KhachHang.class)
                    .setParameter("email", email.trim())
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    @Override
    public List<KhachHang> findAll() {
        return JpaDaoSupport.execute(em ->
                em.createQuery("SELECT k FROM KhachHang k ORDER BY k.maKH", KhachHang.class)
                        .getResultList());
    }

    @Override
    public List<KhachHang> findByLoaiKhachHang(String maLoai) {
        if (maLoai == null || maLoai.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT k FROM KhachHang k
                        WHERE k.loaiKhachHang.maLoai = :maLoai
                        ORDER BY k.hoTen
                        """, KhachHang.class)
                        .setParameter("maLoai", maLoai)
                        .getResultList());
    }

    @Override
    public KhachHang save(KhachHang khachHang) {
        if (khachHang == null) throw new IllegalArgumentException("Khách hàng không được null");
        return JpaDaoSupport.executeInTransaction(em -> {
            if (khachHang.getMaKH() != null && em.find(KhachHang.class, khachHang.getMaKH()) != null) return em.merge(khachHang);
            em.persist(khachHang);
            return khachHang;
        });
    }

    @Override
    public void delete(KhachHang khachHang) {
        if (khachHang == null) return;
        JpaDaoSupport.executeInTransaction(em -> {
            KhachHang managed = khachHang.getMaKH() == null ? null : em.find(KhachHang.class, khachHang.getMaKH());
            if (managed != null) em.remove(managed);
            return null;
        });
    }
}
