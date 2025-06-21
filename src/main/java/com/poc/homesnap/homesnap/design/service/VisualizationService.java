package com.poc.homesnap.homesnap.design.service;

import com.poc.homesnap.homesnap.design.model.HousePlan;
import com.poc.homesnap.homesnap.design.model.rhino.FrontendMeshResponse;
import com.tei.eziam.iam.domain.UserId;

public interface VisualizationService {
    /**
     * Generates a 3D visualization for a house plan
     * @param housePlan The house plan to generate visualization for
     * @return The visualization data in a format suitable for Three.js
     */
    FrontendMeshResponse generateVisualization(HousePlan housePlan);

    boolean canGenerateMoreVisualizations(UserId userId);
} 