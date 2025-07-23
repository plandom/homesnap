package com.poc.homesnap.homesnap.design.service.impl;

import com.poc.homesnap.homesnap.design.dto.DesignRequestDto;
import com.poc.homesnap.homesnap.design.model.Design;
import com.tei.eziam.iam.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DesignRepository extends JpaRepository<Design, UUID> {

    List<Design> findAllByUserId(UserId userId);
}
