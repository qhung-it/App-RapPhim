package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.dao.SuatChieuDAO;
import com.example.studentapp.apprapphim.model.entity.SuatChieu;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SuatChieuDAOImpl implements SuatChieuDAO {

    @Override
    public SuatChieu findById(String maSuat) {
        if (maSuat == null || maSuat.isBlank()) return null;
        return JpaDaoSupport.execute(em -> {
            List<SuatChieu> result = em.createQuery("""
                    SELECT s FROM SuatChieu s
                    JOIN FETCH s.phim
                    JOIN FETCH s.phongChieu
                    WHERE s.maSuat = :maSuat
                    """, SuatChieu.class)
                    .setParameter("maSuat", maSuat)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    @Override
    public List<SuatChieu> findAll() {
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT s FROM SuatChieu s
                        JOIN FETCH s.phim
                        JOIN FETCH s.phongChieu
                        ORDER BY s.ngayChieu, s.gioBatDau
                        """, SuatChieu.class).getResultList());
    }

    @Override
    public List<SuatChieu> findByPhim(String maPhim) {
        if (maPhim == null || maPhim.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT s FROM SuatChieu s
                        JOIN FETCH s.phim
                        JOIN FETCH s.phongChieu
                        WHERE s.phim.maPhim = :maPhim
                        ORDER BY s.ngayChieu, s.gioBatDau
                        """, SuatChieu.class)
                        .setParameter("maPhim", maPhim)
                        .getResultList());
    }

    @Override
    public List<SuatChieu> findByNgay(LocalDate ngayChieu) {
        if (ngayChieu == null) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT s FROM SuatChieu s
                        JOIN FETCH s.phim
                        JOIN FETCH s.phongChieu
                        WHERE s.ngayChieu = :ngay
                        ORDER BY s.gioBatDau
                        """, SuatChieu.class)
                        .setParameter("ngay", ngayChieu)
                        .getResultList());
    }

    @Override
    public List<SuatChieu> findByPhong(String maPhong) {
        if (maPhong == null || maPhong.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT s FROM SuatChieu s
                        JOIN FETCH s.phim
                        JOIN FETCH s.phongChieu
                        WHERE s.phongChieu.maPhong = :maPhong
                        ORDER BY s.ngayChieu, s.gioBatDau
                        """, SuatChieu.class)
                        .setParameter("maPhong", maPhong)
                        .getResultList());
    }

    @Override
    public List<SuatChieu> findByPhimAndNgay(String maPhim, LocalDate ngayChieu) {
        if (maPhim == null || maPhim.isBlank() || ngayChieu == null) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT s FROM SuatChieu s
                        JOIN FETCH s.phim
                        JOIN FETCH s.phongChieu
                        WHERE s.phim.maPhim = :maPhim
                          AND s.ngayChieu = :ngay
                        ORDER BY s.gioBatDau
                        """, SuatChieu.class)
                        .setParameter("maPhim", maPhim)
                        .setParameter("ngay", ngayChieu)
                        .getResultList());
    }

    @Override
    public boolean existsConflict(String maPhong, LocalDate ngayChieu, LocalTime gioBatDau,
                                  LocalTime gioKetThuc, String maSuatLoaiTru) {
        if (maPhong == null || maPhong.isBlank() || ngayChieu == null
                || gioBatDau == null || gioKetThuc == null || !gioBatDau.isBefore(gioKetThuc)) {
            return false;
        }

        return JpaDaoSupport.execute(em -> {
            String jpql = """
                    SELECT COUNT(s) FROM SuatChieu s
                    WHERE s.phongChieu.maPhong = :maPhong
                      AND s.ngayChieu = :ngay
                      AND s.gioBatDau < :gioKetThuc
                      AND s.gioKetThuc > :gioBatDau
                    """;
            if (maSuatLoaiTru != null && !maSuatLoaiTru.isBlank()) {
                jpql += " AND s.maSuat <> :maSuat";
            }
            var query = em.createQuery(jpql, Long.class)
                    .setParameter("maPhong", maPhong)
                    .setParameter("ngay", ngayChieu)
                    .setParameter("gioKetThuc", gioKetThuc)
                    .setParameter("gioBatDau", gioBatDau);
            if (maSuatLoaiTru != null && !maSuatLoaiTru.isBlank()) query.setParameter("maSuat", maSuatLoaiTru);
            return query.getSingleResult() > 0;
        });
    }

    @Override
    public SuatChieu save(SuatChieu suatChieu) {
        if (suatChieu == null) throw new IllegalArgumentException("Suất chiếu không được null");
        return JpaDaoSupport.executeInTransaction(em -> {
            if (suatChieu.getMaSuat() != null && em.find(SuatChieu.class, suatChieu.getMaSuat()) != null) return em.merge(suatChieu);
            em.persist(suatChieu);
            return suatChieu;
        });
    }

    @Override
    public void delete(SuatChieu suatChieu) {
        if (suatChieu == null) return;
        JpaDaoSupport.executeInTransaction(em -> {
            SuatChieu managed = suatChieu.getMaSuat() == null ? null : em.find(SuatChieu.class, suatChieu.getMaSuat());
            if (managed != null) em.remove(managed);
            return null;
        });
    }
}
