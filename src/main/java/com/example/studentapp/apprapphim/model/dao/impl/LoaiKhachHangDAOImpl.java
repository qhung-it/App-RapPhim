package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.dao.interf.LoaiKhachHangDAO;
import com.example.studentapp.apprapphim.model.entity.LoaiKhachHang;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.util.List;

public class LoaiKhachHangDAOImpl implements LoaiKhachHangDAO {

    @Override
    public LoaiKhachHang findById(String maLoai) {
        if (maLoai == null || maLoai.isBlank()) return null;
        return JpaDaoSupport.execute(em -> em.find(LoaiKhachHang.class, maLoai));
    }

    @Override
    public List<LoaiKhachHang> findAll() {
        return JpaDaoSupport.execute(em ->
                em.createQuery("SELECT l FROM LoaiKhachHang l ORDER BY l.maLoai", LoaiKhachHang.class)
                        .getResultList());
    }

    @Override
    public LoaiKhachHang save(LoaiKhachHang loaiKhachHang) {
        if (loaiKhachHang == null) throw new IllegalArgumentException("Loại khách hàng không được null");
        return JpaDaoSupport.executeInTransaction(em -> {
            if (loaiKhachHang.getMaLoai() != null && em.find(LoaiKhachHang.class, loaiKhachHang.getMaLoai()) != null) return em.merge(loaiKhachHang);
            em.persist(loaiKhachHang);
            return loaiKhachHang;
        });
    }

    @Override
    public void delete(LoaiKhachHang loaiKhachHang) {
        if (loaiKhachHang == null) return;
        JpaDaoSupport.executeInTransaction(em -> {
            LoaiKhachHang managed = loaiKhachHang.getMaLoai() == null ? null : em.find(LoaiKhachHang.class, loaiKhachHang.getMaLoai());
            if (managed != null) em.remove(managed);
            return null;
        });
    }
}
