package com.example.studentapp.apprapphim.model.dao;

import com.example.studentapp.apprapphim.model.entity.Ve;

import java.util.List;

public interface VeDAO {

    Ve findById(String maVe);

    Ve findByMaVeDienTu(String maVeDienTu);

    List<Ve> findAll();

    List<Ve> findByDonDatVe(String maDon);

    List<Ve> findByKhachHang(String maKH);

    List<Ve> findBySuatChieu(String maSuat);

    List<String> findMaGheDaDat(String maSuat);

    boolean existsBySuatChieuAndGhe(String maSuat, String maGhe);

    List<Ve> findBySuatChieuAndGhe(String maSuat, List<String> danhSachMaGhe);

    Ve save(Ve ve);

    void delete(Ve ve);
}