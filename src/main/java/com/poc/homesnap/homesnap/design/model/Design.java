package com.poc.homesnap.homesnap.design.model;

import com.poc.homesnap.homesnap.design.dto.DesignRequestDto;
import com.tei.eziam.iam.domain.UserId;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "designs")
@NoArgsConstructor
@AllArgsConstructor
public class Design {

    @Id
    private UUID id;

    @Embedded
    private UserId userId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Getter
    private DesignRequestDto designRequest;

    public static Design create(UserId id, DesignRequestDto designRequest) {
        return new Design(UUID.randomUUID(), id, designRequest);
    }
}