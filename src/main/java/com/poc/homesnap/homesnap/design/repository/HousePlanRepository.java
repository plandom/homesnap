package com.poc.homesnap.homesnap.design.repository;

import com.poc.homesnap.homesnap.design.model.HousePlan;
import com.tei.eziam.iam.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HousePlanRepository extends JpaRepository<HousePlan, Long> {
    List<HousePlan> findByUser(User user);
    List<HousePlan> findByUserAndCityContainingIgnoreCase(User user, String city);
    List<HousePlan> findByUserAndPostalCode(User user, String postalCode);
    int countByUser(User user);
} 