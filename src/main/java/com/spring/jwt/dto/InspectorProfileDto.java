package com.spring.jwt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InspectorProfileDto {

        private Integer inspectorProfileId;

        private String address;

        private String city;

        private String firstName;

        private String lastName;

        private String email;

        private Boolean status;

        private boolean isStatusProvided = false;

        private String mobileNo;

        private Integer UserId;


        public void setStatus(boolean status) {
                this.status = status;
                this.isStatusProvided = true;
        }
}
