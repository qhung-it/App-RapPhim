package com.example.studentapp.apprapphim.controller.service.interf;

import com.example.studentapp.apprapphim.model.entity.DonDatVe;
import com.example.studentapp.apprapphim.model.entity.KhachHang;

import java.util.List;

public interface KhachHangService {

    boolean dangKy(KhachHang khachHang);

    KhachHang dangNhap(String email, String matKhau);

    boolean doiMatKhau(String maKH, String matKhauCu, String matKhauMoi);

    boolean quenMatKhau(String email);

    boolean xacThucOTP(String email, String otp);

    boolean datLaiMatKhau(String email, String matKhauMoi);

    KhachHang timTheoMa(String maKH);

    KhachHang timTheoEmail(String email);

    List<DonDatVe> xemLichSuDatVe(String maKH);

    List<DonDatVe> xemDonDatVe(String maKH);
}