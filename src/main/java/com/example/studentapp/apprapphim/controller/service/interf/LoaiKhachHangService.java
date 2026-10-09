package com.example.studentapp.apprapphim.controller.service.interf;

import com.example.studentapp.apprapphim.model.entity.LoaiKhachHang;

import java.util.List;

public interface LoaiKhachHangService {

    boolean themLoaiKhachHang(LoaiKhachHang loaiKhachHang);

    boolean capNhatLoaiKhachHang(LoaiKhachHang loaiKhachHang);

    boolean xoaLoaiKhachHang(String maLoai);

    LoaiKhachHang timTheoMa(String maLoai);

    List<LoaiKhachHang> layDanhSachLoaiKhachHang();
}