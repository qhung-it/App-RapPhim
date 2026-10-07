package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.dao.interf.RapDAO;
import com.example.studentapp.apprapphim.model.entity.Rap;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.util.List;

public class RapDAOImpl implements RapDAO {

    @Override
    public Rap findById(String maRap) {
        if (maRap == null || maRap.isBlank()) return null;
        return JpaDaoSupport.execute(em -> em.find(Rap.class, maRap));
    }

    @Override
    public List<Rap> findAll() {
        return JpaDaoSupport.execute(em ->
                em.createQuery("SELECT r FROM Rap r ORDER BY r.tenRap", Rap.class).getResultList());
    }

    @Override
    public Rap save(Rap rap) {
        if (rap == null) throw new IllegalArgumentException("Rạp không được null");
        return JpaDaoSupport.executeInTransaction(em -> {
            if (rap.getMaRap() != null && em.find(Rap.class, rap.getMaRap()) != null) return em.merge(rap);
            em.persist(rap);
            return rap;
        });
    }

    @Override
    public void delete(Rap rap) {
        if (rap == null) return;
        JpaDaoSupport.executeInTransaction(em -> {
            Rap managed = rap.getMaRap() == null ? null : em.find(Rap.class, rap.getMaRap());
            if (managed != null) em.remove(managed);
            return null;
        });
    }
}
