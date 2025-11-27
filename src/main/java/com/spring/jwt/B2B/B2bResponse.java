package com.spring.jwt.B2B;

import com.spring.jwt.B2B.B2BDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class B2bResponse {

        private String status;
        private String message;
        private B2BDto response;


        public B2bResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

    }
