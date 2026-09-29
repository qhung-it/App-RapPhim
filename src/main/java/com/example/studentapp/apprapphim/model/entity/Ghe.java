package com.example.studentapp.apprapphim.model.entity;

import com.example.studentapp.apprapphim.model.Enum.LoaiGhe;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Ghe")
public class  Ghe {

    @Id
    @Column(name = "maGhe", length = 20)
    private String maGhe;

    @Column(name = "soGhe", nullable = false)
    private int soGhe;

    @Column(name = "hangGhe", nullable = false, length = 10)
    private String hangGhe;

    @Enumerated(EnumType.STRING)
    @Column(name = "loaiGhe", nullable = false, length = 20)
    private LoaiGhe loaiGhe = LoaiGhe.Thuong;

    @Column(name = "gia", precision = 12, scale = 2, nullable = false)
    private BigDecimal gia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maPhong", nullable = false)
    private PhongChieu phongChieu;

    @OneToMany(mappedBy = "ghe")
    private List<Ve> danhSachVe = new ArrayList<>();

    public Ghe() {
    }

    public String getMaGhe() {
        return maGhe;
    }

    public void setMaGhe(String maGhe) {
        this.maGhe = maGhe;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    public String getHangGhe() {
        return hangGhe;
    }

    public void setHangGhe(String hangGhe) {
        this.hangGhe = hangGhe;
    }

    public LoaiGhe getLoaiGhe() {
        return loaiGhe;
    }

    public void setLoaiGhe(LoaiGhe loaiGhe) {
        this.loaiGhe = loaiGhe;
    }

    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
        this.gia = gia;
    }

    public PhongChieu getPhongChieu() {
        return phongChieu;
    }

    public void setPhongChieu(PhongChieu phongChieu) {
        this.phongChieu = phongChieu;
    }

    public List<Ve> getDanhSachVe() {
        return danhSachVe;
    }

    public void setDanhSachVe(List<Ve> danhSachVe) {
        this.danhSachVe = danhSachVe;
    }
}
