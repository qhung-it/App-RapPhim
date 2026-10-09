package com.example.studentapp.apprapphim.controller.service.interf;

import com.example.studentapp.apprapphim.model.entity.DonDatVe;
import com.example.studentapp.apprapphim.model.entity.KhuyenMai;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface KhuyenMaiService {

    boolean themKhuyenMai(KhuyenMai khuyenMai);

    boolean capNhatKhuyenMai(KhuyenMai khuyenMai);

    boolean xoaKhuyenMai(String maKM);

    KhuyenMai timTheoMa(String maKM);

    List<KhuyenMai> layDanhSachKhuyenMai();

    List<KhuyenMai> layKhuyenMaiDangApDung(LocalDate ngayApDung);

    List<KhuyenMai> timKhuyenMaiPhuHop(DonDatVe donDatVe);

    KhuyenMai xacDinhKhuyenMai(DonDatVe donDatVe);

    BigDecimal tinhTienGiam(KhuyenMai khuyenMai, BigDecimal tongTien);
}