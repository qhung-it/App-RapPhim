package com.example.studentapp.apprapphim.model.dao;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiPhim;
import com.example.studentapp.apprapphim.model.entity.Phim;

import java.util.List;

public interface PhimDAO {

    Phim findById(String maPhim);

    List<Phim> findAll();

    List<Phim> findByTenPhim(
            String tenPhim
    );

    List<Phim> findByTheLoai(
            String theLoai
    );

    List<Phim> findByTrangThai(
            TrangThaiPhim trangThai
    );

    List<Phim> timKiem(
            String tenPhim,
            String theLoai,
            TrangThaiPhim trangThai,
            Integer doTuoi
    );

    Phim save(Phim phim);

    void delete(Phim phim);
}