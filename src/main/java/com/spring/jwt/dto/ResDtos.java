package com.spring.jwt.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResDtos {
    public String status;
    public Object object;

    public ResDtos(String status, Object object) {
        this.status=status;
        this.object=object;
    }

}
