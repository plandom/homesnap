package com.poc.homesnap.homesnap.controller;

import com.poc.homesnap.homesnap.model.HousePlan;
import com.poc.homesnap.homesnap.model.rhino.FrontendMeshResponse;
import com.poc.homesnap.homesnap.service.HousePlanService;
import com.poc.homesnap.homesnap.service.VisualizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/visualization")
@CrossOrigin("*")
public class HousePlanController {

    @Autowired
    private HousePlanService housePlanService;

    @Autowired
    private VisualizationService visualizationService;

    @GetMapping
    public FrontendMeshResponse visualize() {
        return visualizationService.generateVisualization(new HousePlan());
    }

    @PostMapping
    public ResponseEntity<HousePlan> createHousePlan(
            @AuthenticationPrincipal Long userId,
            @RequestBody HousePlan housePlan) {
        if (!visualizationService.canGenerateMoreVisualizations(userId)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(housePlanService.createHousePlan(userId, housePlan));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HousePlan> getHousePlan(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        return housePlanService.getHousePlan(userId, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<HousePlan> updateHousePlan(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id,
            @RequestBody HousePlan housePlan) {
        return housePlanService.updateHousePlan(userId, id, housePlan)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHousePlan(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        if (housePlanService.deleteHousePlan(userId, id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/visualization")
    public ResponseEntity<FrontendMeshResponse> getVisualization(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        return housePlanService.getHousePlan(userId, id)
                .map(plan -> ResponseEntity.ok(visualizationService.generateVisualization(plan)))
                .orElse(ResponseEntity.notFound().build());
    }
} 