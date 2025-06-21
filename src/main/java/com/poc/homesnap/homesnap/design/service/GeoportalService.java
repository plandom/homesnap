package com.poc.homesnap.homesnap.design.service;

import com.poc.homesnap.homesnap.design.model.ParcelData;
import com.poc.homesnap.homesnap.design.model.ZoningData;

import java.util.List;

public interface GeoportalService {

    String getBBoxFromCoordinates(String x, String y);
    List<String> getPolygonFromXy(String x, String y);

    ParcelData getFeatureInfoParcel(String bbox);

    ZoningData getFeatureInfoZoning(String bbox);
} 