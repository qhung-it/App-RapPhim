package com.example.studentapp.apprapphim.model.dao.interf;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ThongKeDoanhThuDAO {

    BigDecimal doanhThuTheoNgay(LocalDate ngay);

    BigDecimal doanhThuTheoThang(int thang, int nam);

    BigDecimal doanhThuTheoNam(int nam);

    BigDecimal doanhThuVeTheoNgay(LocalDate ngay);

    BigDecimal doanhThuComboTheoNgay(LocalDate ngay);

    BigDecimal doanhThuTheoPhim(String maPhim);

    BigDecimal doanhThuTheoRap(String maRap);

    BigDecimal doanhThuTheoPhong(String maPhong);

    long soVeBanTheoNgay(LocalDate ngay);
}