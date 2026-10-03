package com.example.studentapp.apprapphim.model.dao;

import com.example.studentapp.apprapphim.model.entity.ChiTietCombo;

import java.util.List;

public interface ChiTietComboDAO {

    ChiTietCombo findById(Long id);

    List<ChiTietCombo> findByDonDatVe(String maDon);

    List<ChiTietCombo> findByCombo(String maCombo);

    ChiTietCombo findByDonDatVeAndCombo(String maDon, String maCombo);

    ChiTietCombo save(ChiTietCombo chiTietCombo);

    void delete(ChiTietCombo chiTietCombo);
}