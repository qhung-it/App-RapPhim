package com.example.studentapp.apprapphim.model.dao.interf;

import com.example.studentapp.apprapphim.model.entity.KhachHang;

import java.util.List;

public interface KhachHangDAO {

    KhachHang findById(String maKH);

    KhachHang findByEmail(String email);

    List<KhachHang> findAll();

    List<KhachHang> findByLoaiKhachHang(String maLoai);

    KhachHang save(KhachHang khachHang);

    void delete(KhachHang khachHang);
}