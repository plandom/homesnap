package com.poc.homesnap.homesnap.design.model;

import com.poc.homesnap.homesnap.design.model.ZoningData.Feature;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Metadata {

    String parcelIdentifier;
    String voivodeship;
    String county;
    String municipality;
    String district;
    Double areaHectares;
    String mapLink;
    String function; // Nowe pole dla FUNKCJA
    String groupAndSubgroup; // Nowe pole dla połączonych GRUPA i PODGRUPA
    List<String> coordinates;

    public static Metadata map(ParcelData parcelData, ZoningData zoningData, List<String> coordinates) {
        if (zoningData == null) {
            return builder()
                .parcelIdentifier(parcelData.parcelIdentifier())
                .voivodeship(parcelData.voivodeship())
                .county(parcelData.county())
                .municipality(parcelData.municipality())
                .district(parcelData.district())
                .areaHectares(parcelData.areaHectares() == null || parcelData.areaHectares().isEmpty() ? null : Double.parseDouble(parcelData.areaHectares()))
                .mapLink(parcelData.additionalInfo())
                .coordinates(coordinates)
                .build();
        }

        List<Feature> features = zoningData.features();
        ZoningData.Feature.Properties zoningProperties;
        if (!features.isEmpty()) {
            zoningProperties = features.getFirst().properties();
        }
        else {
            zoningProperties = null;
        }
        return builder()
            .parcelIdentifier(parcelData.parcelIdentifier())
            .voivodeship(parcelData.voivodeship())
            .county(parcelData.county())
            .municipality(parcelData.municipality())
            .district(parcelData.district())
            .areaHectares(parcelData.areaHectares() == null || parcelData.areaHectares().isEmpty() ? null : Double.parseDouble(parcelData.areaHectares()))
            .mapLink(parcelData.additionalInfo())
            .function(zoningProperties.FUNKCJA())
            .groupAndSubgroup(zoningProperties.GRUPA() + " - " + zoningProperties.PODGRUPA())
            .coordinates(coordinates)
            .build();
    }

}
