package com.poc.homesnap.homesnap.service;

import com.poc.homesnap.homesnap.model.HousePlan;
import java.util.List;
import java.util.Optional;


public interface HousePlanService {
    HousePlan createHousePlan(Long userId, HousePlan housePlan);
    List<HousePlan> getUserHousePlans(Long userId);
    Optional<HousePlan> getHousePlan(Long userId, Long id);
    Optional<HousePlan> updateHousePlan(Long userId, Long id, HousePlan housePlan);
    boolean deleteHousePlan(Long userId, Long id);
} 