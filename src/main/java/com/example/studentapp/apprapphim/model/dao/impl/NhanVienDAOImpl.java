package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.Enum.ChucVu;
import com.example.studentapp.apprapphim.model.dao.interf.NhanVienDAO;
import com.example.studentapp.apprapphim.model.entity.NhanVien;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.util.List;

public class NhanVienDAOImpl implements NhanVienDAO {

    @Override
    public NhanVien findById(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.isBlank()) return null;
        return JpaDaoSupport.execute(em -> em.find(NhanVien.class, maNhanVien));
    }

    @Override
    public NhanVien findByEmail(String email) {
        if (email == null || email.isBlank()) return null;
        return JpaDaoSupport.execute(em -> {
            List<NhanVien> result = em.createQuery("""
                    SELECT n FROM NhanVien n
                    WHERE LOWER(n.email) = LOWER(:email)
                    """, NhanVien.class)
                    .setParameter("email", email.trim())
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    @Override
    public List<NhanVien> findAll() {
        return JpaDaoSupport.execute(em ->
                em.createQuery("SELECT n FROM NhanVien n ORDER BY n.maNV", NhanVien.class)
                        .getResultList());
    }

    @Override
    public List<NhanVien> findByChucVu(ChucVu chucVu) {
        if (chucVu == null) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT n FROM NhanVien n
                        WHERE n.chucVu = :chucVu
                        ORDER BY n.hoTen
                        """, NhanVien.class)
                        .setParameter("chucVu", chucVu)
                        .getResultList());
    }

    @Override
    public NhanVien save(NhanVien nhanVien) {
        if (nhanVien == null) throw new IllegalArgumentException("Nhân viên không được null");
        return JpaDaoSupport.executeInTransaction(em -> {
            if (nhanVien.getMaNV() != null && em.find(NhanVien.class, nhanVien.getMaNV()) != null) return em.merge(nhanVien);
            em.persist(nhanVien);
            return nhanVien;
        });
    }

    @Override
    public void delete(NhanVien nhanVien) {
        if (nhanVien == null) return;
        JpaDaoSupport.executeInTransaction(em -> {
            NhanVien managed = nhanVien.getMaNV() == null ? null : em.find(NhanVien.class, nhanVien.getMaNV());
            if (managed != null) em.remove(managed);
            return null;
        });
    }
}
