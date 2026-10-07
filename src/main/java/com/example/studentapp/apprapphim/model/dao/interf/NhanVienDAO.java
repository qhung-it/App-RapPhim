package com.example.studentapp.apprapphim.model.dao.interf;

import com.example.studentapp.apprapphim.model.Enum.ChucVu;
import com.example.studentapp.apprapphim.model.entity.NhanVien;

import java.util.List;

public interface NhanVienDAO {

    NhanVien findById(String maNV);

    NhanVien findByEmail(String email);

    List<NhanVien> findAll();

    List<NhanVien> findByChucVu(
            ChucVu chucVu
    );

    NhanVien save(NhanVien nhanVien);

    void delete(NhanVien nhanVien);
}