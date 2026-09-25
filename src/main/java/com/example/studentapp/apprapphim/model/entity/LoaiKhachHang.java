package com.example.studentapp.apprapphim.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LoaiKhachHang")
public class LoaiKhachHang {
    @Id
    @Column(name = "maLoai", length = 20)
    private String maLoai;

    @Column(name = "tenLoai", nullable = false, length = 100)
    private String tenLoai;

    @Column(name = "moTa", length = 255)
    private String moTa;

    @Column(name = "mucUuDai", precision = 12, scale = 2)
    private BigDecimal mucUuDai;

    @OneToMany(mappedBy = "loaiKhachHang")
    private List<KhachHang> danhSachKhachHang = new ArrayList<>();

    public LoaiKhachHang() {
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public BigDecimal getMucUuDai() {
        return mucUuDai;
    }

    public void setMucUuDai(BigDecimal mucUuDai) {
        this.mucUuDai = mucUuDai;
    }

    public List<KhachHang> getDanhSachKhachHang() {
        return danhSachKhachHang;
    }

    public void setDanhSachKhachHang(List<KhachHang> danhSachKhachHang) {
        this.danhSachKhachHang = danhSachKhachHang;
    }
}
