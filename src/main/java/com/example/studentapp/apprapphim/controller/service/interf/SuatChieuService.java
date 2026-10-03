package com.example.studentapp.apprapphim.controller.service.interf;

import com.example.studentapp.apprapphim.model.entity.Ghe;
import com.example.studentapp.apprapphim.model.entity.SuatChieu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SuatChieuService {

    boolean taoSuatChieu(SuatChieu suatChieu);

    boolean capNhatSuatChieu(SuatChieu suatChieu);

    boolean xoaSuatChieu(String maSuat);

    SuatChieu timTheoMa(String maSuat);

    List<SuatChieu> layDanhSachSuatChieu();

    List<SuatChieu> timSuatChieu(String maPhim, LocalDate ngayChieu, String maPhong);

    List<SuatChieu> laySuatChieuTheoPhim(String maPhim);

    List<SuatChieu> laySuatChieuTheoNgay(LocalDate ngayChieu);

    List<Ghe> layGheTrong(String maSuat);

    boolean kiemTraGheTrong(String maSuat, List<String> danhSachMaGhe);

    boolean kiemTraTrungLich(String maPhong, LocalDate ngayChieu, LocalTime gioBatDau, LocalTime gioKetThuc, String maSuatLoaiTru);
}
