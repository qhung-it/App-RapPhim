package com.example.studentapp.apprapphim.model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Rap")
public class Rap {

    @Id
    @Column(name = "maRap", length = 20)
    private String maRap;

    @Column(name = "tenRap", nullable = false, length = 150)
    private String tenRap;

    @Column(name = "diaChi", length = 255)
    private String diaChi;

    @Column(name = "soDienThoai", length = 20)
    private String soDienThoai;

    @OneToMany(
            mappedBy = "rap",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PhongChieu> danhSachPhongChieu = new ArrayList<>();

    public Rap() {
    }

    public String getMaRap() {
        return maRap;
    }

    public void setMaRap(String maRap) {
        this.maRap = maRap;
    }

    public String getTenRap() {
        return tenRap;
    }

    public void setTenRap(String tenRap) {
        this.tenRap = tenRap;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public List<PhongChieu> getDanhSachPhongChieu() {
        return danhSachPhongChieu;
    }

    public void setDanhSachPhongChieu(List<PhongChieu> danhSachPhongChieu) {
        this.danhSachPhongChieu = danhSachPhongChieu;
    }
}