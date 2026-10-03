package com.example.studentapp.apprapphim.model.dao.impl;

import com.example.studentapp.apprapphim.model.dao.ThanhToanDAO;
import com.example.studentapp.apprapphim.model.entity.ThanhToan;
import com.example.studentapp.apprapphim.model.util.JpaDaoSupport;
import java.util.List;

public class ThanhToanDAOImpl implements ThanhToanDAO {

    @Override
    public ThanhToan findById(String maTT) {
        if (maTT == null || maTT.isBlank()) return null;
        return JpaDaoSupport.execute(em -> {
            List<ThanhToan> result = em.createQuery("""
                    SELECT t FROM ThanhToan t
                    LEFT JOIN FETCH t.donDatVe
                    WHERE t.maTT = :maTT
                    """, ThanhToan.class)
                    .setParameter("maTT", maTT)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    @Override
    public ThanhToan findByDonDatVe(String maDon) {
        if (maDon == null || maDon.isBlank()) return null;
        return JpaDaoSupport.execute(em -> {
            List<ThanhToan> result = em.createQuery("""
                    SELECT t FROM ThanhToan t
                    LEFT JOIN FETCH t.donDatVe
                    WHERE t.donDatVe.maDon = :maDon
                    """, ThanhToan.class)
                    .setParameter("maDon", maDon)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    @Override
    public List<ThanhToan> findAll() {
        return JpaDaoSupport.execute(em ->
                em.createQuery("SELECT t FROM ThanhToan t ORDER BY t.thoiGian DESC", ThanhToan.class).getResultList());
    }

    @Override
    public ThanhToan save(ThanhToan thanhToan) {
        if (thanhToan == null) throw new IllegalArgumentException("Thanh toán không được null");
        return JpaDaoSupport.executeInTransaction(em -> {
            if (thanhToan.getMaTT() != null && em.find(ThanhToan.class, thanhToan.getMaTT()) != null) return em.merge(thanhToan);
            em.persist(thanhToan);
            return thanhToan;
        });
    }

    @Override
    public void delete(ThanhToan thanhToan) {
        if (thanhToan == null) return;
        JpaDaoSupport.executeInTransaction(em -> {
            ThanhToan managed = thanhToan.getMaTT() == null ? null : em.find(ThanhToan.class, thanhToan.getMaTT());
            if (managed != null) em.remove(managed);
            return null;
        });
    }
}
