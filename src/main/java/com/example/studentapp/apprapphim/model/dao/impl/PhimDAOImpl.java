package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiPhim;
import com.example.studentapp.apprapphim.model.dao.PhimDAO;
import com.example.studentapp.apprapphim.model.entity.Phim;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.util.List;

public class PhimDAOImpl implements PhimDAO {

    @Override
    public Phim findById(String maPhim) {
        if (maPhim == null || maPhim.isBlank()) return null;
        return JpaDaoSupport.execute(em -> em.find(Phim.class, maPhim));
    }

    @Override
    public List<Phim> findAll() {
        return JpaDaoSupport.execute(em ->
                em.createQuery("SELECT p FROM Phim p ORDER BY p.tenPhim", Phim.class).getResultList());
    }

    @Override
    public List<Phim> findByTenPhim(String tenPhim) {
        if (tenPhim == null || tenPhim.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT p FROM Phim p
                        WHERE LOWER(p.tenPhim) LIKE LOWER(:ten)
                        ORDER BY p.tenPhim
                        """, Phim.class)
                        .setParameter("ten", "%" + tenPhim.trim() + "%")
                        .getResultList());
    }

    @Override
    public List<Phim> findByTheLoai(String theLoai) {
        if (theLoai == null || theLoai.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT p FROM Phim p
                        WHERE LOWER(p.theLoai) LIKE LOWER(:theLoai)
                        ORDER BY p.tenPhim
                        """, Phim.class)
                        .setParameter("theLoai", "%" + theLoai.trim() + "%")
                        .getResultList());
    }

    @Override
    public List<Phim> findByTrangThai(TrangThaiPhim trangThai) {
        if (trangThai == null) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT p FROM Phim p
                        WHERE p.trangThai = :trangThai
                        ORDER BY p.tenPhim
                        """, Phim.class)
                        .setParameter("trangThai", trangThai)
                        .getResultList());
    }

    @Override
    public List<Phim> timKiem(String tenPhim, String theLoai, TrangThaiPhim trangThai, Integer doTuoi) {
        return JpaDaoSupport.execute(em -> {
            StringBuilder jpql = new StringBuilder("SELECT p FROM Phim p WHERE 1=1");
            if (tenPhim != null && !tenPhim.isBlank()) jpql.append(" AND LOWER(p.tenPhim) LIKE LOWER(:tenPhim)");
            if (theLoai != null && !theLoai.isBlank()) jpql.append(" AND LOWER(p.theLoai) LIKE LOWER(:theLoai)");
            if (trangThai != null) jpql.append(" AND p.trangThai = :trangThai");
            if (doTuoi != null) jpql.append(" AND p.doTuoi <= :doTuoi");
            jpql.append(" ORDER BY p.tenPhim");

            var q = em.createQuery(jpql.toString(), Phim.class);
            if (tenPhim != null && !tenPhim.isBlank()) q.setParameter("tenPhim", "%" + tenPhim.trim() + "%");
            if (theLoai != null && !theLoai.isBlank()) q.setParameter("theLoai", "%" + theLoai.trim() + "%");
            if (trangThai != null) q.setParameter("trangThai", trangThai);
            if (doTuoi != null) q.setParameter("doTuoi", doTuoi);
            return q.getResultList();
        });
    }

    @Override
    public Phim save(Phim phim) {
        if (phim == null) throw new IllegalArgumentException("Phim không được null");
        return JpaDaoSupport.executeInTransaction(em -> {
            if (phim.getMaPhim() != null && em.find(Phim.class, phim.getMaPhim()) != null) return em.merge(phim);
            em.persist(phim);
            return phim;
        });
    }

    @Override
    public void delete(Phim phim) {
        if (phim == null) return;
        JpaDaoSupport.executeInTransaction(em -> {
            Phim managed = phim.getMaPhim() == null ? null : em.find(Phim.class, phim.getMaPhim());
            if (managed != null) em.remove(managed);
            return null;
        });
    }
}
