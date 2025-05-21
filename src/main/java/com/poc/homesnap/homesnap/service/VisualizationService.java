package com.poc.homesnap.homesnap.service;

import com.poc.homesnap.homesnap.model.HousePlan;
import com.poc.homesnap.homesnap.model.rhino.FrontendMeshResponse;

public interface VisualizationService {
    /**
     * Generates a 3D visualization for a house plan
     * @param housePlan The house plan to generate visualization for
     * @return The visualization data in a format suitable for Three.js
     */
    FrontendMeshResponse generateVisualization(HousePlan housePlan);

    /**
     * Validates if a user can generate more visualizations based on their subscription
     * @param userId The ID of the user
     * @return true if the user can generate more visualizations, false otherwise
     */
    boolean canGenerateMoreVisualizations(Long userId);
} 