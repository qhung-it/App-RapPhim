package com.example.studentapp.apprapphim.controller.service.impl;

import com.example.studentapp.apprapphim.controller.service.interf.PhimService;
import com.example.studentapp.apprapphim.model.Enum.TrangThaiPhim;
import com.example.studentapp.apprapphim.model.dao.PhimDAO;
import com.example.studentapp.apprapphim.model.dao.SuatChieuDAO;
import com.example.studentapp.apprapphim.model.dao.impl.PhimDAOImpl;
import com.example.studentapp.apprapphim.model.dao.impl.SuatChieuDAOImpl;
import com.example.studentapp.apprapphim.model.entity.Phim;
import com.example.studentapp.apprapphim.model.entity.SuatChieu;
import java.util.List;

public class PhimServiceImpl implements PhimService {

    private final PhimDAO phimDAO;
    private final SuatChieuDAO suatChieuDAO;

    public PhimServiceImpl() {
        this(new PhimDAOImpl(), new SuatChieuDAOImpl());
    }

    public PhimServiceImpl(PhimDAO phimDAO, SuatChieuDAO suatChieuDAO) {
        this.phimDAO = phimDAO;
        this.suatChieuDAO = suatChieuDAO;
    }

    @Override
    public boolean themPhim(Phim phim) {
        if (phim == null || phim.getMaPhim() == null || phim.getMaPhim().isBlank()
                || phim.getTenPhim() == null || phim.getTenPhim().isBlank()
                || phim.getThoiLuong() <= 0 || phim.getDoTuoi() < 0) return false;
        if (phimDAO.findById(phim.getMaPhim()) != null) return false;
        phimDAO.save(phim);
        return true;
    }

    @Override
    public boolean capNhatPhim(Phim phim) {
        if (phim == null || phim.getMaPhim() == null || phimDAO.findById(phim.getMaPhim()) == null) return false;
        if (phim.getTenPhim() == null || phim.getTenPhim().isBlank()
                || phim.getThoiLuong() <= 0 || phim.getDoTuoi() < 0) return false;
        phimDAO.save(phim);
        return true;
    }

    @Override
    public boolean xoaPhim(String maPhim) {
        Phim phim = phimDAO.findById(maPhim);
        if (phim == null) return false;
        if (!suatChieuDAO.findByPhim(maPhim).isEmpty()) return false;
        phimDAO.delete(phim);
        return true;
    }

    @Override
    public Phim timTheoMa(String maPhim) {
        return phimDAO.findById(maPhim);
    }

    @Override
    public List<Phim> layDanhSachPhim() {
        return phimDAO.findAll();
    }

    @Override
    public List<Phim> layPhimDangChieu() {
        return phimDAO.findByTrangThai(TrangThaiPhim.DangChieu);
    }

    @Override
    public List<Phim> layPhimSapChieu() {
        return phimDAO.findByTrangThai(TrangThaiPhim.SapChieu);
    }

    @Override
    public List<Phim> timKiem(String tenPhim, String theLoai, TrangThaiPhim trangThai, Integer doTuoi) {
        return phimDAO.timKiem(tenPhim, theLoai, trangThai, doTuoi);
    }

    @Override
    public List<SuatChieu> laySuatChieu(String maPhim) {
        return suatChieuDAO.findByPhim(maPhim);
    }
}
