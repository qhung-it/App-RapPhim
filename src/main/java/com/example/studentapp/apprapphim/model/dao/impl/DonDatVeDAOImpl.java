package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiDon;
import com.example.studentapp.apprapphim.model.dao.DonDatVeDAO;
import com.example.studentapp.apprapphim.model.entity.DonDatVe;
import com.example.studentapp.apprapphim.model.entity.Ve;
import com.example.studentapp.apprapphim.model.entity.ChiTietCombo;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.util.List;

public class DonDatVeDAOImpl implements DonDatVeDAO {

    private static final String DETAIL_FETCH = """
            SELECT DISTINCT d FROM DonDatVe d
            LEFT JOIN FETCH d.khachHang
            LEFT JOIN FETCH d.nhanVien
            LEFT JOIN FETCH d.khuyenMai
            LEFT JOIN FETCH d.thanhToan
            LEFT JOIN FETCH d.danhSachVe v
            LEFT JOIN FETCH v.suatChieu
            LEFT JOIN FETCH v.ghe
            LEFT JOIN FETCH d.danhSachCombo c
            LEFT JOIN FETCH c.combo
            """;

    @Override
    public DonDatVe findById(String maDon) {
        if (maDon == null || maDon.isBlank()) return null;

        return JpaDaoSupport.execute(em -> {
            // Query 1: lấy đơn + các quan hệ đơn trị + danh sách vé.
            // Chỉ fetch một collection trong query để tránh Cartesian Product.
            List<DonDatVe> orders = em.createQuery("""
                    SELECT DISTINCT d FROM DonDatVe d
                    LEFT JOIN FETCH d.khachHang
                    LEFT JOIN FETCH d.nhanVien
                    LEFT JOIN FETCH d.khuyenMai
                    LEFT JOIN FETCH d.thanhToan
                    LEFT JOIN FETCH d.danhSachVe v
                    LEFT JOIN FETCH v.suatChieu
                    LEFT JOIN FETCH v.ghe
                    WHERE d.maDon = :maDon
                    """, DonDatVe.class)
                    .setParameter("maDon", maDon.trim())
                    .getResultList();

            if (orders.isEmpty()) return null;
            DonDatVe don = orders.get(0);

            // Query 2: fetch collection combo riêng, tránh JOIN 2 collection cùng lúc.
            // Query này fetch trực tiếp collection của DonDatVe nên không cần
            // chạy thêm một query lazy nữa khi transaction kết thúc.
            em.createQuery("""
                    SELECT DISTINCT d FROM DonDatVe d
                    LEFT JOIN FETCH d.danhSachCombo c
                    LEFT JOIN FETCH c.combo
                    WHERE d.maDon = :maDon
                    """, DonDatVe.class)
                    .setParameter("maDon", maDon.trim())
                    .getResultList();
            return don;
        });
    }

    @Override
    public List<DonDatVe> findAll() {
        return JpaDaoSupport.execute(em ->
                em.createQuery(
                                "SELECT d FROM DonDatVe d ORDER BY d.ngayDat DESC", DonDatVe.class)
                        .getResultList());
    }

    @Override
    public List<DonDatVe> findByKhachHang(String maKH) {
        if (maKH == null || maKH.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT d FROM DonDatVe d
                        WHERE d.khachHang.maKH = :maKH
                        ORDER BY d.ngayDat DESC
                        """, DonDatVe.class)
                        .setParameter("maKH", maKH)
                        .getResultList());
    }

    @Override
    public List<DonDatVe> findByNhanVien(String maNV) {
        if (maNV == null || maNV.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT d FROM DonDatVe d
                        WHERE d.nhanVien.maNV = :maNV
                        ORDER BY d.ngayDat DESC
                        """, DonDatVe.class)
                        .setParameter("maNV", maNV)
                        .getResultList());
    }

    @Override
    public List<DonDatVe> findByTrangThai(TrangThaiDon trangThai) {
        if (trangThai == null) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT d FROM DonDatVe d
                        WHERE d.trangThai = :trangThai
                        ORDER BY d.ngayDat DESC
                        """, DonDatVe.class)
                        .setParameter("trangThai", trangThai)
                        .getResultList());
    }

    @Override
    public DonDatVe save(DonDatVe donDatVe) {
        if (donDatVe == null) throw new IllegalArgumentException("Đơn đặt vé không được null");
        return JpaDaoSupport.executeInTransaction(em -> {
            if (donDatVe.getMaDon() != null && em.find(DonDatVe.class, donDatVe.getMaDon()) != null) {
                return em.merge(donDatVe);
            }
            em.persist(donDatVe);
            return donDatVe;
        });
    }

    @Override
    public void delete(DonDatVe donDatVe) {
        if (donDatVe == null) return;
        JpaDaoSupport.executeInTransaction(em -> {
            DonDatVe managed = donDatVe.getMaDon() == null ? null : em.find(DonDatVe.class, donDatVe.getMaDon());
            if (managed != null) em.remove(managed);
            return null;
        });
    }
}
