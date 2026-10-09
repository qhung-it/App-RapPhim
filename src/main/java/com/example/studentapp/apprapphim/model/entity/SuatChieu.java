package com.example.studentapp.apprapphim.model.entity;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiSuatChieu;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SuatChieu")
public class SuatChieu {

    @Id
    @Column(name = "maSuat", length = 20)
    private String maSuat;

    @Column(name = "ngayChieu", nullable = false)
    private LocalDate ngayChieu;

    @Column(name = "gioBatDau", nullable = false)
    private LocalTime gioBatDau;

    @Column(name = "gioKetThuc", nullable = false)
    private LocalTime gioKetThuc;

    @Column(name = "giaSuat", precision = 12, scale = 2, nullable = false)
    private BigDecimal giaSuat;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 30)
    private TrangThaiSuatChieu trangThai = TrangThaiSuatChieu.SapChieu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maPhim", nullable = false)
    private Phim phim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maPhong", nullable = false)
    private PhongChieu phongChieu;

    @OneToMany(mappedBy = "suatChieu")
    private List<Ve> danhSachVe = new ArrayList<>();

    public SuatChieu() {
    }

    public String getMaSuat() {
        return maSuat;
    }

    public void setMaSuat(String maSuat) {
        this.maSuat = maSuat;
    }

    public LocalDate getNgayChieu() {
        return ngayChieu;
    }

    public void setNgayChieu(LocalDate ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public LocalTime getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(LocalTime gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public LocalTime getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(LocalTime gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public BigDecimal getGiaSuat() {
        return giaSuat;
    }

    public void setGiaSuat(BigDecimal giaSuat) {
        this.giaSuat = giaSuat;
    }

    public TrangThaiSuatChieu getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiSuatChieu trangThai) {
        this.trangThai = trangThai;
    }

    public Phim getPhim() {
        return phim;
    }

    public void setPhim(Phim phim) {
        this.phim = phim;
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
