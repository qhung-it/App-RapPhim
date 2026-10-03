package com.example.studentapp.apprapphim.model.dao;

import com.example.studentapp.apprapphim.model.entity.Ghe;

import java.util.List;

public interface GheDAO {

    Ghe findById(String maGhe);

    List<Ghe> findByPhongChieu(String maPhong);

    List<Ghe> findAll();

    Ghe save(Ghe ghe);

    void delete(Ghe ghe);
}