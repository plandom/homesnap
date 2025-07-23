package com.poc.homesnap.homesnap.design.controller;

import com.poc.homesnap.homesnap.design.dto.DesignRequestDto;
import com.poc.homesnap.homesnap.design.events.DesignGeneratedEvent;
import com.poc.homesnap.homesnap.design.model.Design;
import com.poc.homesnap.homesnap.design.service.DesignService;
import com.poc.homesnap.homesnap.design.service.VisualizationService;
import com.tei.ezcommon.common.events.EventPublisher;
import com.tei.eziam.iam.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/design")
@CrossOrigin("*")
@RequiredArgsConstructor
public final class DesignController {

    private final VisualizationService visualizationService;
    private final EventPublisher eventPublisher;
    private final DesignService designService;

    // load params
    // call rhino, mock for now Visualize service
    // if failed throw and return error
    // if worked save design for user
    // and publish event design generated
    @PostMapping("/generate")
    public ResponseEntity<?> generateAndSaveDesign(@RequestBody DesignRequestDto designRequestDto) {
        UserId uid = new UserId(designRequestDto.getUserId());
        if (!visualizationService.canGenerateMoreVisualizations(uid)) {
            return ResponseEntity.badRequest().body("Visualization limit reached");
        }
        try {
            ClassPathResource file = new ClassPathResource("static/meshes/house.glb");
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            designService.save(designRequestDto);
            eventPublisher.publishEvent(new DesignGeneratedEvent(uid, designRequestDto));
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"house.glb\"")
                .contentType(MediaType.parseMediaType("model/gltf-binary"))
                .body(file);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Visualization failed: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public List<Design> findAllByUserId(@PathVariable UserId userId) {
        return designService.findByUserId(userId);
    }
}
