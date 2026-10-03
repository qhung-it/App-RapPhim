package com.example.studentapp.apprapphim.controller.service.interf;

import com.example.studentapp.apprapphim.model.entity.Ve;

import java.util.List;

public interface VeService {

    Ve timTheoMa(String maVe);

    Ve timTheoMaDienTu(String maVeDienTu);

    List<Ve> timTheoDon(String maDon);

    List<Ve> timTheoKhachHang(String maKH);

    List<Ve> timTheoSuatChieu(String maSuat);

    List<Ve> layDanhSachVe();

    boolean kiemTraVe(String maVe);

    boolean suDungVe(String maVe);
}