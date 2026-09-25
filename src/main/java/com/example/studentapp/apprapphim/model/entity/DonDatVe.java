package com.example.studentapp.apprapphim.model.entity;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiDon;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DonDatVe")
public class DonDatVe {

    @Id
    @Column(name = "maDon", length = 20)
    private String maDon;

    @Column(name = "ngayDat", nullable = false)
    private LocalDateTime ngayDat;

    @Column(name = "tongTien", precision = 12, scale = 2, nullable = false)
    private BigDecimal tongTien;

    @Column(name = "tienGiam", precision = 12, scale = 2, nullable = false)
    private BigDecimal tienGiam;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 30)
    private TrangThaiDon trangThai = TrangThaiDon.ChoThanhToan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKH", nullable = false)
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNV")
    private NhanVien nhanVien;

    @OneToMany(
            mappedBy = "donDatVe",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Ve> danhSachVe = new ArrayList<>();

    @OneToMany(
            mappedBy = "donDatVe",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ChiTietCombo> danhSachCombo = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKM")
    private KhuyenMai khuyenMai;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "maTT", unique = true)
    private ThanhToan thanhToan;

    public DonDatVe() {
    }

    public String getMaDon() {
        return maDon;
    }

    public void setMaDon(String maDon) {
        this.maDon = maDon;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDateTime ngayDat) {
        this.ngayDat = ngayDat;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public BigDecimal getTienGiam() {
        return tienGiam;
    }

    public void setTienGiam(BigDecimal tienGiam) {
        this.tienGiam = tienGiam;
    }

    public TrangThaiDon getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiDon trangThai) {
        this.trangThai = trangThai;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public List<Ve> getDanhSachVe() {
        return danhSachVe;
    }

    public void setDanhSachVe(List<Ve> danhSachVe) {
        this.danhSachVe = danhSachVe;
    }

    public List<ChiTietCombo> getDanhSachCombo() {
        return danhSachCombo;
    }

    public void setDanhSachCombo(List<ChiTietCombo> danhSachCombo) {
        this.danhSachCombo = danhSachCombo;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public ThanhToan getThanhToan() {
        return thanhToan;
    }

    public void setThanhToan(ThanhToan thanhToan) {
        this.thanhToan = thanhToan;
    }
}
