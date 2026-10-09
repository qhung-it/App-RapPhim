package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiKhuyenMai;
import com.example.studentapp.apprapphim.model.dao.interf.KhuyenMaiDAO;
import com.example.studentapp.apprapphim.model.entity.KhuyenMai;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.time.LocalDate;
import java.util.List;

public class KhuyenMaiDAOImpl implements KhuyenMaiDAO {

    @Override
    public KhuyenMai findById(String maKM) {
        if (maKM == null || maKM.isBlank()) return null;
        return JpaDaoSupport.execute(em -> em.find(KhuyenMai.class, maKM));
    }

    @Override
    public List<KhuyenMai> findAll() {
        return JpaDaoSupport.execute(em ->
                em.createQuery("SELECT k FROM KhuyenMai k ORDER BY k.ngayBatDau DESC, k.maKM", KhuyenMai.class)
                        .getResultList());
    }

    @Override
    public List<KhuyenMai> findByTrangThai(TrangThaiKhuyenMai trangThai) {
        if (trangThai == null) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT k FROM KhuyenMai k
                        WHERE k.trangThai = :trangThai
                        ORDER BY k.ngayBatDau DESC
                        """, KhuyenMai.class)
                        .setParameter("trangThai", trangThai)
                        .getResultList());
    }

    @Override
    public List<KhuyenMai> findDangApDung(LocalDate ngay) {
        if (ngay == null) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT k FROM KhuyenMai k
                        WHERE k.trangThai = com.example.studentapp.apprapphim.model.Enum.TrangThaiKhuyenMai.DangHoatDong
                          AND :ngay BETWEEN k.ngayBatDau AND k.ngayKetThuc
                        ORDER BY k.phanTramGiam DESC
                        """, KhuyenMai.class)
                        .setParameter("ngay", ngay)
                        .getResultList());
    }

    @Override
    public KhuyenMai save(KhuyenMai khuyenMai) {
        if (khuyenMai == null) throw new IllegalArgumentException("Khuyến mãi không được null");
        return JpaDaoSupport.executeInTransaction(em -> {
            if (khuyenMai.getMaKM() != null && em.find(KhuyenMai.class, khuyenMai.getMaKM()) != null) return em.merge(khuyenMai);
            em.persist(khuyenMai);
            return khuyenMai;
        });
    }

    @Override
    public void delete(KhuyenMai khuyenMai) {
        if (khuyenMai == null) return;
        JpaDaoSupport.executeInTransaction(em -> {
            KhuyenMai managed = khuyenMai.getMaKM() == null ? null : em.find(KhuyenMai.class, khuyenMai.getMaKM());
            if (managed != null) em.remove(managed);
            return null;
        });
    }
}
