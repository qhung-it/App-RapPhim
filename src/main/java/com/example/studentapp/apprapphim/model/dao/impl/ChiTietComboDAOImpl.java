package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.dao.interf.ChiTietComboDAO;
import com.example.studentapp.apprapphim.model.entity.ChiTietCombo;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.util.List;

public class ChiTietComboDAOImpl implements ChiTietComboDAO {

    @Override
    public ChiTietCombo findById(Long id) {
        if (id == null) return null;
        return JpaDaoSupport.execute(em -> em.find(ChiTietCombo.class, id));
    }

    @Override
    public List<ChiTietCombo> findByDonDatVe(String maDon) {
        if (maDon == null || maDon.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT c FROM ChiTietCombo c
                        JOIN FETCH c.combo
                        WHERE c.donDatVe.maDon = :maDon
                        ORDER BY c.id
                        """, ChiTietCombo.class)
                        .setParameter("maDon", maDon)
                        .getResultList());
    }

    @Override
    public List<ChiTietCombo> findByCombo(String maCombo) {
        if (maCombo == null || maCombo.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT c FROM ChiTietCombo c
                        JOIN FETCH c.donDatVe
                        WHERE c.combo.maCombo = :maCombo
                        ORDER BY c.id
                        """, ChiTietCombo.class)
                        .setParameter("maCombo", maCombo)
                        .getResultList());
    }

    @Override
    public ChiTietCombo findByDonDatVeAndCombo(String maDon, String maCombo) {
        if (maDon == null || maDon.isBlank() || maCombo == null || maCombo.isBlank()) return null;
        return JpaDaoSupport.execute(em -> {
            try {
                return em.createQuery("""
                        SELECT c FROM ChiTietCombo c
                        JOIN FETCH c.combo
                        WHERE c.donDatVe.maDon = :maDon
                          AND c.combo.maCombo = :maCombo
                        """, ChiTietCombo.class)
                        .setParameter("maDon", maDon)
                        .setParameter("maCombo", maCombo)
                        .getSingleResult();
            } catch (jakarta.persistence.NoResultException ex) {
                return null;
            }
        });
    }

    @Override
    public ChiTietCombo save(ChiTietCombo chiTietCombo) {
        if (chiTietCombo == null) throw new IllegalArgumentException("ChiTietCombo không được null");
        return JpaDaoSupport.executeInTransaction(em -> {
            chiTietCombo.tinhThanhTien();
            if (chiTietCombo.getId() != null && em.find(ChiTietCombo.class, chiTietCombo.getId()) != null) {
                return em.merge(chiTietCombo);
            }
            em.persist(chiTietCombo);
            return chiTietCombo;
        });
    }

    @Override
    public void delete(ChiTietCombo chiTietCombo) {
        if (chiTietCombo == null) return;
        JpaDaoSupport.executeInTransaction(em -> {
            ChiTietCombo managed = chiTietCombo.getId() == null ? null : em.find(ChiTietCombo.class, chiTietCombo.getId());
            if (managed != null) em.remove(managed);
            return null;
        });
    }
}
