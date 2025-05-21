package com.poc.homesnap.homesnap.service;

import com.poc.homesnap.homesnap.model.ParcelData;
import com.poc.homesnap.homesnap.model.ZoningData;

import java.util.List;

public interface GeoportalService {

    String getBBoxFromCoordinates(String x, String y);
    List<String> getPolygonFromXy(String x, String y);

    ParcelData getFeatureInfoParcel(String bbox);

    ZoningData getFeatureInfoZoning(String bbox);
} 