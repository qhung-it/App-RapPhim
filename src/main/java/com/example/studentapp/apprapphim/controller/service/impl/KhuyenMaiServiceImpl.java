package com.example.studentapp.apprapphim.controller.service.impl;

import com.example.studentapp.apprapphim.controller.service.interf.KhuyenMaiService;
import com.example.studentapp.apprapphim.model.dao.interf.KhuyenMaiDAO;
import com.example.studentapp.apprapphim.model.dao.impl.KhuyenMaiDAOImpl;
import com.example.studentapp.apprapphim.model.entity.DonDatVe;
import com.example.studentapp.apprapphim.model.entity.KhuyenMai;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class KhuyenMaiServiceImpl implements KhuyenMaiService {

    private final KhuyenMaiDAO khuyenMaiDAO;

    public KhuyenMaiServiceImpl() {
        this(new KhuyenMaiDAOImpl());
    }

    public KhuyenMaiServiceImpl(KhuyenMaiDAO khuyenMaiDAO) {
        this.khuyenMaiDAO = khuyenMaiDAO;
    }

    @Override
    public boolean themKhuyenMai(KhuyenMai khuyenMai) {
        if (!hopLe(khuyenMai) || khuyenMaiDAO.findById(khuyenMai.getMaKM()) != null) return false;
        khuyenMaiDAO.save(khuyenMai);
        return true;
    }

    @Override
    public boolean capNhatKhuyenMai(KhuyenMai khuyenMai) {
        if (!hopLe(khuyenMai) || khuyenMaiDAO.findById(khuyenMai.getMaKM()) == null) return false;
        khuyenMaiDAO.save(khuyenMai);
        return true;
    }

    private boolean hopLe(KhuyenMai km) {
        return km != null
                && km.getMaKM() != null && !km.getMaKM().isBlank()
                && km.getTenKM() != null && !km.getTenKM().isBlank()
                && km.getPhanTramGiam() != null
                && km.getPhanTramGiam().compareTo(BigDecimal.ZERO) >= 0
                && km.getPhanTramGiam().compareTo(BigDecimal.valueOf(100)) <= 0
                && km.getNgayBatDau() != null
                && km.getNgayKetThuc() != null
                && !km.getNgayKetThuc().isBefore(km.getNgayBatDau());
    }

    @Override
    public boolean xoaKhuyenMai(String maKM) {
        KhuyenMai km = khuyenMaiDAO.findById(maKM);
        if (km == null) return false;
        khuyenMaiDAO.delete(km);
        return true;
    }

    @Override
    public KhuyenMai timTheoMa(String maKM) {
        return khuyenMaiDAO.findById(maKM);
    }

    @Override
    public List<KhuyenMai> layDanhSachKhuyenMai() {
        return khuyenMaiDAO.findAll();
    }

    @Override
    public List<KhuyenMai> layKhuyenMaiDangApDung(LocalDate ngayApDung) {
        return khuyenMaiDAO.findDangApDung(ngayApDung);
    }

    @Override
    public List<KhuyenMai> timKhuyenMaiPhuHop(DonDatVe donDatVe) {
        if (donDatVe == null) return List.of();
        LocalDate ngay = donDatVe.getNgayDat() == null ? LocalDate.now() : donDatVe.getNgayDat().toLocalDate();
        BigDecimal tongTien = donDatVe.tinhTongTien();
        return khuyenMaiDAO.findDangApDung(ngay).stream()
                .filter(km -> tinhTienGiam(km, tongTien).signum() > 0)
                .toList();
    }

    @Override
    public KhuyenMai xacDinhKhuyenMai(DonDatVe donDatVe) {
        return timKhuyenMaiPhuHop(donDatVe).stream()
                .max((a, b) -> tinhTienGiam(a, donDatVe.tinhTongTien())
                        .compareTo(tinhTienGiam(b, donDatVe.tinhTongTien())))
                .orElse(null);
    }

    @Override
    public BigDecimal tinhTienGiam(KhuyenMai khuyenMai, BigDecimal tongTien) {
        if (khuyenMai == null) return BigDecimal.ZERO;
        return khuyenMai.tinhTienGiam(tongTien);
    }
}
