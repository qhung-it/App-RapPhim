package com.example.studentapp.apprapphim.model.entity;

import com.example.studentapp.apprapphim.model.Enum.ChucVu;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NhanVien")
public class NhanVien {

    @Id
    @Column(name = "maNV", length = 20)
    private String maNV;

    @Column(name = "hoTen", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "email", unique = true, nullable = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String matKhau;

    @Column(name = "soDienThoai", length = 20)
    private String soDienThoai;

    @Enumerated(EnumType.STRING)
    @Column(name = "chucVu", nullable = false, length = 30)
    private ChucVu chucVu = ChucVu.NhanVienBanVe;

    @OneToMany(mappedBy = "nhanVien")
    private List<DonDatVe> danhSachDonDatVe = new ArrayList<>();

    public NhanVien() {
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
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

    public ChucVu getChucVu() {
        return chucVu;
    }

    public void setChucVu(ChucVu chucVu) {
        this.chucVu = chucVu;
    }

    public List<DonDatVe> getDanhSachDonDatVe() {
        return danhSachDonDatVe;
    }

    public void setDanhSachDonDatVe(List<DonDatVe> danhSachDonDatVe) {
        this.danhSachDonDatVe = danhSachDonDatVe;
    }
}
