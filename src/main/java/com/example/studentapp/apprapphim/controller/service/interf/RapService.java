package com.example.studentapp.apprapphim.controller.service.interf;

import com.example.studentapp.apprapphim.model.entity.Rap;

import java.util.List;

public interface RapService {
    boolean themRap(Rap rap);

    boolean capNhatRap(Rap rap);

    boolean xoaRap(String maRap);

    Rap timTheoMa(String maRap);

    List<Rap> layDanhSachRap();
}
