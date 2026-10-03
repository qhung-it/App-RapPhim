package com.example.studentapp.apprapphim.model.dao;

import com.example.studentapp.apprapphim.model.entity.Rap;

import java.util.List;

public interface RapDAO {
    Rap findById(String maRap);

    List<Rap> findAll();

    Rap save(Rap rap);

    void delete(Rap rap);
}
