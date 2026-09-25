package com.example.studentapp.apprapphim.model.entity;

import com.example.studentapp.apprapphim.model.Enum.PhuongThuc;
import com.example.studentapp.apprapphim.model.Enum.TrangThaiThanhToan;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ThanhToan")
public class ThanhToan {

    @Id
    @Column(name = "maTT", length = 20)
    private String maTT;

    @Column(name = "thoiGian", nullable = false)
    private LocalDateTime thoiGian;

    @Column(name = "soTien", precision = 12, scale = 2, nullable = false)
    private BigDecimal soTien;

    @Enumerated(EnumType.STRING)
    @Column(name = "phuongThuc", nullable = false, length = 30)
    private PhuongThuc phuongThuc = PhuongThuc.TienMat;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 30)
    private TrangThaiThanhToan trangThai = TrangThaiThanhToan.DangXuLy;

    @OneToOne(mappedBy = "thanhToan")
    private DonDatVe donDatVe;

    public ThanhToan() {
    }

    public String getMaTT() {
        return maTT;
    }

    public void setMaTT(String maTT) {
        this.maTT = maTT;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }

    public BigDecimal getSoTien() {
        return soTien;
    }

    public void setSoTien(BigDecimal soTien) {
        this.soTien = soTien;
    }

    public PhuongThuc getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(PhuongThuc phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public TrangThaiThanhToan getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiThanhToan trangThai) {
        this.trangThai = trangThai;
    }

    public DonDatVe getDonDatVe() {
        return donDatVe;
    }

    public void setDonDatVe(DonDatVe donDatVe) {
        this.donDatVe = donDatVe;
    }
}
