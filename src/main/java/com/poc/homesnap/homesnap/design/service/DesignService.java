package com.poc.homesnap.homesnap.design.service;


import com.poc.homesnap.homesnap.design.dto.DesignRequestDto;
import com.poc.homesnap.homesnap.design.model.Design;
import com.tei.eziam.iam.domain.UserId;

import java.util.List;

public interface DesignService {

    void save(DesignRequestDto designRequestDto);

    List<Design> findByUserId(UserId userId);
} 