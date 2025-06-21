package com.poc.homesnap.homesnap.design.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.homesnap.homesnap.design.model.HousePlan;
import com.poc.homesnap.homesnap.design.model.rhino.FrontendMeshResponse;
import com.poc.homesnap.homesnap.design.model.rhino.MeshResponse;
import com.poc.homesnap.homesnap.design.model.rhino.MeshResponse.MeshData;
import com.poc.homesnap.homesnap.design.repository.HousePlanRepository;
import com.poc.homesnap.homesnap.design.service.VisualizationService;
import com.tei.eziam.iam.domain.User;
import com.tei.eziam.iam.domain.UserId;
import com.tei.eziam.iam.domain.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MockVisualizationService implements VisualizationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HousePlanRepository housePlanRepository;

    @Override
    @SneakyThrows
    public FrontendMeshResponse generateVisualization(HousePlan housePlan) {
        // call rhino
        MeshResponse meshResponse = loadMeshResponse("rhino-mock.json");
        System.out.println(meshResponse.values());

        // Mock implementation that returns a simple JSON structure
        return mapToFrontendMesh(meshResponse);
    }

    public FrontendMeshResponse mapToFrontendMesh(MeshResponse meshResponse) {
        // Navigate to your MeshData
        MeshData meshData = meshResponse
            .values()
            .stream()
            .filter(value -> value.paramName().startsWith("RH_OUT:")) // Assuming you have RH_OUT outputs
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No mesh output found"))
            .innerTree()
            .get("{0}")
            .getFirst() // Assuming one mesh
            .data();

        // Map vertices
        List<List<Double>> vertices = meshData.vertices().stream()
            .map(vertex -> List.of(vertex.x(), vertex.y(), vertex.z()))
            .toList();

        // Map faces
        List<List<Integer>> faces = meshData.faces().stream()
            .map(face -> List.of(face.a(), face.b(), face.c(), face.d()))
            .toList();

        return new FrontendMeshResponse(vertices, faces);
    }

    public static MeshResponse loadMeshResponse(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new ClassPathResource(filePath).getInputStream(), MeshResponse.class);
    }

    @Override
    public boolean canGenerateMoreVisualizations(UserId userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        long currentPlanCount = housePlanRepository.countByUser(user);

        return currentPlanCount < 3;
    }
} 