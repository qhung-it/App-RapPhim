package com.example.studentapp.apprapphim.controller.service.impl;

import com.example.studentapp.apprapphim.controller.service.interf.KhachHangService;
import com.example.studentapp.apprapphim.model.dao.DonDatVeDAO;
import com.example.studentapp.apprapphim.model.dao.KhachHangDAO;
import com.example.studentapp.apprapphim.model.dao.impl.DonDatVeDAOImpl;
import com.example.studentapp.apprapphim.model.dao.impl.KhachHangDAOImpl;
import com.example.studentapp.apprapphim.model.entity.DonDatVe;
import com.example.studentapp.apprapphim.model.entity.KhachHang;
import com.example.studentapp.apprapphim.model.util.MailUtilRender;
import com.example.studentapp.apprapphim.model.util.OTPInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class KhachHangServiceImpl implements KhachHangService {

    private final KhachHangDAO khachHangDAO;
    private final DonDatVeDAO donDatVeDAO;
    private final Map<String, OTPInfo> otpMap = new HashMap<>();

    public KhachHangServiceImpl() {
        this(new KhachHangDAOImpl(), new DonDatVeDAOImpl());
    }

    public KhachHangServiceImpl(KhachHangDAO khachHangDAO, DonDatVeDAO donDatVeDAO) {
        this.khachHangDAO = khachHangDAO;
        this.donDatVeDAO = donDatVeDAO;
    }

    @Override
    public boolean dangKy(KhachHang khachHang) {
        if (khachHang == null || khachHang.getMaKH() == null || khachHang.getMaKH().isBlank()
                || khachHang.getHoTen() == null || khachHang.getHoTen().isBlank()
                || khachHang.getEmail() == null || khachHang.getEmail().isBlank()
                || khachHang.getMatKhau() == null || khachHang.getMatKhau().isBlank()
                || khachHang.getLoaiKhachHang() == null) return false;
        if (khachHangDAO.findById(khachHang.getMaKH()) != null
                || khachHangDAO.findByEmail(khachHang.getEmail()) != null) return false;
        khachHangDAO.save(khachHang);
        return true;
    }

    @Override
    public KhachHang dangNhap(String email, String matKhau) {
        if (email == null || email.isBlank() || matKhau == null || matKhau.isBlank()) return null;
        KhachHang kh = khachHangDAO.findByEmail(email);
        return kh != null && matKhau.equals(kh.getMatKhau()) ? kh : null;
    }

    @Override
    public boolean doiMatKhau(String maKH, String matKhauCu, String matKhauMoi) {
        if (maKH == null || matKhauCu == null || matKhauMoi == null || matKhauMoi.isBlank()) return false;
        KhachHang kh = khachHangDAO.findById(maKH);
        if (kh == null || !matKhauCu.equals(kh.getMatKhau())) return false;
        kh.setMatKhau(matKhauMoi);
        khachHangDAO.save(kh);
        return true;
    }

    @Override
    public boolean quenMatKhau(String email) {
        // ==============================
        // 1. Kiểm tra email đầu vào
        // ==============================
        if (email == null || email.isBlank()) {
            return false;
        }

        // Xóa khoảng trắng
        email = email.trim();

        // Chuẩn hóa email
        String emailKey = email.toLowerCase();

        // ==============================
        // 2. Tìm khách hàng theo email
        // ==============================
        KhachHang khachHang =
                khachHangDAO.findByEmail(email);

        // Không tìm thấy khách hàng
        if (khachHang == null) {
            return false;
        }

        // ==============================
        // 3. Tạo OTP 6 số
        // ==============================
        String otp = String.format(
                "%06d",
                ThreadLocalRandom.current()
                        .nextInt(0, 1_000_000)
        );

        // ==============================
        // 4. Thiết lập thời gian hết hạn
        // ==============================
        long expireTime =
                System.currentTimeMillis()
                        + 5 * 60 * 1000;


        // ==============================
        // 5. Lưu OTP
        // ==============================
        otpMap.put(
                emailKey,
                new OTPInfo(otp, expireTime)
        );

        // ==============================
        // 6. Gửi email
        // ==============================
        try {
            MailUtilRender.sendMail(
                    khachHang.getEmail(),
                    khachHang.getHoTen(),
                    "Mã OTP đặt lại mật khẩu",
                    "Xin chào " + khachHang.getHoTen()
                            + ",\n\n"
                            + "Mã OTP để đặt lại mật khẩu của bạn là: "
                            + otp
                            + "\n\n"
                            + "Mã OTP có hiệu lực trong 5 phút."
                            + "\n\n"
                            + "Nếu bạn không yêu cầu đặt lại mật khẩu, "
                            + "vui lòng bỏ qua email này."
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // Gửi email thất bại
            // thì xóa OTP đã lưu
            otpMap.remove(emailKey);
            return false;
        }
    }

    @Override
    public boolean xacThucOTP(String email, String otp) {

        if (email == null || email.isBlank()
                || otp == null || otp.isBlank()) {
            return false;
        }

        email = email.trim().toLowerCase();
        otp = otp.trim();

        OTPInfo otpInfo = otpMap.get(email);

        // Không có OTP
        if (otpInfo == null) {
            return false;
        }

        // OTP hết hạn
        if (System.currentTimeMillis() > otpInfo.getExpireTime()) {
            otpMap.remove(email);
            return false;
        }

        // OTP không đúng
        if (!otpInfo.getOtp().equals(otp)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean datLaiMatKhau(String email, String matKhauMoi) {
        if (email == null || email.isBlank()
                || matKhauMoi == null
                || matKhauMoi.isBlank()) {
            return false;
        }

        email = email.trim().toLowerCase();

        // Kiểm tra OTP còn tồn tại
        OTPInfo otpInfo = otpMap.get(email);

        if (otpInfo == null) {
            return false;
        }

        // Kiểm tra OTP hết hạn
        if (System.currentTimeMillis()
                > otpInfo.getExpireTime()) {
            otpMap.remove(email);
            return false;
        }

        // Tìm khách hàng
        KhachHang khachHang =
                khachHangDAO.findByEmail(email);

        if (khachHang == null) {
            return false;
        }

        // Đổi mật khẩu
        khachHang.setMatKhau(matKhauMoi);

        // save() hiện tại của DAO đã xử lý merge()
        khachHangDAO.save(khachHang);

        // OTP chỉ được dùng một lần
        otpMap.remove(email);
        return true;
    }

    @Override
    public KhachHang timTheoMa(String maKH) {
        return khachHangDAO.findById(maKH);
    }

    @Override
    public KhachHang timTheoEmail(String email) {
        return khachHangDAO.findByEmail(email);
    }

    @Override
    public List<DonDatVe> xemLichSuDatVe(String maKH) {
        return donDatVeDAO.findByKhachHang(maKH);
    }

    @Override
    public List<DonDatVe> xemDonDatVe(String maKH) {
        return donDatVeDAO.findByKhachHang(maKH);
    }
}
