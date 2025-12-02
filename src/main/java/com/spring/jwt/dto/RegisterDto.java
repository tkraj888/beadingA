package com.spring.jwt.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RegisterDto {

    public String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    public String mobileNo;
    public String firstName;
    public String lastName;
    public String address;
    public String city;
    public String roles;
    public int document;
    public String shopName;
    public String area;
    public boolean status;
    public String userType;
    private Long  documentId;
    private Date joiningdate;
    private Integer salesPersonId;

}
