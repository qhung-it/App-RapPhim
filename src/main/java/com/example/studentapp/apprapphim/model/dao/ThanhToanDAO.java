package com.example.studentapp.apprapphim.model.dao;

import com.example.studentapp.apprapphim.model.entity.ThanhToan;

import java.util.List;

public interface ThanhToanDAO {

    ThanhToan findById(String maTT);

    ThanhToan findByDonDatVe(String maDon);

    List<ThanhToan> findAll();

    ThanhToan save(ThanhToan thanhToan);

    void delete(ThanhToan thanhToan);
}