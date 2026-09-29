package com.example.studentapp.apprapphim.model.entity;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiVe;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "Ve",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_Ve_SuatChieu_Ghe",
                        columnNames = {"maSuat", "maGhe"}
                )
        }
)
public class Ve {

    @Id
    @Column(name = "maVe", length = 20)
    private String maVe;

    @Column(name = "maVeDienTu", unique = true, length = 100)
    private String maVeDienTu;

    @Column(name = "giaVe", precision = 12, scale = 2, nullable = false)
    private BigDecimal giaVe;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 30)
    private TrangThaiVe trangThai = TrangThaiVe.ChuaSuDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDon", nullable = false)
    private DonDatVe donDatVe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maSuat", nullable = false)
    private SuatChieu suatChieu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maGhe", nullable = false)
    private Ghe ghe;

    public Ve() {
    }

    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public String getMaVeDienTu() {
        return maVeDienTu;
    }

    public void setMaVeDienTu(String maVeDienTu) {
        this.maVeDienTu = maVeDienTu;
    }

    public BigDecimal getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(BigDecimal giaVe) {
        this.giaVe = giaVe;
    }

    public TrangThaiVe getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiVe trangThai) {
        this.trangThai = trangThai;
    }

    public DonDatVe getDonDatVe() {
        return donDatVe;
    }

    public void setDonDatVe(DonDatVe donDatVe) {
        this.donDatVe = donDatVe;
    }

    public SuatChieu getSuatChieu() {
        return suatChieu;
    }

    public void setSuatChieu(SuatChieu suatChieu) {
        this.suatChieu = suatChieu;
    }

    public Ghe getGhe() {
        return ghe;
    }

    public void setGhe(Ghe ghe) {
        this.ghe = ghe;
    }

    public boolean kiemTraVe() {
        return trangThai == TrangThaiVe.ChuaSuDung;
    }

    public boolean suDungVe() {
        if (trangThai != TrangThaiVe.ChuaSuDung) {
            return false;
        }

        trangThai = TrangThaiVe.DaSuDung;
        return true;
    }
}
