package com.poc.homesnap.homesnap.design.service.impl;

import com.poc.homesnap.homesnap.design.dto.DesignRequestDto;
import com.poc.homesnap.homesnap.design.model.Design;
import com.poc.homesnap.homesnap.design.service.DesignService;
import com.tei.eziam.iam.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DesignServiceImpl implements DesignService {

    private final DesignRepository designRepository;

    public void save(DesignRequestDto design) {
        Design toSave = Design.create(new UserId(design.getUserId()), design);
        designRepository.save(toSave);
    }

    @Override
    public List<Design> findByUserId(UserId userId) {
        return designRepository.findAllByUserId(userId);
    }
}