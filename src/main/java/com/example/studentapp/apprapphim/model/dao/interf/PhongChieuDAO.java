package com.example.studentapp.apprapphim.model.dao.interf;

import com.example.studentapp.apprapphim.model.entity.PhongChieu;

import java.util.List;

public interface PhongChieuDAO {
    PhongChieu findById(String maPhong);

    List<PhongChieu> findAll();

    List<PhongChieu> findByRap(String maRap);

    PhongChieu save(PhongChieu phongChieu);

    void delete(PhongChieu phongChieu);
}
