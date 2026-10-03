package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiDon;
import com.example.studentapp.apprapphim.model.Enum.TrangThaiThanhToan;
import com.example.studentapp.apprapphim.model.dao.ThongKeDoanhThuDAO;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ThongKeDoanhhThuDaoImpl implements ThongKeDoanhThuDAO {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private LocalDateTime start(LocalDate date) {
        return date.atStartOfDay();
    }

    private LocalDateTime end(LocalDate date) {
        return date.plusDays(1).atStartOfDay();
    }

    @Override
    public BigDecimal doanhThuTheoNgay(LocalDate ngay) {
        if (ngay == null) return ZERO;
        return JpaDaoSupport.execute(em -> {
            BigDecimal result = em.createQuery("""
                    SELECT COALESCE(SUM(t.soTien), 0)
                    FROM ThanhToan t
                    WHERE t.trangThai = :trangThaiTT
                      AND t.donDatVe.trangThai = :trangThaiDon
                      AND t.thoiGian >= :fromTime
                      AND t.thoiGian < :toTime
                    """, BigDecimal.class)
                    .setParameter("trangThaiTT", TrangThaiThanhToan.ThanhCong)
                    .setParameter("trangThaiDon", TrangThaiDon.DaThanhToan)
                    .setParameter("fromTime", start(ngay))
                    .setParameter("toTime", end(ngay))
                    .getSingleResult();
            return result == null ? ZERO : result;
        });
    }

    @Override
    public BigDecimal doanhThuTheoThang(int thang, int nam) {
        if (thang < 1 || thang > 12) return ZERO;
        LocalDate from = LocalDate.of(nam, thang, 1);
        return doanhThuTrongKhoang(from, from.plusMonths(1));
    }

    @Override
    public BigDecimal doanhThuTheoNam(int nam) {
        LocalDate from = LocalDate.of(nam, 1, 1);
        return doanhThuTrongKhoang(from, from.plusYears(1));
    }

    private BigDecimal doanhThuTrongKhoang(LocalDate from, LocalDate toExclusive) {
        return JpaDaoSupport.execute(em -> {
            BigDecimal result = em.createQuery("""
                    SELECT COALESCE(SUM(t.soTien), 0)
                    FROM ThanhToan t
                    WHERE t.trangThai = :trangThaiTT
                      AND t.donDatVe.trangThai = :trangThaiDon
                      AND t.thoiGian >= :fromTime
                      AND t.thoiGian < :toTime
                    """, BigDecimal.class)
                    .setParameter("trangThaiTT", TrangThaiThanhToan.ThanhCong)
                    .setParameter("trangThaiDon", TrangThaiDon.DaThanhToan)
                    .setParameter("fromTime", start(from))
                    .setParameter("toTime", start(toExclusive))
                    .getSingleResult();
            return result == null ? ZERO : result;
        });
    }

    @Override
    public BigDecimal doanhThuVeTheoNgay(LocalDate ngay) {
        if (ngay == null) return ZERO;
        return JpaDaoSupport.execute(em -> {
            BigDecimal result = em.createQuery("""
                    SELECT COALESCE(SUM(v.giaVe + s.giaSuat + g.gia), 0)
                    FROM Ve v
                    JOIN v.donDatVe d
                    JOIN d.thanhToan t
                    JOIN v.suatChieu s
                    JOIN v.ghe g
                    WHERE d.trangThai = :trangThaiDon
                      AND t.trangThai = :trangThaiTT
                      AND t.thoiGian >= :fromTime
                      AND t.thoiGian < :toTime
                    """, BigDecimal.class)
                    .setParameter("trangThaiDon", TrangThaiDon.DaThanhToan)
                    .setParameter("trangThaiTT", TrangThaiThanhToan.ThanhCong)
                    .setParameter("fromTime", start(ngay))
                    .setParameter("toTime", end(ngay))
                    .getSingleResult();
            return result == null ? ZERO : result;
        });
    }

    @Override
    public BigDecimal doanhThuComboTheoNgay(LocalDate ngay) {
        if (ngay == null) return ZERO;
        return JpaDaoSupport.execute(em -> {
            BigDecimal result = em.createQuery("""
                    SELECT COALESCE(SUM(c.thanhTien), 0)
                    FROM ChiTietCombo c
                    JOIN c.donDatVe d
                    JOIN d.thanhToan t
                    WHERE d.trangThai = :trangThaiDon
                      AND t.trangThai = :trangThaiTT
                      AND t.thoiGian >= :fromTime
                      AND t.thoiGian < :toTime
                    """, BigDecimal.class)
                    .setParameter("trangThaiDon", TrangThaiDon.DaThanhToan)
                    .setParameter("trangThaiTT", TrangThaiThanhToan.ThanhCong)
                    .setParameter("fromTime", start(ngay))
                    .setParameter("toTime", end(ngay))
                    .getSingleResult();
            return result == null ? ZERO : result;
        });
    }

    @Override
    public BigDecimal doanhThuTheoPhim(String maPhim) {
        if (maPhim == null || maPhim.isBlank()) return ZERO;
        return doanhThuTheoDimension("s.phim.maPhim", maPhim);
    }

    @Override
    public BigDecimal doanhThuTheoRap(String maRap) {
        if (maRap == null || maRap.isBlank()) return ZERO;
        return doanhThuTheoDimension("s.phongChieu.rap.maRap", maRap);
    }

    @Override
    public BigDecimal doanhThuTheoPhong(String maPhong) {
        if (maPhong == null || maPhong.isBlank()) return ZERO;
        return doanhThuTheoDimension("s.phongChieu.maPhong", maPhong);
    }

    private BigDecimal doanhThuTheoDimension(String fieldPath, String value) {
        return JpaDaoSupport.execute(em -> {
            BigDecimal result = em.createQuery("""
                    SELECT COALESCE(SUM(v.giaVe + s.giaSuat + g.gia), 0)
                    FROM Ve v
                    JOIN v.donDatVe d
                    JOIN d.thanhToan t
                    JOIN v.suatChieu s
                    JOIN v.ghe g
                    WHERE d.trangThai = :trangThaiDon
                      AND t.trangThai = :trangThaiTT
                      AND %s = :value
                    """.formatted(fieldPath), BigDecimal.class)
                    .setParameter("trangThaiDon", TrangThaiDon.DaThanhToan)
                    .setParameter("trangThaiTT", TrangThaiThanhToan.ThanhCong)
                    .setParameter("value", value)
                    .getSingleResult();
            return result == null ? ZERO : result;
        });
    }

    @Override
    public long soVeBanTheoNgay(LocalDate ngay) {
        if (ngay == null) return 0;
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT COUNT(v)
                        FROM Ve v
                        JOIN v.donDatVe d
                        JOIN d.thanhToan t
                        WHERE d.trangThai = :trangThaiDon
                          AND t.trangThai = :trangThaiTT
                          AND t.thoiGian >= :fromTime
                          AND t.thoiGian < :toTime
                        """, Long.class)
                        .setParameter("trangThaiDon", TrangThaiDon.DaThanhToan)
                        .setParameter("trangThaiTT", TrangThaiThanhToan.ThanhCong)
                        .setParameter("fromTime", start(ngay))
                        .setParameter("toTime", end(ngay))
                        .getSingleResult());
    }
}
