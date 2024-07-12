package com.codegym.service;

import com.codegym.model.Promotion;

import java.util.Optional;

public interface IPromotionService {
    Iterable<Promotion> findAll();
    void save(Promotion promotion);
    Optional<Promotion> findById(Long id);
    void remove(Long id);
}
