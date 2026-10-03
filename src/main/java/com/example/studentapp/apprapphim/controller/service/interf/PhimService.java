package com.example.studentapp.apprapphim.controller.service.interf;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiPhim;
import com.example.studentapp.apprapphim.model.entity.Phim;
import com.example.studentapp.apprapphim.model.entity.SuatChieu;

import java.util.List;

public interface PhimService {

    boolean themPhim(Phim phim);

    boolean capNhatPhim(Phim phim);

    boolean xoaPhim(String maPhim);

    Phim timTheoMa(String maPhim);

    List<Phim> layDanhSachPhim();

    List<Phim> layPhimDangChieu();

    List<Phim> layPhimSapChieu();

    List<Phim> timKiem(
            String tenPhim,
            String theLoai,
            TrangThaiPhim trangThai,
            Integer doTuoi
    );

    List<SuatChieu> laySuatChieu(
            String maPhim
    );
}