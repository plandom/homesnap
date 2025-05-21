package com.poc.homesnap.homesnap.service.impl;

import com.poc.homesnap.homesnap.model.HousePlan;
import com.poc.homesnap.homesnap.model.User;
import com.poc.homesnap.homesnap.repository.HousePlanRepository;
import com.poc.homesnap.homesnap.repository.UserRepository;
import com.poc.homesnap.homesnap.service.HousePlanService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HousePlanServiceImpl implements HousePlanService {

    @Autowired
    private HousePlanRepository housePlanRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public HousePlan createHousePlan(Long userId, HousePlan housePlan) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        housePlan.setUser(user);
        return housePlanRepository.save(housePlan);
    }

    @Override
    public List<HousePlan> getUserHousePlans(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return housePlanRepository.findByUser(user);
    }

    @Override
    public Optional<HousePlan> getHousePlan(Long userId, Long id) {
        return housePlanRepository.findById(id)
                .filter(plan -> plan.getUser().getId().equals(userId));
    }

    @Override
    @Transactional
    public Optional<HousePlan> updateHousePlan(Long userId, Long id, HousePlan housePlan) {
        return housePlanRepository.findById(id)
                .filter(plan -> plan.getUser().getId().equals(userId))
                .map(existingPlan -> {
                    existingPlan.setName(housePlan.getName());
                    existingPlan.setDescription(housePlan.getDescription());
                    existingPlan.setParcelId(housePlan.getParcelId());
                    existingPlan.setCity(housePlan.getCity());
                    existingPlan.setPostalCode(housePlan.getPostalCode());
                    existingPlan.setAddress(housePlan.getAddress());
                    return housePlanRepository.save(existingPlan);
                });
    }

    @Override
    @Transactional
    public boolean deleteHousePlan(Long userId, Long id) {
        return housePlanRepository.findById(id)
                .filter(plan -> plan.getUser().getId().equals(userId))
                .map(plan -> {
                    housePlanRepository.delete(plan);
                    return true;
                })
                .orElse(false);
    }
} 