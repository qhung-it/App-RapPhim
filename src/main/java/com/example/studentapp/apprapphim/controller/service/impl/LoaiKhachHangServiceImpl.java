package com.example.studentapp.apprapphim.controller.service.impl;

import com.example.studentapp.apprapphim.controller.service.interf.LoaiKhachHangService;
import com.example.studentapp.apprapphim.model.dao.interf.LoaiKhachHangDAO;
import com.example.studentapp.apprapphim.model.dao.impl.LoaiKhachHangDAOImpl;
import com.example.studentapp.apprapphim.model.entity.LoaiKhachHang;
import java.util.List;

public class LoaiKhachHangServiceImpl implements LoaiKhachHangService {

    private final LoaiKhachHangDAO dao;

    public LoaiKhachHangServiceImpl() {
        this(new LoaiKhachHangDAOImpl());
    }

    public LoaiKhachHangServiceImpl(LoaiKhachHangDAO dao) {
        this.dao = dao;
    }

    @Override
    public boolean themLoaiKhachHang(LoaiKhachHang loaiKhachHang) {
        if (loaiKhachHang == null || loaiKhachHang.getMaLoai() == null || loaiKhachHang.getMaLoai().isBlank()
                || loaiKhachHang.getTenLoai() == null || loaiKhachHang.getTenLoai().isBlank()) return false;
        if (dao.findById(loaiKhachHang.getMaLoai()) != null) return false;
        dao.save(loaiKhachHang);
        return true;
    }

    @Override
    public boolean capNhatLoaiKhachHang(LoaiKhachHang loaiKhachHang) {
        if (loaiKhachHang == null || loaiKhachHang.getMaLoai() == null || dao.findById(loaiKhachHang.getMaLoai()) == null
                || loaiKhachHang.getTenLoai() == null || loaiKhachHang.getTenLoai().isBlank()) return false;
        dao.save(loaiKhachHang);
        return true;
    }

    @Override
    public boolean xoaLoaiKhachHang(String maLoai) {
        LoaiKhachHang entity = dao.findById(maLoai);
        if (entity == null) return false;
        dao.delete(entity);
        return true;
    }

    @Override
    public LoaiKhachHang timTheoMa(String maLoai) {
        return dao.findById(maLoai);
    }

    @Override
    public List<LoaiKhachHang> layDanhSachLoaiKhachHang() {
        return dao.findAll();
    }
}
