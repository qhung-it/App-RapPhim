package com.example.studentapp.apprapphim.controller.service.impl;

import com.example.studentapp.apprapphim.controller.service.interf.PhongChieuService;
import com.example.studentapp.apprapphim.model.dao.interf.PhongChieuDAO;
import com.example.studentapp.apprapphim.model.dao.interf.SuatChieuDAO;
import com.example.studentapp.apprapphim.model.dao.impl.PhongChieuDAOImpl;
import com.example.studentapp.apprapphim.model.dao.impl.SuatChieuDAOImpl;
import com.example.studentapp.apprapphim.model.entity.PhongChieu;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PhongChieuServiceImpl implements PhongChieuService {

    private final PhongChieuDAO phongChieuDAO;
    private final SuatChieuDAO suatChieuDAO;

    public PhongChieuServiceImpl() {
        this(new PhongChieuDAOImpl(), new SuatChieuDAOImpl());
    }

    public PhongChieuServiceImpl(PhongChieuDAO phongChieuDAO, SuatChieuDAO suatChieuDAO) {
        this.phongChieuDAO = phongChieuDAO;
        this.suatChieuDAO = suatChieuDAO;
    }

    @Override
    public boolean themPhongChieu(PhongChieu phongChieu) {
        if (phongChieu == null || phongChieu.getMaPhong() == null || phongChieu.getMaPhong().isBlank()
                || phongChieu.getTenPhong() == null || phongChieu.getTenPhong().isBlank()
                || phongChieu.getSoLuongGhe() <= 0 || phongChieu.getRap() == null) return false;
        if (phongChieuDAO.findById(phongChieu.getMaPhong()) != null) return false;
        phongChieuDAO.save(phongChieu);
        return true;
    }

    @Override
    public boolean capNhatPhongChieu(PhongChieu phongChieu) {
        if (phongChieu == null || phongChieu.getMaPhong() == null || phongChieuDAO.findById(phongChieu.getMaPhong()) == null) return false;
        if (phongChieu.getTenPhong() == null || phongChieu.getTenPhong().isBlank()
                || phongChieu.getSoLuongGhe() <= 0 || phongChieu.getRap() == null) return false;
        phongChieuDAO.save(phongChieu);
        return true;
    }

    @Override
    public boolean xoaPhongChieu(String maPhong) {
        return JpaDaoSupport.executeInTransaction(em -> {
            PhongChieu phong = phongChieuDAO.findById(maPhong);
            if (phong == null) return false;
            if (!suatChieuDAO.findByPhong(maPhong).isEmpty()) return false;
            phongChieuDAO.delete(phong);
            return true;
        });
    }

    @Override
    public PhongChieu timTheoMa(String maPhong) {
        return phongChieuDAO.findById(maPhong);
    }

    @Override
    public List<PhongChieu> layDanhSachPhongCuaRap(String maRap) {
        return phongChieuDAO.findByRap(maRap);
    }

    @Override
    public boolean kiemTraLichChieu(String maPhong, LocalDate ngayChieu, LocalTime gioBatDau,
                                    LocalTime gioKetThuc, String maSuatLoaiTru) {
        return !suatChieuDAO.existsConflict(maPhong, ngayChieu, gioBatDau, gioKetThuc, maSuatLoaiTru);
    }
}
