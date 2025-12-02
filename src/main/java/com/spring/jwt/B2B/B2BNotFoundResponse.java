package com.spring.jwt.B2B;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class B2BNotFoundResponse {

    private String error;
    private String message;
}
