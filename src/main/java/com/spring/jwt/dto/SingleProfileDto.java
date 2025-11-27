package com.spring.jwt.dto;
import lombok.Data;

@Data
public class SingleProfileDto {

    private String status;
    private InspectorProfileDto Response;

    public SingleProfileDto(String status) {
        this.status = status;
    }

}
