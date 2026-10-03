package com.example.studentapp.apprapphim.controller.service.interf;

import com.example.studentapp.apprapphim.model.entity.Ghe;

import java.util.List;

public interface GheService {

    boolean themGhe(Ghe ghe);

    boolean capNhatGhe(Ghe ghe);

    boolean xoaGhe(String maGhe);

    Ghe timTheoMa(String maGhe);

    List<Ghe> layDanhSachGhe(String maPhong);

    List<Ghe> layGheTrong(String maSuat);

    List<Ghe> layGheDaBan(String maSuat);

    boolean kiemTraGheTrong(String maSuat, String maGhe);
}