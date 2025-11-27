package com.spring.jwt.B2B;

import lombok.Data;

@Data
public class UserInfoDto {

    private Integer userId;
    private String email;
    private String mobileNo;
    private String firstName;
    private String lastName;
    private String address;
}
