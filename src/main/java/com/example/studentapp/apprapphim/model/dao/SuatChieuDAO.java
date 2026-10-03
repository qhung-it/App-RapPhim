package com.example.studentapp.apprapphim.model.dao;

import com.example.studentapp.apprapphim.model.entity.SuatChieu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SuatChieuDAO {

    SuatChieu findById(String maSuat);

    List<SuatChieu> findAll();

    List<SuatChieu> findByPhim(String maPhim);

    List<SuatChieu> findByNgay(LocalDate ngayChieu);

    List<SuatChieu> findByPhong(String maPhong);

    List<SuatChieu> findByPhimAndNgay(String maPhim, LocalDate ngayChieu);

    boolean existsConflict(String maPhong, LocalDate ngayChieu, LocalTime gioBatDau, LocalTime gioKetThuc, String maSuatLoaiTru);

    SuatChieu save(SuatChieu suatChieu);

    void delete(SuatChieu suatChieu);
}