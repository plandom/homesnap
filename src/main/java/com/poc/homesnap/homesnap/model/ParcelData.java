package com.poc.homesnap.homesnap.model;

public record ParcelData(
    String parcelIdentifier,
    String voivodeship,
    String county,
    String municipality,
    String district,
    String parcelNumber,
    String areaHectares,
    String registrationGroup,
    String usageDesignation,
    String contourDesignation,
    String publicationDate,
    String originInfo,
    String additionalInfo,
    String qrCode
) {

}