package com.spring.jwt.Bidding.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class SalesPersonDto {

        private Integer salesPersonId;

        private String address;

        private String firstName;

        private String lastName;

        private Long  documentId;

        private String area;

        private String city;

        private Date joiningdate;

        private Boolean status;

        private String email;

        private String mobileNo;

        private Integer UserId;

        private int totalAddedDealers;

    }

