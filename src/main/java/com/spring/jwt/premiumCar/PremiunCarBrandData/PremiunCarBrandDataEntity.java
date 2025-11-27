package com.spring.jwt.premiumCar.PremiunCarBrandData;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "PremiunCarBrandDataEntity", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"brand", "variant", "subVariant"})
})
public class PremiunCarBrandDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "premiunBrandDataId", nullable = false)
    private Integer premiunBrandDataId;

    @Column(name = "brand")
    private String brand;

    @Column(name = "variant")
    private String variant;

    @Column(name = "subVariant")
    private String subVariant;

    public PremiunCarBrandDataEntity() {

    }
}
