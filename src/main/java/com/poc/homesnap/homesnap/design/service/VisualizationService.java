package com.poc.homesnap.homesnap.design.service;

import com.tei.eziam.iam.domain.UserId;

public interface VisualizationService {

    boolean canGenerateMoreVisualizations(UserId userId);
} 