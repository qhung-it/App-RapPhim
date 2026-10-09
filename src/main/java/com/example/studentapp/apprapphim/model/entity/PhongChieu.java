package com.example.studentapp.apprapphim.model.entity;

import com.example.studentapp.apprapphim.model.Enum.LoaiPhong;
import com.example.studentapp.apprapphim.model.Enum.TrangThaiPhongChieu;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PhongChieu")
public class PhongChieu {

    @Id
    @Column(name = "maPhong", length = 20)
    private String maPhong;

    @Column(name = "tenPhong", nullable = false, length = 100)
    private String tenPhong;

    @Enumerated(EnumType.STRING)
    @Column(name = "loaiPhong", nullable = false, length = 50)
    private LoaiPhong loaiPhong = LoaiPhong.THUONG;

    @Column(name = "soLuongGhe", nullable = false)
    private int soLuongGhe;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 30)
    private TrangThaiPhongChieu trangThai = TrangThaiPhongChieu.HoatDong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maRap", nullable = false)
    private Rap rap;

    @OneToMany(
            mappedBy = "phongChieu",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Ghe> danhSachGhe = new ArrayList<>();

    @OneToMany(mappedBy = "phongChieu")
    private List<SuatChieu> danhSachSuatChieu = new ArrayList<>();

    public PhongChieu() {
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public LoaiPhong getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(LoaiPhong loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public int getSoLuongGhe() {
        return soLuongGhe;
    }

    public void setSoLuongGhe(int soLuongGhe) {
        this.soLuongGhe = soLuongGhe;
    }

    public TrangThaiPhongChieu getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiPhongChieu trangThai) {
        this.trangThai = trangThai;
    }

    public Rap getRap() {
        return rap;
    }

    public void setRap(Rap rap) {
        this.rap = rap;
    }

    public List<Ghe> getDanhSachGhe() {
        return danhSachGhe;
    }

    public void setDanhSachGhe(List<Ghe> danhSachGhe) {
        this.danhSachGhe = danhSachGhe;
    }

    public List<SuatChieu> getDanhSachSuatChieu() {
        return danhSachSuatChieu;
    }

    public void setDanhSachSuatChieu(List<SuatChieu> danhSachSuatChieu) {
        this.danhSachSuatChieu = danhSachSuatChieu;
    }
}
