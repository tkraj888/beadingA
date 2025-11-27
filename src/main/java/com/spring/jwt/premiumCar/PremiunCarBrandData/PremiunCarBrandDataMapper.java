package com.spring.jwt.premiumCar.PremiunCarBrandData;

public class PremiunCarBrandDataMapper {

    // DTO -> Entity
    public static PremiunCarBrandDataEntity toEntity(PremiunCarBrandDataDto dto) {
        PremiunCarBrandDataEntity entity = new PremiunCarBrandDataEntity();
        entity.setPremiunBrandDataId(dto.getPremiunBrandDataId());
        entity.setBrand(dto.getBrand());
        entity.setVariant(dto.getVariant());
        entity.setSubVariant(dto.getSubVariant());
        return entity;
    }

        // Entity → DTO
        public static PremiunCarBrandDataDto toDTO(PremiunCarBrandDataEntity entity) {

            PremiunCarBrandDataDto dto = new PremiunCarBrandDataDto();
            dto.setPremiunBrandDataId(entity.getPremiunBrandDataId());
            dto.setBrand(entity.getBrand());
            dto.setVariant(entity.getVariant());
            dto.setSubVariant(entity.getSubVariant());
            return dto;
        }
    }


