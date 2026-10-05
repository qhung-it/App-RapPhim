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

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class KhachHangServiceImpl implements KhachHangService {

    private static final int PBKDF2_ITERATIONS = 65_536;
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 256;

    private static final long OTP_VALID_MILLIS = 5 * 60 * 1000L;
    private static final int MAX_OTP_ATTEMPTS = 5;

    private final KhachHangDAO khachHangDAO;
    private final DonDatVeDAO donDatVeDAO;

    // Thread-safe để OTP không bị mất/corrupt khi có nhiều request.
    private final ConcurrentHashMap<String, OTPInfo> otpMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> otpAttempts = new ConcurrentHashMap<>();
    private final Set<String> verifiedOtp = ConcurrentHashMap.newKeySet();

    private final SecureRandom secureRandom = new SecureRandom();

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
                || khachHang.getLoaiKhachHang() == null) {
            return false;
        }

        if (khachHangDAO.findById(khachHang.getMaKH()) != null
                || khachHangDAO.findByEmail(khachHang.getEmail()) != null) {
            return false;
        }

        khachHang.setMatKhau(hashPassword(khachHang.getMatKhau()));
        khachHangDAO.save(khachHang);
        return true;
    }

    @Override
    public KhachHang dangNhap(String email, String matKhau) {
        if (email == null || email.isBlank() || matKhau == null || matKhau.isBlank()) {
            return null;
        }

        KhachHang kh = khachHangDAO.findByEmail(email);
        if (kh == null || kh.getMatKhau() == null) {
            return null;
        }

        // Hỗ trợ dữ liệu cũ đang lưu plaintext và tự chuyển sang hash sau lần đăng nhập đúng.
        if (kh.getMatKhau().startsWith("PBKDF2$")) {
            return verifyPassword(matKhau, kh.getMatKhau()) ? kh : null;
        }

        if (matKhau.equals(kh.getMatKhau())) {
            kh.setMatKhau(hashPassword(matKhau));
            khachHangDAO.save(kh);
            return kh;
        }

        return null;
    }

    @Override
    public boolean doiMatKhau(String maKH, String matKhauCu, String matKhauMoi) {
        if (maKH == null || maKH.isBlank()
                || matKhauCu == null || matKhauCu.isBlank()
                || matKhauMoi == null || matKhauMoi.isBlank()) {
            return false;
        }

        KhachHang kh = khachHangDAO.findById(maKH);
        if (kh == null || !verifyStoredPassword(matKhauCu, kh.getMatKhau())) {
            return false;
        }

        kh.setMatKhau(hashPassword(matKhauMoi));
        khachHangDAO.save(kh);
        return true;
    }

    @Override
    public boolean quenMatKhau(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        String emailKey = email.trim().toLowerCase();
        KhachHang khachHang = khachHangDAO.findByEmail(emailKey);
        if (khachHang == null) {
            return false;
        }

        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));
        long expireTime = System.currentTimeMillis() + OTP_VALID_MILLIS;

        otpMap.put(emailKey, new OTPInfo(otp, expireTime));
        otpAttempts.put(emailKey, 0);
        verifiedOtp.remove(emailKey);

        try {
            MailUtilRender.sendMail(
                    khachHang.getEmail(),
                    khachHang.getHoTen(),
                    "Mã OTP đặt lại mật khẩu",
                    "Xin chào " + khachHang.getHoTen() + ",\n\n"
                            + "Mã OTP để đặt lại mật khẩu của bạn là: " + otp + "\n\n"
                            + "Mã OTP có hiệu lực trong 5 phút.\n\n"
                            + "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này."
            );
            return true;
        } catch (Exception e) {
            otpMap.remove(emailKey);
            otpAttempts.remove(emailKey);
            verifiedOtp.remove(emailKey);
            return false;
        }
    }

    @Override
    public boolean xacThucOTP(String email, String otp) {
        if (email == null || email.isBlank() || otp == null || otp.isBlank()) {
            return false;
        }

        String emailKey = email.trim().toLowerCase();
        OTPInfo otpInfo = otpMap.get(emailKey);

        if (otpInfo == null) {
            return false;
        }

        if (System.currentTimeMillis() > otpInfo.getExpireTime()) {
            clearOtp(emailKey);
            return false;
        }

        int attempts = otpAttempts.merge(emailKey, 1, Integer::sum);
        if (attempts > MAX_OTP_ATTEMPTS) {
            clearOtp(emailKey);
            return false;
        }

        if (!otpInfo.getOtp().equals(otp.trim())) {
            return false;
        }

        verifiedOtp.add(emailKey);
        return true;
    }

    @Override
    public boolean datLaiMatKhau(String email, String otp, String matKhauMoi) {
        if (email == null || email.isBlank()
                || otp == null || otp.isBlank()
                || matKhauMoi == null || matKhauMoi.isBlank()) {
            return false;
        }

        String emailKey = email.trim().toLowerCase();
        OTPInfo otpInfo = otpMap.get(emailKey);

        if (otpInfo == null
                || System.currentTimeMillis() > otpInfo.getExpireTime()
                || !verifiedOtp.contains(emailKey)
                || !otpInfo.getOtp().equals(otp.trim())) {
            return false;
        }

        KhachHang khachHang = khachHangDAO.findByEmail(emailKey);
        if (khachHang == null) {
            return false;
        }

        khachHang.setMatKhau(hashPassword(matKhauMoi));
        khachHangDAO.save(khachHang);

        clearOtp(emailKey);
        return true;
    }

    private void clearOtp(String emailKey) {
        otpMap.remove(emailKey);
        otpAttempts.remove(emailKey);
        verifiedOtp.remove(emailKey);
    }

    private boolean verifyStoredPassword(String rawPassword, String storedPassword) {
        if (storedPassword == null) return false;

        if (storedPassword.startsWith("PBKDF2$")) {
            return verifyPassword(rawPassword, storedPassword);
        }

        return rawPassword.equals(storedPassword);
    }

    private String hashPassword(String password) {
        try {
            byte[] salt = new byte[SALT_LENGTH];
            secureRandom.nextBytes(salt);

            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    PBKDF2_ITERATIONS,
                    HASH_LENGTH
            );

            byte[] hash = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();

            return "PBKDF2$"
                    + PBKDF2_ITERATIONS + "$"
                    + Base64.getEncoder().encodeToString(salt) + "$"
                    + Base64.getEncoder().encodeToString(hash);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Không thể mã hóa mật khẩu.", e);
        }
    }

    private boolean verifyPassword(String password, String stored) {
        try {
            String[] parts = stored.split("\\$", -1);
            if (parts.length != 4 || !"PBKDF2".equals(parts[0])) {
                return false;
            }

            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[3]);

            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    iterations,
                    expectedHash.length * 8
            );

            byte[] actualHash = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();

            return java.security.MessageDigest.isEqual(expectedHash, actualHash);
        } catch (Exception e) {
            return false;
        }
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
