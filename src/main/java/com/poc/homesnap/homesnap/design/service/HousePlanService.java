package com.poc.homesnap.homesnap.design.service;

import com.poc.homesnap.homesnap.design.model.HousePlan;
import com.tei.eziam.iam.domain.UserId;

import java.util.List;
import java.util.Optional;


public interface HousePlanService {
    HousePlan createHousePlan(UserId userId, HousePlan housePlan);
    List<HousePlan> getUserHousePlans(UserId userId);
    Optional<HousePlan> getHousePlan(UserId userId, Long id);
    Optional<HousePlan> updateHousePlan(UserId userId, Long id, HousePlan housePlan);
    boolean deleteHousePlan(UserId userId, Long id);
} 