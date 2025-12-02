package com.spring.jwt.Bidding.DTO;

import com.spring.jwt.dto.InspectorProfileDto;
import lombok.Data;
@Data
public class SingleSalesPersonDto {

        private String status;
        private SalesPersonDto Response;

        public SingleSalesPersonDto(String status) {
            this.status = status;
        }

    }

