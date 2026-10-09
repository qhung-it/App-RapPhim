package com.example.studentapp.apprapphim.controller.service.interf;

import com.example.studentapp.apprapphim.model.Enum.PhuongThuc;
import com.example.studentapp.apprapphim.model.entity.ThanhToan;

import java.math.BigDecimal;

public interface ThanhToanService {

    ThanhToan taoThanhToan(String maDon, BigDecimal soTien, PhuongThuc phuongThuc);

    boolean thanhToan(String maTT);

    boolean huyThanhToan(String maTT);

    boolean kiemTraThanhToan(String maTT);

    ThanhToan timTheoMa(String maTT);

    ThanhToan timTheoDon(String maDon);
}