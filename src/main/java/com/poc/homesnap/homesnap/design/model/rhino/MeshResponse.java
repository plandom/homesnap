package com.poc.homesnap.homesnap.design.model.rhino;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record MeshResponse(
    @JsonProperty("values") List<Value> values
) {

    public record Value(
        @JsonProperty("ParamName") String paramName,
        @JsonProperty("InnerTree") Map<String, List<MeshElement>> innerTree
    ) {

    }

    public record MeshElement(
        @JsonProperty("type") String type,
        @JsonProperty("data") MeshData data
    ) {

    }

    public record MeshData(
        @JsonProperty("Vertices") List<Vertex> vertices,
        @JsonProperty("Faces") List<Face> faces
    ) {

    }

    public record Vertex(
        @JsonProperty("X") double x,
        @JsonProperty("Y") double y,
        @JsonProperty("Z") double z
    ) {

    }

    public record Face(
        @JsonProperty("A") int a,
        @JsonProperty("B") int b,
        @JsonProperty("C") int c,
        @JsonProperty("D") int d
    ) {

    }
}