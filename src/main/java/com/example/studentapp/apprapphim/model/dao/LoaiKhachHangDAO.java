package com.example.studentapp.apprapphim.model.dao;

import com.example.studentapp.apprapphim.model.entity.LoaiKhachHang;

import java.util.List;

public interface LoaiKhachHangDAO {
    LoaiKhachHang findById(String maLoai);

    List<LoaiKhachHang> findAll();

    LoaiKhachHang save(LoaiKhachHang loaiKhachHang);

    void delete(LoaiKhachHang loaiKhachHang);
}