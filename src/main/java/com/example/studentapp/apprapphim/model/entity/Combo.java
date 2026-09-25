package com.example.studentapp.apprapphim.model.entity;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiCombo;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Combo")
public class Combo {

    @Id
    @Column(name = "maCombo", length = 20)
    private String maCombo;

    @Column(name = "tenCombo", nullable = false, length = 100)
    private String tenCombo;

    @Column(name = "moTa", length = 255)
    private String moTa;

    @Column(name = "gia", precision = 12, scale = 2, nullable = false)
    private BigDecimal gia;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 30)
    private TrangThaiCombo trangThai = TrangThaiCombo.DangBan;

    @OneToMany(mappedBy = "combo")
    private List<ChiTietCombo> danhSachChiTiet = new ArrayList<>();

    public Combo() {
    }

    public String getMaCombo() {
        return maCombo;
    }

    public void setMaCombo(String maCombo) {
        this.maCombo = maCombo;
    }

    public String getTenCombo() {
        return tenCombo;
    }

    public void setTenCombo(String tenCombo) {
        this.tenCombo = tenCombo;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
        this.gia = gia;
    }

    public TrangThaiCombo getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiCombo trangThai) {
        this.trangThai = trangThai;
    }

    public List<ChiTietCombo> getDanhSachChiTiet() {
        return danhSachChiTiet;
    }

    public void setDanhSachChiTiet(List<ChiTietCombo> danhSachChiTiet) {
        this.danhSachChiTiet = danhSachChiTiet;
    }
}
