package com.example.studentapp.apprapphim.controller.service.impl;

import com.example.studentapp.apprapphim.controller.service.interf.ThongKeDoanhThuService;
import com.example.studentapp.apprapphim.model.dao.ThongKeDoanhThuDAO;
import com.example.studentapp.apprapphim.model.dao.impl.ThongKeDoanhhThuDaoImpl;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ThongKeDoanhThuServiceImpl implements ThongKeDoanhThuService {

    private final ThongKeDoanhThuDAO dao;

    public ThongKeDoanhThuServiceImpl() {
        this(new ThongKeDoanhhThuDaoImpl());
    }

    public ThongKeDoanhThuServiceImpl(ThongKeDoanhThuDAO dao) {
        this.dao = dao;
    }

    @Override
    public BigDecimal tinhDoanhThuTheoNgay(LocalDate ngay) {
        return dao.doanhThuTheoNgay(ngay);
    }

    @Override
    public BigDecimal tinhDoanhThuTheoThang(int thang, int nam) {
        return dao.doanhThuTheoThang(thang, nam);
    }

    @Override
    public BigDecimal tinhDoanhThuTheoNam(int nam) {
        return dao.doanhThuTheoNam(nam);
    }

    @Override
    public BigDecimal tinhDoanhThuVeTheoNgay(LocalDate ngay) {
        return dao.doanhThuVeTheoNgay(ngay);
    }

    @Override
    public BigDecimal tinhDoanhThuComboTheoNgay(LocalDate ngay) {
        return dao.doanhThuComboTheoNgay(ngay);
    }

    @Override
    public BigDecimal tinhDoanhThuTheoPhim(String maPhim) {
        return dao.doanhThuTheoPhim(maPhim);
    }

    @Override
    public BigDecimal tinhDoanhThuTheoRap(String maRap) {
        return dao.doanhThuTheoRap(maRap);
    }

    @Override
    public BigDecimal tinhDoanhThuTheoPhong(String maPhong) {
        return dao.doanhThuTheoPhong(maPhong);
    }

    @Override
    public long demSoVeBanTheoNgay(LocalDate ngay) {
        return dao.soVeBanTheoNgay(ngay);
    }
}
