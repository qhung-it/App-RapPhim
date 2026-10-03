package com.example.studentapp.apprapphim.controller.service.impl;

import com.example.studentapp.apprapphim.controller.service.interf.GheService;
import com.example.studentapp.apprapphim.model.dao.GheDAO;
import com.example.studentapp.apprapphim.model.dao.VeDAO;
import com.example.studentapp.apprapphim.model.dao.impl.GheDAOImpl;
import com.example.studentapp.apprapphim.model.dao.impl.VeDAOImpl;
import com.example.studentapp.apprapphim.model.dao.SuatChieuDAO;
import com.example.studentapp.apprapphim.model.dao.impl.SuatChieuDAOImpl;
import com.example.studentapp.apprapphim.model.entity.Ghe;
import java.util.List;

public class GheServiceImpl implements GheService {

    private final GheDAO gheDAO;
    private final VeDAO veDAO;
    private final SuatChieuDAO suatChieuDAO;

    public GheServiceImpl() {
        this(new GheDAOImpl(), new VeDAOImpl(), new SuatChieuDAOImpl());
    }

    public GheServiceImpl(GheDAO gheDAO, VeDAO veDAO, SuatChieuDAO suatChieuDAO) {
        this.gheDAO = gheDAO;
        this.veDAO = veDAO;
        this.suatChieuDAO = suatChieuDAO;
    }

    @Override
    public boolean themGhe(Ghe ghe) {
        if (ghe == null || ghe.getMaGhe() == null || ghe.getMaGhe().isBlank()
                || ghe.getPhongChieu() == null || ghe.getGia() == null || ghe.getGia().signum() < 0) return false;
        if (gheDAO.findById(ghe.getMaGhe()) != null) return false;
        gheDAO.save(ghe);
        return true;
    }

    @Override
    public boolean capNhatGhe(Ghe ghe) {
        if (ghe == null || ghe.getMaGhe() == null || gheDAO.findById(ghe.getMaGhe()) == null) return false;
        if (ghe.getPhongChieu() == null || ghe.getGia() == null || ghe.getGia().signum() < 0) return false;
        gheDAO.save(ghe);
        return true;
    }

    @Override
    public boolean xoaGhe(String maGhe) {
        Ghe ghe = gheDAO.findById(maGhe);
        if (ghe == null) return false;
        if (ghe.getDanhSachVe() != null && !ghe.getDanhSachVe().isEmpty()) return false;
        gheDAO.delete(ghe);
        return true;
    }

    @Override
    public Ghe timTheoMa(String maGhe) {
        return gheDAO.findById(maGhe);
    }

    @Override
    public List<Ghe> layDanhSachGhe(String maPhong) {
        return gheDAO.findByPhongChieu(maPhong);
    }

    @Override
    public List<Ghe> layGheTrong(String maSuat) {
        var suatChieu = suatChieuDAO.findById(maSuat);
        if (suatChieu == null || suatChieu.getPhongChieu() == null) return List.of();
        List<Ghe> ghePhong = gheDAO.findByPhongChieu(suatChieu.getPhongChieu().getMaPhong());
        return ghePhong.stream()
                .filter(g -> !veDAO.existsBySuatChieuAndGhe(maSuat, g.getMaGhe()))
                .toList();
    }

    @Override
    public List<Ghe> layGheDaBan(String maSuat) {
        return veDAO.findBySuatChieu(maSuat).stream()
                .map(com.example.studentapp.apprapphim.model.entity.Ve::getGhe)
                .toList();
    }

    @Override
    public boolean kiemTraGheTrong(String maSuat, String maGhe) {
        return !veDAO.existsBySuatChieuAndGhe(maSuat, maGhe);
    }
}
