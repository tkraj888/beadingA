package  com.spring.jwt.enquiry;


public class EnquiryMapper {

    public static Enquiry toEntity(EnquiryDTO dto) {

        Enquiry entity = new Enquiry();
        entity.setEnquiryId(dto.getEnquiryId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setMobileNumber(dto.getMobileNumber());
        entity.setAddress(dto.getAddress());
        entity.setMessage(dto.getMessage());
        entity.setDateTime(dto.getDateTime());
        entity.setType(dto.getType());

        return entity;
    }

    public static EnquiryDTO toDto(Enquiry entity) {

        EnquiryDTO dto = new EnquiryDTO();
        dto.setEnquiryId(entity.getEnquiryId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setMobileNumber(entity.getMobileNumber());
        dto.setAddress(entity.getAddress());
        dto.setMessage(entity.getMessage());
        dto.setDateTime(entity.getDateTime());
        dto.setType(entity.getType());

        return dto;
    }
}
