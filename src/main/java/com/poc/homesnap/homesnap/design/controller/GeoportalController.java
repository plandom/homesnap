package com.poc.homesnap.homesnap.design.controller;

import com.poc.homesnap.homesnap.design.model.Metadata;
import com.poc.homesnap.homesnap.design.model.ParcelData;
import com.poc.homesnap.homesnap.design.model.ZoningData;
import com.poc.homesnap.homesnap.design.service.GeoportalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/parcels")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GeoportalController {

    private final GeoportalService geoportalService;

    @GetMapping("/{x}/{y}")
    public Metadata getParcelMetadata(@PathVariable String x, @PathVariable String y) {
        String bBoxFromCoordinates = geoportalService.getBBoxFromCoordinates(x, y);
        ParcelData featureInfoParcel = geoportalService.getFeatureInfoParcel(bBoxFromCoordinates);
        ZoningData featureInfoZoning = geoportalService.getFeatureInfoZoning(bBoxFromCoordinates);
        List<String> coordinates = geoportalService.getPolygonFromXy(x, y);
        return Metadata.map(featureInfoParcel, featureInfoZoning, coordinates);
    }
} 