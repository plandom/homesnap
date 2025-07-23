package com.poc.homesnap.homesnap.design.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DesignRequestDto {

    private UUID userId;
    private String parcelId;
    private int bathrooms;
    private int rooms;
    private int floors;
}
