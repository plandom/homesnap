package com.poc.homesnap.homesnap.design.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.homesnap.homesnap.coin.UserBalance;
import com.poc.homesnap.homesnap.coin.UserBalanceRepository;
import com.poc.homesnap.homesnap.design.model.rhino.FrontendMeshResponse;
import com.poc.homesnap.homesnap.design.model.rhino.MeshResponse;
import com.poc.homesnap.homesnap.design.model.rhino.MeshResponse.MeshData;
import com.poc.homesnap.homesnap.design.service.VisualizationService;
import com.tei.eziam.iam.domain.User;
import com.tei.eziam.iam.domain.UserId;
import com.tei.eziam.iam.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MockVisualizationService implements VisualizationService {

    private final UserRepository userRepository;

    private final UserBalanceRepository userBalance;

    public MockVisualizationService(UserRepository userRepository, UserBalanceRepository userBalance) {
        this.userRepository = userRepository;
        this.userBalance = userBalance;
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
    @Transactional
    public boolean canGenerateMoreVisualizations(UserId userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        UserBalance userBalanceByUserId = userBalance.findUserBalanceByUserId(userId);
        if (userBalanceByUserId == null) {
            throw new RuntimeException("User balance not found");
        }

        int balance = userBalanceByUserId.getBalance();
        if (balance < 30) {
            throw new IllegalStateException("Unsufficent balance");
        }

        return true;
    }
} 