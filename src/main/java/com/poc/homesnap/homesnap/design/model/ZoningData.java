package com.poc.homesnap.homesnap.design.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ZoningData(
    String type,
    List<Feature> features,
    String totalFeatures,
    int numberReturned,
    String timeStamp,
    Crs crs
) {
    public record Feature(
        String type,
        String id,
        Geometry geometry,
        String geometry_name,
        Properties properties
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Geometry(
            String type
            // Właściwość "coordinates" została usunięta
        ) {}

        public record Properties(
            String RODZAJ,
            String RODZAJ_ID,
            int DLUGOSC,
            int OBJECTID,
            String FUNKCJA,
            String GRUPA,
            int ID_GRUPA,
            String PODGRUPA,
            int ID_PODGRUPA
        ) {}
    }

    public record Crs(
        String type,
        CrsProperties properties
    ) {
        public record CrsProperties(
            String name
        ) {}
    }
}