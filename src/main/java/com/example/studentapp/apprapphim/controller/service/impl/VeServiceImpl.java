package com.example.studentapp.apprapphim.controller.service.impl;

import com.example.studentapp.apprapphim.controller.service.interf.VeService;
import com.example.studentapp.apprapphim.model.Enum.TrangThaiVe;
import com.example.studentapp.apprapphim.model.dao.VeDAO;
import com.example.studentapp.apprapphim.model.dao.impl.VeDAOImpl;
import com.example.studentapp.apprapphim.model.entity.Ve;
import java.util.List;

public class VeServiceImpl implements VeService {

    private final VeDAO veDAO;

    public VeServiceImpl() {
        this(new VeDAOImpl());
    }

    public VeServiceImpl(VeDAO veDAO) {
        this.veDAO = veDAO;
    }

    @Override
    public Ve timTheoMa(String maVe) {
        return veDAO.findById(maVe);
    }

    @Override
    public Ve timTheoMaDienTu(String maVeDienTu) {
        return veDAO.findByMaVeDienTu(maVeDienTu);
    }

    @Override
    public List<Ve> timTheoDon(String maDon) {
        return veDAO.findByDonDatVe(maDon);
    }

    @Override
    public List<Ve> timTheoKhachHang(String maKH) {
        return veDAO.findByKhachHang(maKH);
    }

    @Override
    public List<Ve> timTheoSuatChieu(String maSuat) {
        return veDAO.findBySuatChieu(maSuat);
    }

    @Override
    public List<Ve> layDanhSachVe() {
        return veDAO.findAll();
    }

    @Override
    public boolean kiemTraVe(String maVe) {
        Ve ve = veDAO.findById(maVe);
        return ve != null && ve.kiemTraVe();
    }

    @Override
    public boolean suDungVe(String maVe) {
        Ve ve = veDAO.findById(maVe);
        if (ve == null || ve.getTrangThai() != TrangThaiVe.ChuaSuDung) return false;
        ve.suDungVe();
        veDAO.save(ve);
        return true;
    }
}
