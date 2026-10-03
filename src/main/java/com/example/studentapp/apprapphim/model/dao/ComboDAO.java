package com.example.studentapp.apprapphim.model.dao;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiCombo;
import com.example.studentapp.apprapphim.model.entity.Combo;

import java.util.List;

public interface ComboDAO {

    Combo findById(String maCombo);

    List<Combo> findAll();

    List<Combo> findByTrangThai(TrangThaiCombo trangThai);

    List<Combo> findByTenCombo(String tenCombo);

    Combo save(Combo combo);

    void delete(Combo combo);
}
