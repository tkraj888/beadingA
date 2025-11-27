package  com.spring.jwt.enquiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnquiryServiceImpl implements EnquiryService{

    @Autowired
    EnquiryRepo enquiryRepository;

    @Override
    public EnquiryDTO createEnquiry(EnquiryDTO enquiry) {
        // Step 1: Convert DTO → Entity
        Enquiry entity = EnquiryMapper.toEntity(enquiry);

        entity.setDateTime(LocalDateTime.now());
        // Step 2: Save entity in DB
        Enquiry savedEntity = enquiryRepository.save(entity);

        // Step 3: Convert Entity → DTO
        return EnquiryMapper.toDto(savedEntity);
    }

    @Override
    public Enquiry getById(Integer id) {
        return enquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enquiry not found with ID: " + id));
    }

    @Override
    public List<Enquiry> getAll() {
        return enquiryRepository.findAll();
    }

    @Override
    public Enquiry updateById(Integer id, EnquiryDTO enquiryDTO) {
        // Find existing record
        Enquiry existingEnquiry = enquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enquiry not found with ID: " + id));

        // Update fields (manual or via mapper)
        existingEnquiry.setEnquiryId(enquiryDTO.getEnquiryId());
        existingEnquiry.setName(enquiryDTO.getName());
        existingEnquiry.setEmail(enquiryDTO.getEmail());
        existingEnquiry.setAddress(enquiryDTO.getAddress());
        existingEnquiry.setMessage(enquiryDTO.getMessage());
        existingEnquiry.setMobileNumber(enquiryDTO.getMobileNumber());
        existingEnquiry.setType(enquiryDTO.getType());

        existingEnquiry.setDateTime(LocalDateTime.now());
        // Save updated record
        return enquiryRepository.save(existingEnquiry);
    }

    @Override
    public void deleteById(Integer id) {
        if (!enquiryRepository.existsById(id)) {
            throw new RuntimeException("Enquiry not found with ID: " + id);
        }
        enquiryRepository.deleteById(id);
    }

    }

