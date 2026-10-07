package com.example.studentapp.apprapphim.model.dao.interf;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiDon;
import com.example.studentapp.apprapphim.model.entity.DonDatVe;

import java.util.List;

public interface DonDatVeDAO {

    DonDatVe findById(String maDon);

    List<DonDatVe> findAll();

    List<DonDatVe> findByKhachHang(String maKH);

    List<DonDatVe> findByNhanVien(String maNV);

    List<DonDatVe> findByTrangThai(TrangThaiDon trangThai);

    DonDatVe save(DonDatVe donDatVe);

    void delete(DonDatVe donDatVe);
}
