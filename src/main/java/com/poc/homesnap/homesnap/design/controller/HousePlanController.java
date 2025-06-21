package com.poc.homesnap.homesnap.design.controller;

import com.poc.homesnap.homesnap.design.model.HousePlan;
import com.poc.homesnap.homesnap.design.model.rhino.FrontendMeshResponse;
import com.poc.homesnap.homesnap.design.service.HousePlanService;
import com.poc.homesnap.homesnap.design.service.VisualizationService;
import com.tei.eziam.iam.domain.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
        String userId,
            @RequestBody HousePlan housePlan) {
        if (!visualizationService.canGenerateMoreVisualizations(UserId.fromString(userId))) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(housePlanService.createHousePlan(UserId.fromString(userId), housePlan));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HousePlan> getHousePlan(
             String userId,
            @PathVariable Long id) {
        return housePlanService.getHousePlan(UserId.fromString(userId), id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<HousePlan> updateHousePlan(
             String userId,
            @PathVariable Long id,
            @RequestBody HousePlan housePlan) {
        return housePlanService.updateHousePlan(UserId.fromString(userId), id, housePlan)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHousePlan(
            String userId,
            @PathVariable Long id) {
        if (housePlanService.deleteHousePlan(UserId.fromString(userId), id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/visualization")
    public ResponseEntity<FrontendMeshResponse> getVisualization(
            String userId,
            @PathVariable Long id) {
        return housePlanService.getHousePlan(UserId.fromString(userId), id)
                .map(plan -> ResponseEntity.ok(visualizationService.generateVisualization(plan)))
                .orElse(ResponseEntity.notFound().build());
    }
} 