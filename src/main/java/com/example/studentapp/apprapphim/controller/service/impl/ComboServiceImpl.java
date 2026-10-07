package com.example.studentapp.apprapphim.controller.service.impl;

import com.example.studentapp.apprapphim.controller.service.interf.ComboService;
import com.example.studentapp.apprapphim.model.Enum.TrangThaiCombo;
import com.example.studentapp.apprapphim.model.dao.interf.ComboDAO;
import com.example.studentapp.apprapphim.model.dao.impl.ComboDAOImpl;
import com.example.studentapp.apprapphim.model.entity.Combo;
import java.util.List;

public class ComboServiceImpl implements ComboService {

    private final ComboDAO comboDAO;

    public ComboServiceImpl() {
        this(new ComboDAOImpl());
    }

    public ComboServiceImpl(ComboDAO comboDAO) {
        this.comboDAO = comboDAO;
    }

    @Override
    public boolean themCombo(Combo combo) {
        if (combo == null || combo.getMaCombo() == null || combo.getMaCombo().isBlank()
                || combo.getTenCombo() == null || combo.getTenCombo().isBlank()
                || combo.getGia() == null || combo.getGia().signum() < 0) return false;
        if (comboDAO.findById(combo.getMaCombo()) != null) return false;
        comboDAO.save(combo);
        return true;
    }

    @Override
    public boolean capNhatCombo(Combo combo) {
        if (combo == null || combo.getMaCombo() == null || comboDAO.findById(combo.getMaCombo()) == null) return false;
        if (combo.getTenCombo() == null || combo.getTenCombo().isBlank()
                || combo.getGia() == null || combo.getGia().signum() < 0) return false;
        comboDAO.save(combo);
        return true;
    }

    @Override
    public boolean xoaCombo(String maCombo) {
        Combo combo = comboDAO.findById(maCombo);
        if (combo == null) return false;
        comboDAO.delete(combo);
        return true;
    }

    @Override
    public Combo timTheoMa(String maCombo) {
        return comboDAO.findById(maCombo);
    }

    @Override
    public List<Combo> layDanhSachCombo() {
        return comboDAO.findAll();
    }

    @Override
    public List<Combo> layComboDangBan() {
        return comboDAO.findByTrangThai(TrangThaiCombo.DangBan);
    }

    @Override
    public boolean capNhatTrangThai(String maCombo, TrangThaiCombo trangThai) {
        Combo combo = comboDAO.findById(maCombo);
        if (combo == null || trangThai == null) return false;
        combo.setTrangThai(trangThai);
        comboDAO.save(combo);
        return true;
    }
}
