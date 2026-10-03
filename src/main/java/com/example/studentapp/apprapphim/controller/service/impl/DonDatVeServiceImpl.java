package com.example.studentapp.apprapphim.controller.service.impl;

import com.example.studentapp.apprapphim.controller.service.interf.DonDatVeService;
import com.example.studentapp.apprapphim.model.Enum.TrangThaiDon;
import com.example.studentapp.apprapphim.model.Enum.TrangThaiThanhToan;
import com.example.studentapp.apprapphim.model.Enum.TrangThaiVe;
import com.example.studentapp.apprapphim.model.dao.*;
import com.example.studentapp.apprapphim.model.dao.impl.*;
import com.example.studentapp.apprapphim.model.entity.*;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import jakarta.persistence.PersistenceException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Business logic cho đơn đặt vé.
 *
 * Các nghiệp vụ tác động đồng thời đến nhiều DAO đều mở transaction ở Service.
 * DAO được gọi bên trong sẽ dùng chung EntityManager của transaction đó.
 */
public class DonDatVeServiceImpl implements DonDatVeService {

    private final DonDatVeDAO donDAO;
    private final KhachHangDAO khachHangDAO;
    private final NhanVienDAO nhanVienDAO;
    private final VeDAO veDAO;
    private final ComboDAO comboDAO;
    private final KhuyenMaiDAO khuyenMaiDAO;
    private final ThanhToanDAO thanhToanDAO;

    public DonDatVeServiceImpl() {
        this(new DonDatVeDAOImpl(), new KhachHangDAOImpl(), new NhanVienDAOImpl(),
                new VeDAOImpl(), new ComboDAOImpl(), new KhuyenMaiDAOImpl(),
                new ThanhToanDAOImpl());
    }

    public DonDatVeServiceImpl(DonDatVeDAO donDAO, KhachHangDAO khachHangDAO,
                               NhanVienDAO nhanVienDAO, VeDAO veDAO,
                               ComboDAO comboDAO, KhuyenMaiDAO khuyenMaiDAO) {
        this(donDAO, khachHangDAO, nhanVienDAO, veDAO, comboDAO,
                khuyenMaiDAO, new ThanhToanDAOImpl());
    }

    public DonDatVeServiceImpl(DonDatVeDAO donDAO, KhachHangDAO khachHangDAO,
                               NhanVienDAO nhanVienDAO, VeDAO veDAO,
                               ComboDAO comboDAO, KhuyenMaiDAO khuyenMaiDAO,
                               ThanhToanDAO thanhToanDAO) {
        this.donDAO = donDAO;
        this.khachHangDAO = khachHangDAO;
        this.nhanVienDAO = nhanVienDAO;
        this.veDAO = veDAO;
        this.comboDAO = comboDAO;
        this.khuyenMaiDAO = khuyenMaiDAO;
        this.thanhToanDAO = thanhToanDAO;
    }

    @Override
    public DonDatVe taoDon(String maKH, String maNV) {
        return JpaDaoSupport.executeInTransaction(em -> {
            KhachHang kh = khachHangDAO.findById(maKH);
            if (kh == null) return null;

            NhanVien nv = maNV == null || maNV.isBlank()
                    ? null : nhanVienDAO.findById(maNV);

            DonDatVe don = new DonDatVe();
            don.setMaDon("DON-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            don.setNgayDat(LocalDateTime.now());
            don.setTongTien(BigDecimal.ZERO);
            don.setTienGiam(BigDecimal.ZERO);
            don.setTrangThai(TrangThaiDon.ChoThanhToan);
            don.setKhachHang(kh);
            don.setNhanVien(nv);

            return donDAO.save(don);
        });
    }

    @Override
    public DonDatVe timTheoMa(String maDon) {
        return donDAO.findById(maDon);
    }

    @Override
    public List<DonDatVe> layDanhSachDon() {
        return donDAO.findAll();
    }

    @Override
    public List<DonDatVe> layDonTheoKhachHang(String maKH) {
        return donDAO.findByKhachHang(maKH);
    }

    @Override
    public boolean themVe(String maDon, Ve ve) {
        if (ve == null || ve.getSuatChieu() == null || ve.getGhe() == null) return false;

        try {
            return JpaDaoSupport.executeInTransaction(em -> {
                DonDatVe don = donDAO.findById(maDon);
                if (don == null || don.getTrangThai() != TrangThaiDon.ChoThanhToan) return false;

                if (ve.getMaVe() == null || ve.getMaVe().isBlank()) {
                    ve.setMaVe("VE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                } else if (veDAO.findById(ve.getMaVe()) != null) {
                    return false;
                }

                // Kiểm tra trước khi insert. Unique constraint trên Ve(maSuat, maGhe)
                // vẫn là lớp bảo vệ cuối cùng khi có hai request đồng thời.
                if (veDAO.existsBySuatChieuAndGhe(
                        ve.getSuatChieu().getMaSuat(), ve.getGhe().getMaGhe())) {
                    return false;
                }

                ve.setDonDatVe(don);
                if (!don.themVe(ve)) return false;

                don.tinhTongTien();
                donDAO.save(don);
                return true;
            });
        } catch (PersistenceException ex) {
            // Ví dụ request khác vừa chiếm cùng ghế và DB unique constraint phát hiện.
            return false;
        }
    }

    @Override
    public boolean themNhieuVe(String maDon, List<Ve> danhSachVe) {
        if (danhSachVe == null || danhSachVe.isEmpty()) return false;

        try {
            return JpaDaoSupport.executeInTransaction(em -> {
                DonDatVe don = donDAO.findById(maDon);
                if (don == null || don.getTrangThai() != TrangThaiDon.ChoThanhToan) return false;

                Set<String> gheDaChon = new HashSet<>();
                Set<String> maVeDaChon = new HashSet<>();

                for (Ve ve : danhSachVe) {
                    if (ve == null || ve.getSuatChieu() == null || ve.getGhe() == null) return false;

                    String key = ve.getSuatChieu().getMaSuat() + "|" + ve.getGhe().getMaGhe();
                    if (!gheDaChon.add(key)
                            || veDAO.existsBySuatChieuAndGhe(
                            ve.getSuatChieu().getMaSuat(), ve.getGhe().getMaGhe())) {
                        return false;
                    }

                    if (ve.getMaVe() == null || ve.getMaVe().isBlank()) {
                        ve.setMaVe("VE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                    }
                    if (!maVeDaChon.add(ve.getMaVe()) || veDAO.findById(ve.getMaVe()) != null) {
                        return false;
                    }
                }

                for (Ve ve : danhSachVe) {
                    if (!don.themVe(ve)) return false;
                }

                don.tinhTongTien();
                donDAO.save(don);
                return true;
            });
        } catch (PersistenceException ex) {
            return false;
        }
    }

    @Override
    public boolean xoaVe(String maDon, String maVe) {
        return JpaDaoSupport.executeInTransaction(em -> {
            DonDatVe don = donDAO.findById(maDon);
            if (don == null || don.getTrangThai() != TrangThaiDon.ChoThanhToan) return false;

            Ve target = don.getDanhSachVe().stream()
                    .filter(v -> maVe != null && maVe.equals(v.getMaVe()))
                    .findFirst().orElse(null);
            if (target == null) return false;

            don.xoaVe(target);
            don.tinhTongTien();
            donDAO.save(don);
            return true;
        });
    }

    @Override
    public boolean themCombo(String maDon, String maCombo, int soLuong) {
        if (soLuong <= 0) return false;

        return JpaDaoSupport.executeInTransaction(em -> {
            DonDatVe don = donDAO.findById(maDon);
            Combo combo = comboDAO.findById(maCombo);
            if (don == null || combo == null || don.getTrangThai() != TrangThaiDon.ChoThanhToan) return false;

            ChiTietCombo detail = don.getDanhSachCombo().stream()
                    .filter(c -> c.getCombo() != null && maCombo.equals(c.getCombo().getMaCombo()))
                    .findFirst().orElse(null);

            if (detail == null) {
                detail = new ChiTietCombo();
                detail.setCombo(combo);
                detail.setDonGia(combo.getGia());
                detail.setSoLuong(soLuong);
                detail.tinhThanhTien();
                if (!don.themCombo(detail)) return false;
            } else {
                detail.setSoLuong(detail.getSoLuong() + soLuong);
                detail.setDonGia(combo.getGia());
                detail.tinhThanhTien();
            }

            don.tinhTongTien();
            donDAO.save(don);
            return true;
        });
    }

    @Override
    public boolean capNhatSoLuongCombo(String maDon, String maCombo, int soLuong) {
        if (soLuong <= 0) return false;

        return JpaDaoSupport.executeInTransaction(em -> {
            DonDatVe don = donDAO.findById(maDon);
            if (don == null || don.getTrangThai() != TrangThaiDon.ChoThanhToan) return false;

            ChiTietCombo detail = don.getDanhSachCombo().stream()
                    .filter(c -> c.getCombo() != null && maCombo.equals(c.getCombo().getMaCombo()))
                    .findFirst().orElse(null);
            Combo combo = comboDAO.findById(maCombo);
            if (detail == null || combo == null) return false;

            detail.setSoLuong(soLuong);
            detail.setDonGia(combo.getGia());
            detail.tinhThanhTien();
            don.tinhTongTien();
            donDAO.save(don);
            return true;
        });
    }

    @Override
    public boolean xoaCombo(String maDon, String maCombo) {
        return JpaDaoSupport.executeInTransaction(em -> {
            DonDatVe don = donDAO.findById(maDon);
            if (don == null || don.getTrangThai() != TrangThaiDon.ChoThanhToan) return false;

            ChiTietCombo detail = don.getDanhSachCombo().stream()
                    .filter(c -> c.getCombo() != null && maCombo != null
                            && maCombo.equals(c.getCombo().getMaCombo()))
                    .findFirst().orElse(null);
            if (detail == null) return false;

            don.getDanhSachCombo().remove(detail);
            detail.setDonDatVe(null);
            don.tinhTongTien();
            donDAO.save(don);
            return true;
        });
    }

    @Override
    public BigDecimal tinhTongTien(String maDon) {
        DonDatVe don = donDAO.findById(maDon);
        return don == null ? BigDecimal.ZERO : don.tinhTongTienSauGiam();
    }

    @Override
    public KhuyenMai xacDinhKhuyenMai(String maDon) {
        DonDatVe don = donDAO.findById(maDon);
        if (don == null || don.getNgayDat() == null) return null;

        return khuyenMaiDAO.findDangApDung(don.getNgayDat().toLocalDate()).stream()
                .max((a, b) -> a.tinhTienGiam(don.tinhTongTien())
                        .compareTo(b.tinhTienGiam(don.tinhTongTien())))
                .orElse(null);
    }

    @Override
    public boolean apDungKhuyenMai(String maDon, String maKM) {
        return JpaDaoSupport.executeInTransaction(em -> {
            DonDatVe don = donDAO.findById(maDon);
            KhuyenMai km = khuyenMaiDAO.findById(maKM);
            if (don == null || km == null || don.getTrangThai() != TrangThaiDon.ChoThanhToan) return false;

            if (don.getNgayDat() == null
                    || khuyenMaiDAO.findDangApDung(don.getNgayDat().toLocalDate()).stream()
                    .noneMatch(k -> k.getMaKM().equals(km.getMaKM()))) {
                return false;
            }

            don.tinhTongTien();
            don.setKhuyenMai(km);
            don.setTienGiam(km.tinhTienGiam(don.getTongTien()));
            donDAO.save(don);
            return true;
        });
    }

    @Override
    public boolean huyDon(String maDon) {
        return JpaDaoSupport.executeInTransaction(em -> {
            DonDatVe don = donDAO.findById(maDon);
            if (don == null || !don.huyDon()) return false;

            for (Ve ve : don.getDanhSachVe()) {
                if (ve.getTrangThai() == TrangThaiVe.ChuaSuDung) {
                    ve.setTrangThai(TrangThaiVe.DaHuy);
                }
            }

            // Nếu đang có giao dịch thanh toán chờ xử lý thì chuyển sang thất bại
            // trong cùng transaction với việc hủy đơn.
            ThanhToan tt = thanhToanDAO.findByDonDatVe(maDon);
            if (tt != null && tt.getTrangThai() == TrangThaiThanhToan.DangXuLy) {
                tt.setTrangThai(TrangThaiThanhToan.ThatBai);
                thanhToanDAO.save(tt);
            }

            donDAO.save(don);
            return true;
        });
    }

    @Override
    public boolean xacNhanDon(String maDon) {
        return JpaDaoSupport.executeInTransaction(em -> {
            DonDatVe don = donDAO.findById(maDon);
            if (don == null || don.getTrangThai() == TrangThaiDon.DaHuy) return false;
            if (don.getDanhSachVe().isEmpty() && don.getDanhSachCombo().isEmpty()) return false;

            // Không đánh dấu đơn đã thanh toán nếu chưa có giao dịch thành công.
            ThanhToan tt = don.getThanhToan();
            if (tt == null || tt.getTrangThai() != TrangThaiThanhToan.ThanhCong) return false;

            // Cho phép gọi lại method sau khi ThanhToanService đã cập nhật trạng thái.
            if (don.getTrangThai() == TrangThaiDon.DaThanhToan) return true;
            if (don.getTrangThai() != TrangThaiDon.ChoThanhToan) return false;

            don.tinhTongTien();
            don.setTrangThai(TrangThaiDon.DaThanhToan);
            donDAO.save(don);
            return true;
        });
    }
}
