package com.spring.jwt.entity;

import com.spring.jwt.dto.CarVerifyDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carVerified")
public class CarVerified {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "CarVerifiedId", nullable = false)
   private int CarVerifiedId;

   @Column(name = "carId")
   private int carId;

   @Column(name = "partName")
   private  String partName;

   @Column(name = "PartCondition")
   private  String PartCondition;

   @Column(name="userId")
   private Integer userId;

   public CarVerified(CarVerifyDto carVerifyDto) {
      this.userId=carVerifyDto.getUserId();
      this.carId = carVerifyDto.getCarId();
      this.partName = carVerifyDto.getPartName();
      this.PartCondition = carVerifyDto.getPartCondition();
   }
}
