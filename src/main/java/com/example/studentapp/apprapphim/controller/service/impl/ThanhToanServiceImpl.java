package com.example.studentapp.apprapphim.controller.service.impl;

import com.example.studentapp.apprapphim.controller.service.interf.ThanhToanService;
import com.example.studentapp.apprapphim.model.Enum.PhuongThuc;
import com.example.studentapp.apprapphim.model.Enum.TrangThaiDon;
import com.example.studentapp.apprapphim.model.Enum.TrangThaiThanhToan;
import com.example.studentapp.apprapphim.model.dao.DonDatVeDAO;
import com.example.studentapp.apprapphim.model.dao.ThanhToanDAO;
import com.example.studentapp.apprapphim.model.dao.impl.DonDatVeDAOImpl;
import com.example.studentapp.apprapphim.model.dao.impl.ThanhToanDAOImpl;
import com.example.studentapp.apprapphim.model.entity.DonDatVe;
import com.example.studentapp.apprapphim.model.entity.ThanhToan;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ThanhToanServiceImpl implements ThanhToanService {

    private final ThanhToanDAO thanhToanDAO;
    private final DonDatVeDAO donDatVeDAO;

    public ThanhToanServiceImpl() {
        this(new ThanhToanDAOImpl(), new DonDatVeDAOImpl());
    }

    public ThanhToanServiceImpl(ThanhToanDAO thanhToanDAO, DonDatVeDAO donDatVeDAO) {
        this.thanhToanDAO = thanhToanDAO;
        this.donDatVeDAO = donDatVeDAO;
    }

    @Override
    public ThanhToan taoThanhToan(String maDon, BigDecimal soTien, PhuongThuc phuongThuc) {
        return JpaDaoSupport.executeInTransaction(em -> {
            DonDatVe don = donDatVeDAO.findById(maDon);

            if (don == null
                    || don.getTrangThai() != TrangThaiDon.ChoThanhToan
                    || soTien == null
                    || soTien.signum() <= 0
                    || phuongThuc == null) {
                return null;
            }

            BigDecimal expected = don.tinhTongTienSauGiam();
            if (expected.signum() < 0 || soTien.compareTo(expected) != 0) {
                return null;
            }

            /*
             * Nếu giao dịch thất bại trước đó thì dùng lại chính bản ghi đó.
             * Như vậy một đơn không tạo hàng loạt ThanhToan.
             */
            ThanhToan tt = thanhToanDAO.findByDonDatVe(maDon);

            if (tt != null && tt.getTrangThai() == TrangThaiThanhToan.ThanhCong) {
                return tt;
            }

            if (tt == null) {
                tt = new ThanhToan();
                tt.setMaTT("TT-" + UUID.randomUUID()
                        .toString().substring(0, 8).toUpperCase());
                tt.setDonDatVe(don);
            }

            tt.setThoiGian(LocalDateTime.now());
            tt.setSoTien(soTien);
            tt.setPhuongThuc(phuongThuc);
            tt.setTrangThai(TrangThaiThanhToan.DangXuLy);

            // DonDatVe là phía sở hữu FK (maTT), nên phải cập nhật phía này.
            don.setThanhToan(tt);
            tt.setDonDatVe(don);

            thanhToanDAO.save(tt);
            donDatVeDAO.save(don);

            return tt;
        });
    }

    @Override
    public boolean thanhToan(String maTT) {
        return JpaDaoSupport.executeInTransaction(em -> {
            ThanhToan tt = thanhToanDAO.findById(maTT);

            if (tt == null || tt.getTrangThai() != TrangThaiThanhToan.DangXuLy) {
                return false;
            }

            DonDatVe don = tt.getDonDatVe();
            if (don == null || don.getTrangThai() == TrangThaiDon.DaHuy) {
                return false;
            }

            BigDecimal expected = don.tinhTongTienSauGiam();
            if (tt.getSoTien() == null
                    || expected.signum() < 0
                    || tt.getSoTien().compareTo(expected) != 0) {
                return false;
            }

            tt.setTrangThai(TrangThaiThanhToan.ThanhCong);
            tt.setThoiGian(LocalDateTime.now());
            tt.setDonDatVe(don);

            // Cập nhật cả hai phía của quan hệ OneToOne.
            don.setThanhToan(tt);
            don.setTrangThai(TrangThaiDon.DaThanhToan);

            thanhToanDAO.save(tt);
            donDatVeDAO.save(don);
            return true;
        });
    }

    @Override
    public boolean huyThanhToan(String maTT) {
        return JpaDaoSupport.executeInTransaction(em -> {
            ThanhToan tt = thanhToanDAO.findById(maTT);

            if (tt == null || tt.getTrangThai() == TrangThaiThanhToan.ThanhCong) {
                return false;
            }

            tt.setTrangThai(TrangThaiThanhToan.ThatBai);
            thanhToanDAO.save(tt);
            return true;
        });
    }

    @Override
    public boolean kiemTraThanhToan(String maTT) {
        ThanhToan tt = thanhToanDAO.findById(maTT);
        return tt != null && tt.getTrangThai() == TrangThaiThanhToan.ThanhCong;
    }

    @Override
    public ThanhToan timTheoMa(String maTT) {
        return thanhToanDAO.findById(maTT);
    }

    @Override
    public ThanhToan timTheoDon(String maDon) {
        return thanhToanDAO.findByDonDatVe(maDon);
    }
}
