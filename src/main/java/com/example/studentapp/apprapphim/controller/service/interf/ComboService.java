package com.example.studentapp.apprapphim.controller.service.interf;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiCombo;
import com.example.studentapp.apprapphim.model.entity.Combo;

import java.util.List;

public interface ComboService {

    boolean themCombo(Combo combo);

    boolean capNhatCombo(Combo combo);

    boolean xoaCombo(String maCombo);

    Combo timTheoMa(String maCombo);

    List<Combo> layDanhSachCombo();

    List<Combo> layComboDangBan();

    boolean capNhatTrangThai(String maCombo, TrangThaiCombo trangThai);
}