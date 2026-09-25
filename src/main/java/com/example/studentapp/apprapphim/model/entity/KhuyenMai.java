package com.example.studentapp.apprapphim.model.entity;

import com.example.studentapp.apprapphim.model.Enum.LoaiGiam;
import com.example.studentapp.apprapphim.model.Enum.TrangThaiKhuyenMai;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "KhuyenMai")
public class KhuyenMai {

    @Id
    @Column(name = "maKM", length = 20)
    private String maKM;

    @Column(name = "tenKM", nullable = false, length = 150)
    private String tenKM;

    @Column(name = "moTa", length = 255)
    private String moTa;

    @Enumerated(EnumType.STRING)
    @Column(name = "loaiGiam", nullable = false, length = 30)
    private LoaiGiam loaiGiam = LoaiGiam.PhanTram;

    @Column(name = "giaTriGiam", precision = 12, scale = 2, nullable = false)
    private BigDecimal giaTriGiam;

    @Column(name = "ngayBatDau", nullable = false)
    private LocalDate ngayBatDau;

    @Column(name = "ngayKetThuc", nullable = false)
    private LocalDate ngayKetThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 30)
    private TrangThaiKhuyenMai trangThai = TrangThaiKhuyenMai.DangHoatDong;

    @OneToMany(mappedBy = "khuyenMai")
    private List<DonDatVe> danhSachDonDatVe = new ArrayList<>();

    public KhuyenMai() {
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public String getTenKM() {
        return tenKM;
    }

    public void setTenKM(String tenKM) {
        this.tenKM = tenKM;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public LoaiGiam getLoaiGiam() {
        return loaiGiam;
    }

    public void setLoaiGiam(LoaiGiam loaiGiam) {
        this.loaiGiam = loaiGiam;
    }

    public BigDecimal getGiaTriGiam() {
        return giaTriGiam;
    }

    public void setGiaTriGiam(BigDecimal giaTriGiam) {
        this.giaTriGiam = giaTriGiam;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public TrangThaiKhuyenMai getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiKhuyenMai trangThai) {
        this.trangThai = trangThai;
    }

    public List<DonDatVe> getDanhSachDonDatVe() {
        return danhSachDonDatVe;
    }

    public void setDanhSachDonDatVe(List<DonDatVe> danhSachDonDatVe) {
        this.danhSachDonDatVe = danhSachDonDatVe;
    }
}
