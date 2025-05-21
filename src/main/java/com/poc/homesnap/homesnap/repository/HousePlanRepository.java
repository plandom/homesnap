package com.poc.homesnap.homesnap.repository;

import com.poc.homesnap.homesnap.model.HousePlan;
import com.poc.homesnap.homesnap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HousePlanRepository extends JpaRepository<HousePlan, Long> {
    List<HousePlan> findByUser(User user);
    List<HousePlan> findByUserAndCityContainingIgnoreCase(User user, String city);
    List<HousePlan> findByUserAndPostalCode(User user, String postalCode);
    int countByUser(User user);
} 