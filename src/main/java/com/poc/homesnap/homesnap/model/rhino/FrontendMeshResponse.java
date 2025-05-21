package com.poc.homesnap.homesnap.model.rhino;

import java.util.List;

public record FrontendMeshResponse(
    List<List<Double>> vertices,
    List<List<Integer>> faces
) {}

