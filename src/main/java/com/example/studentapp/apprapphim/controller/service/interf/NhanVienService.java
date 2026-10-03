package com.example.studentapp.apprapphim.controller.service.interf;

import com.example.studentapp.apprapphim.model.Enum.ChucVu;
import com.example.studentapp.apprapphim.model.entity.NhanVien;
import java.util.List;

public interface NhanVienService {

    NhanVien dangNhap(String email, String matKhau);

    boolean themNhanVien(NhanVien nhanVien);

    boolean capNhatNhanVien(NhanVien nhanVien);

    boolean xoaNhanVien(String maNV);

    NhanVien timTheoMa(String maNV);

    List<NhanVien> layDanhSachNhanVien();

    boolean phanQuyen(
            String maNV,
            ChucVu chucVu
    );
}