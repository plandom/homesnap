package com.poc.homesnap.homesnap.design.events;

import com.poc.homesnap.homesnap.design.dto.DesignRequestDto;
import com.tei.ezcommon.common.events.Event;
import com.tei.eziam.iam.domain.UserId;
import lombok.Getter;

@Getter
public class DesignGeneratedEvent implements Event {
    private final UserId userId;
    private final DesignRequestDto  designRequestDto;

    public DesignGeneratedEvent(UserId userId, DesignRequestDto designRequestDto) {
        this.userId = userId;
        this.designRequestDto = designRequestDto;
    }

    @Override
    public String toString() {
        return "DesignGeneratedEvent{" +
            "userId=" + userId +
            ", designRequestDto=" + designRequestDto +
            '}';
    }
}
