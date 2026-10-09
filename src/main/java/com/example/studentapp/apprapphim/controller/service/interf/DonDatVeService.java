package com.example.studentapp.apprapphim.controller.service.interf;

import com.example.studentapp.apprapphim.model.entity.DonDatVe;
import com.example.studentapp.apprapphim.model.entity.KhuyenMai;
import com.example.studentapp.apprapphim.model.entity.Ve;

import java.math.BigDecimal;
import java.util.List;

public interface DonDatVeService {

    DonDatVe taoDon(String maKH, String maNV);

    DonDatVe timTheoMa(String maDon);

    List<DonDatVe> layDanhSachDon();

    List<DonDatVe> layDonTheoKhachHang(String maKH);

    boolean themVe(String maDon, Ve ve);

    boolean themNhieuVe(String maDon, List<Ve> danhSachVe);

    boolean xoaVe(String maDon, String maVe);

    boolean themCombo(String maDon, String maCombo, int soLuong);

    boolean capNhatSoLuongCombo(String maDon, String maCombo, int soLuong);

    boolean xoaCombo(String maDon, String maCombo);

    BigDecimal tinhTongTien(String maDon);

    KhuyenMai xacDinhKhuyenMai(String maDon);

    boolean apDungKhuyenMai(String maDon, String maKM);

    boolean huyDon(String maDon);

    boolean xacNhanDon(String maDon);
}