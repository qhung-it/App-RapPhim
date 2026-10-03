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

/**
 * Service thanh toán. Các thay đổi ThanhToan + DonDatVe được commit atomically.
 */
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
            if (don == null || don.getTrangThai() != TrangThaiDon.ChoThanhToan
                    || soTien == null || soTien.signum() <= 0 || phuongThuc == null) return null;

            ThanhToan existing = thanhToanDAO.findByDonDatVe(maDon);
            if (existing != null && existing.getTrangThai() != TrangThaiThanhToan.ThatBai) {
                return existing;
            }

            BigDecimal expected = don.tinhTongTienSauGiam();
            if (soTien.compareTo(expected) != 0) return null;

            ThanhToan tt = new ThanhToan();
            tt.setMaTT("TT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            tt.setThoiGian(LocalDateTime.now());
            tt.setSoTien(soTien);
            tt.setPhuongThuc(phuongThuc);
            tt.setTrangThai(TrangThaiThanhToan.DangXuLy);
            tt.setDonDatVe(don);

            return thanhToanDAO.save(tt);
        });
    }

    @Override
    public boolean thanhToan(String maTT) {
        return JpaDaoSupport.executeInTransaction(em -> {
            ThanhToan tt = thanhToanDAO.findById(maTT);
            if (tt == null || tt.getTrangThai() != TrangThaiThanhToan.DangXuLy) return false;

            DonDatVe don = tt.getDonDatVe();
            if (don == null || don.getTrangThai() == TrangThaiDon.DaHuy) return false;

            // Luôn kiểm tra lại số tiền trước khi ghi nhận thành công.
            BigDecimal expected = don.tinhTongTienSauGiam();
            if (tt.getSoTien() == null || tt.getSoTien().compareTo(expected) != 0) return false;

            tt.setTrangThai(TrangThaiThanhToan.ThanhCong);
            tt.setThoiGian(LocalDateTime.now());
            thanhToanDAO.save(tt);

            don.setThanhToan(tt);
            don.setTrangThai(TrangThaiDon.DaThanhToan);
            donDatVeDAO.save(don);
            return true;
        });
    }

    @Override
    public boolean huyThanhToan(String maTT) {
        return JpaDaoSupport.executeInTransaction(em -> {
            ThanhToan tt = thanhToanDAO.findById(maTT);
            if (tt == null || tt.getTrangThai() == TrangThaiThanhToan.ThanhCong) return false;

            tt.setTrangThai(TrangThaiThanhToan.ThatBai);
            thanhToanDAO.save(tt);

            // Không null don.thanhToan ở đây vì quan hệ OneToOne có orphanRemoval.
            // Giữ giao dịch thất bại để bảo toàn lịch sử; lần thanh toán sau sẽ tạo
            // một giao dịch mới và gán lại cho đơn khi thanh toán thành công.
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
