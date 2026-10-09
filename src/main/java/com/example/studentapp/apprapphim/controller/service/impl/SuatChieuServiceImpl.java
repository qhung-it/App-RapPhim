package com.example.studentapp.apprapphim.controller.service.impl;

import com.example.studentapp.apprapphim.controller.service.interf.SuatChieuService;
import com.example.studentapp.apprapphim.model.dao.interf.GheDAO;
import com.example.studentapp.apprapphim.model.dao.interf.SuatChieuDAO;
import com.example.studentapp.apprapphim.model.dao.interf.VeDAO;
import com.example.studentapp.apprapphim.model.dao.impl.GheDAOImpl;
import com.example.studentapp.apprapphim.model.dao.impl.SuatChieuDAOImpl;
import com.example.studentapp.apprapphim.model.dao.impl.VeDAOImpl;
import com.example.studentapp.apprapphim.model.entity.Ghe;
import com.example.studentapp.apprapphim.model.entity.SuatChieu;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SuatChieuServiceImpl implements SuatChieuService {

    private final SuatChieuDAO suatChieuDAO;
    private final GheDAO gheDAO;
    private final VeDAO veDAO;

    public SuatChieuServiceImpl() {
        this(new SuatChieuDAOImpl(), new GheDAOImpl(), new VeDAOImpl());
    }

    public SuatChieuServiceImpl(SuatChieuDAO suatChieuDAO, GheDAO gheDAO, VeDAO veDAO) {
        this.suatChieuDAO = suatChieuDAO;
        this.gheDAO = gheDAO;
        this.veDAO = veDAO;
    }

    @Override
    public boolean taoSuatChieu(SuatChieu suatChieu) {
        if (!hopLe(suatChieu)) return false;
        return JpaDaoSupport.executeInTransaction(em -> {
            if (suatChieuDAO.findById(suatChieu.getMaSuat()) != null) return false;
            if (suatChieuDAO.existsConflict(suatChieu.getPhongChieu().getMaPhong(), suatChieu.getNgayChieu(),
                    suatChieu.getGioBatDau(), suatChieu.getGioKetThuc(), null)) return false;
            suatChieuDAO.save(suatChieu);
            return true;
        });
    }

    @Override
    public boolean capNhatSuatChieu(SuatChieu suatChieu) {
        if (!hopLe(suatChieu)) return false;
        return JpaDaoSupport.executeInTransaction(em -> {
            if (suatChieuDAO.findById(suatChieu.getMaSuat()) == null) return false;
            if (suatChieuDAO.existsConflict(suatChieu.getPhongChieu().getMaPhong(), suatChieu.getNgayChieu(),
                    suatChieu.getGioBatDau(), suatChieu.getGioKetThuc(), suatChieu.getMaSuat())) return false;
            if (!veDAO.findBySuatChieu(suatChieu.getMaSuat()).isEmpty()) return false;
            suatChieuDAO.save(suatChieu);
            return true;
        });
    }

    private boolean hopLe(SuatChieu s) {
        return s != null && s.getMaSuat() != null && !s.getMaSuat().isBlank()
                && s.getNgayChieu() != null
                && s.getGioBatDau() != null && s.getGioKetThuc() != null
                && s.getGioBatDau().isBefore(s.getGioKetThuc())
                && s.getGiaSuat() != null && s.getGiaSuat().signum() >= 0
                && s.getPhim() != null && s.getPhongChieu() != null;
    }

    @Override
    public boolean xoaSuatChieu(String maSuat) {
        return JpaDaoSupport.executeInTransaction(em -> {
            SuatChieu s = suatChieuDAO.findById(maSuat);
            if (s == null || !veDAO.findBySuatChieu(maSuat).isEmpty()) return false;
            suatChieuDAO.delete(s);
            return true;
        });
    }

    @Override
    public SuatChieu timTheoMa(String maSuat) {
        return suatChieuDAO.findById(maSuat);
    }

    @Override
    public List<SuatChieu> layDanhSachSuatChieu() {
        return suatChieuDAO.findAll();
    }

    @Override
    public List<SuatChieu> timSuatChieu(String maPhim, LocalDate ngayChieu, String maPhong) {
        return suatChieuDAO.findByPhimAndNgay(maPhim, ngayChieu).stream()
                .filter(s -> maPhong == null || maPhong.isBlank()
                        || (s.getPhongChieu() != null && maPhong.equals(s.getPhongChieu().getMaPhong())))
                .toList();
    }

    @Override
    public List<SuatChieu> laySuatChieuTheoPhim(String maPhim) {
        return suatChieuDAO.findByPhim(maPhim);
    }

    @Override
    public List<SuatChieu> laySuatChieuTheoNgay(LocalDate ngayChieu) {
        return suatChieuDAO.findByNgay(ngayChieu);
    }

    @Override
    public List<Ghe> layGheTrong(String maSuat) {
        SuatChieu s = suatChieuDAO.findById(maSuat);
        if (s == null || s.getPhongChieu() == null) return List.of();

        Set<String> gheDaDat = new HashSet<>(veDAO.findMaGheDaDat(maSuat));
        return gheDAO.findByPhongChieu(s.getPhongChieu().getMaPhong()).stream()
                .filter(g -> !gheDaDat.contains(g.getMaGhe()))
                .toList();
    }

    @Override
    public boolean kiemTraGheTrong(String maSuat, List<String> danhSachMaGhe) {
        if (maSuat == null || danhSachMaGhe == null || danhSachMaGhe.isEmpty()) return false;
        Set<String> gheDaDat = new HashSet<>(veDAO.findMaGheDaDat(maSuat));
        return danhSachMaGhe.stream().noneMatch(gheDaDat::contains);
    }

    @Override
    public boolean kiemTraTrungLich(String maPhong, LocalDate ngayChieu, LocalTime gioBatDau,
                                    LocalTime gioKetThuc, String maSuatLoaiTru) {
        return suatChieuDAO.existsConflict(maPhong, ngayChieu, gioBatDau, gioKetThuc, maSuatLoaiTru);
    }
}
