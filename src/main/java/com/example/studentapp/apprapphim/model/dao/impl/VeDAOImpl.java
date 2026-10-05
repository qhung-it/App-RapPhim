package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.dao.VeDAO;
import com.example.studentapp.apprapphim.model.entity.Ve;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.util.List;

public class VeDAOImpl implements VeDAO {

    @Override
    public Ve findById(String maVe) {
        if (maVe == null || maVe.isBlank()) return null;
        return JpaDaoSupport.execute(em -> {
            List<Ve> result = em.createQuery("""
                    SELECT v FROM Ve v
                    JOIN FETCH v.donDatVe
                    JOIN FETCH v.suatChieu
                    JOIN FETCH v.ghe
                    WHERE v.maVe = :maVe
                    """, Ve.class)
                    .setParameter("maVe", maVe)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    @Override
    public Ve findByMaVeDienTu(String maVeDienTu) {
        if (maVeDienTu == null || maVeDienTu.isBlank()) return null;
        return JpaDaoSupport.execute(em -> {
            List<Ve> result = em.createQuery("""
                    SELECT v FROM Ve v
                    JOIN FETCH v.donDatVe
                    JOIN FETCH v.suatChieu
                    JOIN FETCH v.ghe
                    WHERE v.maVeDienTu = :code
                    """, Ve.class)
                    .setParameter("code", maVeDienTu.trim())
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    @Override
    public List<Ve> findAll() {
        return JpaDaoSupport.execute(em ->
                em.createQuery("SELECT v FROM Ve v ORDER BY v.maVe", Ve.class).getResultList());
    }

    @Override
    public List<Ve> findByDonDatVe(String maDon) {
        if (maDon == null || maDon.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT v FROM Ve v
                        JOIN FETCH v.suatChieu
                        JOIN FETCH v.ghe
                        WHERE v.donDatVe.maDon = :maDon
                        ORDER BY v.maVe
                        """, Ve.class)
                        .setParameter("maDon", maDon)
                        .getResultList());
    }

    @Override
    public List<Ve> findByKhachHang(String maKH) {
        if (maKH == null || maKH.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT v FROM Ve v
                        JOIN FETCH v.suatChieu
                        JOIN FETCH v.ghe
                        WHERE v.donDatVe.khachHang.maKH = :maKH
                        ORDER BY v.maVe
                        """, Ve.class)
                        .setParameter("maKH", maKH)
                        .getResultList());
    }

    @Override
    public List<Ve> findBySuatChieu(String maSuat) {
        if (maSuat == null || maSuat.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT v FROM Ve v
                        JOIN FETCH v.donDatVe
                        JOIN FETCH v.ghe
                        WHERE v.suatChieu.maSuat = :maSuat
                        ORDER BY v.ghe.hangGhe, v.ghe.soGhe
                        """, Ve.class)
                        .setParameter("maSuat", maSuat)
                        .getResultList());
    }


    @Override
    public List<String> findMaGheDaDat(String maSuat) {
        if (maSuat == null || maSuat.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT v.ghe.maGhe
                        FROM Ve v
                        WHERE v.suatChieu.maSuat = :maSuat
                          AND v.trangThai <> com.example.studentapp.apprapphim.model.Enum.TrangThaiVe.DaHuy
                        """, String.class)
                        .setParameter("maSuat", maSuat)
                        .getResultList());
    }

    @Override
    public boolean existsBySuatChieuAndGhe(String maSuat, String maGhe) {
        if (maSuat == null || maSuat.isBlank() || maGhe == null || maGhe.isBlank()) return false;
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT COUNT(v) FROM Ve v
                        WHERE v.suatChieu.maSuat = :maSuat
                          AND v.ghe.maGhe = :maGhe
                          AND v.trangThai <> com.example.studentapp.apprapphim.model.Enum.TrangThaiVe.DaHuy
                        """, Long.class)
                        .setParameter("maSuat", maSuat)
                        .setParameter("maGhe", maGhe)
                        .getSingleResult() > 0);
    }

    @Override
    public List<Ve> findBySuatChieuAndGhe(String maSuat, List<String> danhSachMaGhe) {
        if (maSuat == null || maSuat.isBlank() || danhSachMaGhe == null || danhSachMaGhe.isEmpty()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT v FROM Ve v
                        WHERE v.suatChieu.maSuat = :maSuat
                          AND v.ghe.maGhe IN :maGhe
                          AND v.trangThai <> com.example.studentapp.apprapphim.model.Enum.TrangThaiVe.DaHuy
                        """, Ve.class)
                        .setParameter("maSuat", maSuat)
                        .setParameter("maGhe", danhSachMaGhe)
                        .getResultList());
    }

    @Override
    public Ve save(Ve ve) {
        if (ve == null) throw new IllegalArgumentException("Vé không được null");
        return JpaDaoSupport.executeInTransaction(em -> {
            if (ve.getMaVe() != null && em.find(Ve.class, ve.getMaVe()) != null) return em.merge(ve);
            em.persist(ve);
            return ve;
        });
    }

    @Override
    public void delete(Ve ve) {
        if (ve == null) return;
        JpaDaoSupport.executeInTransaction(em -> {
            Ve managed = ve.getMaVe() == null ? null : em.find(Ve.class, ve.getMaVe());
            if (managed != null) em.remove(managed);
            return null;
        });
    }
}
