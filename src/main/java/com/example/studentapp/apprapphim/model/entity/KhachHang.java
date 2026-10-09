package com.example.studentapp.apprapphim.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "KhachHang")
public class KhachHang {

    @Id
    @Column(name = "maKH", length = 20)
    private String maKH;

    @Column(name = "hoTen", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String matKhau;

    @Column(name = "soDienThoai", length = 20)
    private String soDienThoai;

    @Column(name = "ngaySinh")
    private LocalDate ngaySinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maLoai", nullable = false)
    private LoaiKhachHang loaiKhachHang;

    @OneToMany(mappedBy = "khachHang")
    private List<DonDatVe> danhSachDonDatVe = new ArrayList<>();

    public KhachHang() {
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public LoaiKhachHang getLoaiKhachHang() {
        return loaiKhachHang;
    }

    public void setLoaiKhachHang(LoaiKhachHang loaiKhachHang) {
        this.loaiKhachHang = loaiKhachHang;
    }

    public List<DonDatVe> getDanhSachDonDatVe() {
        return danhSachDonDatVe;
    }

    public void setDanhSachDonDatVe(List<DonDatVe> danhSachDonDatVe) {
        this.danhSachDonDatVe = danhSachDonDatVe;
    }
}
