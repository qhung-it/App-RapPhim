package com.example.studentapp.apprapphim.controller.service.impl;

import com.example.studentapp.apprapphim.controller.service.interf.NhanVienService;
import com.example.studentapp.apprapphim.model.Enum.ChucVu;
import com.example.studentapp.apprapphim.model.dao.NhanVienDAO;
import com.example.studentapp.apprapphim.model.dao.impl.NhanVienDAOImpl;
import com.example.studentapp.apprapphim.model.entity.NhanVien;
import java.util.List;

public class NhanVienServiceImpl implements NhanVienService {

    private final NhanVienDAO nhanVienDAO;

    public NhanVienServiceImpl() {
        this(new NhanVienDAOImpl());
    }

    public NhanVienServiceImpl(NhanVienDAO nhanVienDAO) {
        this.nhanVienDAO = nhanVienDAO;
    }

    @Override
    public NhanVien dangNhap(String email, String matKhau) {
        if (email == null || email.isBlank() || matKhau == null || matKhau.isBlank()) return null;
        NhanVien nv = nhanVienDAO.findByEmail(email);
        return nv != null && matKhau.equals(nv.getMatKhau()) ? nv : null;
    }

    @Override
    public boolean themNhanVien(NhanVien nhanVien) {
        if (nhanVien == null || nhanVien.getMaNV() == null || nhanVien.getMaNV().isBlank()
                || nhanVien.getHoTen() == null || nhanVien.getHoTen().isBlank()
                || nhanVien.getMatKhau() == null || nhanVien.getMatKhau().isBlank()) return false;
        if (nhanVienDAO.findById(nhanVien.getMaNV()) != null) return false;
        if (nhanVien.getEmail() != null && nhanVienDAO.findByEmail(nhanVien.getEmail()) != null) return false;
        nhanVienDAO.save(nhanVien);
        return true;
    }

    @Override
    public boolean capNhatNhanVien(NhanVien nhanVien) {
        if (nhanVien == null || nhanVien.getMaNV() == null || nhanVienDAO.findById(nhanVien.getMaNV()) == null
                || nhanVien.getHoTen() == null || nhanVien.getHoTen().isBlank()) return false;
        if (nhanVien.getEmail() != null) {
            NhanVien other = nhanVienDAO.findByEmail(nhanVien.getEmail());
            if (other != null && !nhanVien.getMaNV().equals(other.getMaNV())) return false;
        }
        nhanVienDAO.save(nhanVien);
        return true;
    }

    @Override
    public boolean xoaNhanVien(String maNV) {
        NhanVien nv = nhanVienDAO.findById(maNV);
        if (nv == null) return false;
        nhanVienDAO.delete(nv);
        return true;
    }

    @Override
    public NhanVien timTheoMa(String maNV) {
        return nhanVienDAO.findById(maNV);
    }

    @Override
    public List<NhanVien> layDanhSachNhanVien() {
        return nhanVienDAO.findAll();
    }

    @Override
    public boolean phanQuyen(String maNV, ChucVu chucVu) {
        NhanVien nv = nhanVienDAO.findById(maNV);
        if (nv == null || chucVu == null) return false;
        nv.setChucVu(chucVu);
        nhanVienDAO.save(nv);
        return true;
    }
}
