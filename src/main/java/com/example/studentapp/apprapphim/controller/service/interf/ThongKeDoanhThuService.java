package com.example.studentapp.apprapphim.controller.service.interf;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ThongKeDoanhThuService {

    BigDecimal tinhDoanhThuTheoNgay(LocalDate ngay);

    BigDecimal tinhDoanhThuTheoThang(int thang, int nam);

    BigDecimal tinhDoanhThuTheoNam(int nam);

    BigDecimal tinhDoanhThuVeTheoNgay(LocalDate ngay);

    BigDecimal tinhDoanhThuComboTheoNgay(LocalDate ngay);

    BigDecimal tinhDoanhThuTheoPhim(String maPhim);

    BigDecimal tinhDoanhThuTheoRap(String maRap);

    BigDecimal tinhDoanhThuTheoPhong(String maPhong);

    long demSoVeBanTheoNgay(LocalDate ngay);
}