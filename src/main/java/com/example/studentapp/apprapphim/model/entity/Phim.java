package com.example.studentapp.apprapphim.model.entity;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiPhim;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Phim")
public class Phim {

    @Id
    @Column(name = "maPhim", length = 20)
    private String maPhim;

    @Column(name = "tenPhim", nullable = false, length = 200)
    private String tenPhim;

    @Column(name = "theLoai", length = 100)
    private String theLoai;

    @Column(name = "thoiLuong")
    private int thoiLuong;

    @Column(name = "doTuoi")
    private int doTuoi;

    @Column(name = "moTa", columnDefinition = "NVARCHAR(MAX)")
    private String moTa;

    @Column(name = "poster", length = 500)
    private String poster;

    @Column(name = "trailer", length = 500)
    private String trailer;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 30)
    private TrangThaiPhim trangThai = TrangThaiPhim.DangChieu;

    @OneToMany(mappedBy = "phim")
    private List<SuatChieu> danhSachSuatChieu = new ArrayList<>();

    public Phim() {
    }

    public String getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(String maPhim) {
        this.maPhim = maPhim;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public int getDoTuoi() {
        return doTuoi;
    }

    public void setDoTuoi(int doTuoi) {
        this.doTuoi = doTuoi;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public TrangThaiPhim getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiPhim trangThai) {
        this.trangThai = trangThai;
    }

    public List<SuatChieu> getDanhSachSuatChieu() {
        return danhSachSuatChieu;
    }

    public void setDanhSachSuatChieu(List<SuatChieu> danhSachSuatChieu) {
        this.danhSachSuatChieu = danhSachSuatChieu;
    }
}
