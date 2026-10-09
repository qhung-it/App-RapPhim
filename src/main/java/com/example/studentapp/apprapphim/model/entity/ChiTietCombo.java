package com.example.studentapp.apprapphim.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ChiTietCombo")
public class ChiTietCombo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "soLuong", nullable = false)
    private int soLuong;

    @Column(name = "donGia", nullable = false)
    private BigDecimal donGia;

    @Column(name = "thanhTien", precision = 12, scale = 2, nullable = false)
    private BigDecimal thanhTien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDon", nullable = false)
    private DonDatVe donDatVe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maCombo", nullable = false)
    private Combo combo;

    public ChiTietCombo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public BigDecimal getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien;
    }

    public DonDatVe getDonDatVe() {
        return donDatVe;
    }

    public void setDonDatVe(DonDatVe donDatVe) {
        this.donDatVe = donDatVe;
    }

    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }

    public BigDecimal tinhThanhTien() {
        thanhTien = donGia.multiply(BigDecimal.valueOf(soLuong));
        return thanhTien;
    }
}
