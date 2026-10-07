package com.example.studentapp.apprapphim.model.dao.interf;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiKhuyenMai;
import com.example.studentapp.apprapphim.model.entity.KhuyenMai;

import java.time.LocalDate;
import java.util.List;

public interface KhuyenMaiDAO {

    KhuyenMai findById(String maKM);

    List<KhuyenMai> findAll();

    List<KhuyenMai> findByTrangThai(TrangThaiKhuyenMai trangThai);

    List<KhuyenMai> findDangApDung(LocalDate ngay);

    KhuyenMai save(KhuyenMai khuyenMai);

    void delete(KhuyenMai khuyenMai);
}