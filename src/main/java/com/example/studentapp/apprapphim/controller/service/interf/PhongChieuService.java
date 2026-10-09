package com.example.studentapp.apprapphim.controller.service.interf;

import com.example.studentapp.apprapphim.model.entity.PhongChieu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PhongChieuService {

    boolean themPhongChieu(PhongChieu phongChieu);

    boolean capNhatPhongChieu(PhongChieu phongChieu);

    boolean xoaPhongChieu(String maPhong);

    PhongChieu timTheoMa(String maPhong);

    List<PhongChieu> layDanhSachPhongCuaRap(
            String maRap
    );

    boolean kiemTraLichChieu(
            String maPhong,
            LocalDate ngayChieu,
            LocalTime gioBatDau,
            LocalTime gioKetThuc,
            String maSuatLoaiTru
    );
}