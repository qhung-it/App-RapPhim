package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.Enum.TrangThaiCombo;
import com.example.studentapp.apprapphim.model.dao.ComboDAO;
import com.example.studentapp.apprapphim.model.entity.Combo;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import jakarta.persistence.NoResultException;
import java.util.List;

public class ComboDAOImpl implements ComboDAO {

    @Override
    public Combo findById(String maCombo) {
        if (maCombo == null || maCombo.isBlank()) return null;
        return JpaDaoSupport.execute(em -> em.find(Combo.class, maCombo));
    }

    @Override
    public List<Combo> findAll() {
        return JpaDaoSupport.execute(em ->
                em.createQuery("SELECT c FROM Combo c ORDER BY c.maCombo", Combo.class)
                        .getResultList());
    }

    @Override
    public List<Combo> findByTrangThai(TrangThaiCombo trangThai) {
        if (trangThai == null) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT c FROM Combo c
                        WHERE c.trangThai = :trangThai
                        ORDER BY c.tenCombo
                        """, Combo.class)
                        .setParameter("trangThai", trangThai)
                        .getResultList());
    }

    @Override
    public List<Combo> findByTenCombo(String tenCombo) {
        if (tenCombo == null || tenCombo.isBlank()) return List.of();
        return JpaDaoSupport.execute(em ->
                em.createQuery("""
                        SELECT c FROM Combo c
                        WHERE LOWER(c.tenCombo) LIKE LOWER(:ten)
                        ORDER BY c.tenCombo
                        """, Combo.class)
                        .setParameter("ten", "%" + tenCombo.trim() + "%")
                        .getResultList());
    }

    @Override
    public Combo save(Combo combo) {
        if (combo == null) throw new IllegalArgumentException("Combo không được null");
        return JpaDaoSupport.executeInTransaction(em ->
                combo.getMaCombo() != null && em.find(Combo.class, combo.getMaCombo()) != null
                        ? em.merge(combo)
                        : persistAndReturn(em, combo));
    }

    private Combo persistAndReturn(jakarta.persistence.EntityManager em, Combo combo) {
        em.persist(combo);
        return combo;
    }

    @Override
    public void delete(Combo combo) {
        if (combo == null) return;
        JpaDaoSupport.executeInTransaction(em -> {
            Combo managed = em.find(Combo.class, combo.getMaCombo());
            if (managed != null) em.remove(managed);
            return null;
        });
    }
}
