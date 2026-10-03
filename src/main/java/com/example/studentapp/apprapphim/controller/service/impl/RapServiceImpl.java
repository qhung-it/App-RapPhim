package com.example.studentapp.apprapphim.controller.service.impl;

import com.example.studentapp.apprapphim.controller.service.interf.RapService;
import com.example.studentapp.apprapphim.model.dao.PhongChieuDAO;
import com.example.studentapp.apprapphim.model.dao.RapDAO;
import com.example.studentapp.apprapphim.model.dao.impl.PhongChieuDAOImpl;
import com.example.studentapp.apprapphim.model.dao.impl.RapDAOImpl;
import com.example.studentapp.apprapphim.model.entity.Rap;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.util.List;

public class RapServiceImpl implements RapService {

    private final RapDAO rapDAO;
    private final PhongChieuDAO phongChieuDAO;

    public RapServiceImpl() {
        this(new RapDAOImpl(), new PhongChieuDAOImpl());
    }

    public RapServiceImpl(RapDAO rapDAO, PhongChieuDAO phongChieuDAO) {
        this.rapDAO = rapDAO;
        this.phongChieuDAO = phongChieuDAO;
    }

    @Override
    public boolean themRap(Rap rap) {
        if (rap == null || rap.getMaRap() == null || rap.getMaRap().isBlank()
                || rap.getTenRap() == null || rap.getTenRap().isBlank()) return false;
        if (rapDAO.findById(rap.getMaRap()) != null) return false;
        rapDAO.save(rap);
        return true;
    }

    @Override
    public boolean capNhatRap(Rap rap) {
        if (rap == null || rap.getMaRap() == null || rapDAO.findById(rap.getMaRap()) == null
                || rap.getTenRap() == null || rap.getTenRap().isBlank()) return false;
        rapDAO.save(rap);
        return true;
    }

    @Override
    public boolean xoaRap(String maRap) {
        return JpaDaoSupport.executeInTransaction(em -> {
            Rap rap = rapDAO.findById(maRap);
            if (rap == null) return false;
            if (!phongChieuDAO.findByRap(maRap).isEmpty()) return false;
            rapDAO.delete(rap);
            return true;
        });
    }

    @Override
    public Rap timTheoMa(String maRap) {
        return rapDAO.findById(maRap);
    }

    @Override
    public List<Rap> layDanhSachRap() {
        return rapDAO.findAll();
    }
}
