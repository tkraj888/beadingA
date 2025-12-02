package com.spring.jwt.Bidding.Service;

import com.spring.jwt.Bidding.DTO.SalesPersonDto;
import com.spring.jwt.Bidding.Interface.SalesPersonService;
import com.spring.jwt.Bidding.Repository.SalesPersonRepository;
import com.spring.jwt.dto.PasswordChange;
import com.spring.jwt.entity.InspectorProfile;
import com.spring.jwt.entity.SalesPerson;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.DuplicateRecordException;
import com.spring.jwt.exception.InvalidPasswordException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesPersonServiceImpl implements SalesPersonService {

    private final SalesPersonRepository salesPersonRepository;

    private final UserRepository userRepository;

    private final DealerRepository dealerRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(SalesPersonServiceImpl.class);

    @Override
    public SalesPersonDto getProfileByUserId(Integer userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundExceptions("User not found"));

        SalesPerson salesPerson = user.getSalesPerson();
        if (salesPerson == null) {
            throw new RuntimeException( "Sales Person not found for user " + userId);
        }


        return convertToDto(salesPerson);
    }

    @Override
    public Page<SalesPersonDto> getAllProfiles(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("salesPersonId").descending());
        Page<SalesPerson> allProfiles = salesPersonRepository.findAll(pageable);

        int totalPages = (int) Math.ceil((double) allProfiles.getTotalElements() / pageSize);

        if (pageNo >= totalPages) {
            throw new PageNotFoundException("Page number " + pageNo + " exceeds available pages");
        }

        if (!allProfiles.hasContent()) {
            throw new RuntimeException("No data found for page: " + pageNo);
        }

        List<SalesPersonDto> all = allProfiles.stream()
                .map(salesPerson -> {
                    SalesPersonDto dto = convertToDto(salesPerson);

                    int dealerCount = dealerRepository.countBySalesPersonId(salesPerson.getUser().getId());
                    dto.setTotalAddedDealers(dealerCount);

                    return dto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(all, pageable, allProfiles.getTotalElements());
    }

    @Override
    public String updateProfile(SalesPersonDto salesPersonDto, Integer salesPersonId) {
        SalesPerson profile = salesPersonRepository.findById(salesPersonId).orElseThrow(() -> new UserNotFoundExceptions("Profile not found", HttpStatus.NOT_FOUND));

        if (salesPersonDto.getAddress() != null && !salesPersonDto.getAddress().isEmpty()) {
            profile.setAddress(salesPersonDto.getAddress());
        }
        if (salesPersonDto.getCity() != null && !salesPersonDto.getCity().isEmpty()) {
            profile.setCity(salesPersonDto.getCity());
        }
        if (salesPersonDto.getFirstName() != null &&!salesPersonDto.getFirstName().isEmpty()) {
            profile.setFirstName(salesPersonDto.getFirstName());
        }
        if (salesPersonDto.getLastName() != null && !salesPersonDto.getLastName().isEmpty()) {
            profile.setLastName(salesPersonDto.getLastName());
        }
        if (salesPersonDto.getArea() != null && !salesPersonDto.getArea().isEmpty()) {
            profile.setArea(salesPersonDto.getArea());
        }
        if (salesPersonDto.getStatus() != null) {
            profile.setStatus(salesPersonDto.getStatus());
        }
        salesPersonRepository.save(profile);

        User user = profile.getUser();
            if (user != null) {
                if (salesPersonDto.getMobileNo() != null && !salesPersonDto.getMobileNo().isEmpty()) {
                    if (!salesPersonDto.getMobileNo().equals(user.getMobileNo())) {
                        boolean mobileExists = userRepository.existsByMobileNo(salesPersonDto.getMobileNo());
                        if (mobileExists) {
                            throw new DuplicateRecordException("The Mobile Number you entered is already in use. Please try another one", HttpStatus.CONFLICT);
                        }
                        user.setMobileNo(salesPersonDto.getMobileNo());
                    }
                }

                if (salesPersonDto.getEmail() != null && !salesPersonDto.getEmail().isEmpty()) {
                    if (!salesPersonDto.getEmail().equals(user.getEmail())) {
                        boolean emailExists = userRepository.existsByEmail(salesPersonDto.getEmail());
                        if (emailExists) {
                            throw new DuplicateRecordException("The email address you entered is already in use. Please try another one", HttpStatus.CONFLICT);
                        }
                        user.setEmail(salesPersonDto.getEmail());
                    }

            }
            userRepository.save(user);
        }

        return "Profile fields updated successfully";
    }

    @Override
    public String deleteProfile(Integer inspectorProfileId) {
        SalesPerson profiles = salesPersonRepository.findById(inspectorProfileId).orElseThrow(() -> new UserNotFoundExceptions("Profile Not Found"));
        salesPersonRepository.deleteById(inspectorProfileId);
        User user = profiles.getUser();
        user.getRoles().clear();
        userRepository.deleteById(user.getId());
        return "Profile deleted successfully";
    }

    @Override
    public BaseResponseDTO changePassword(int id, PasswordChange passwordChange) {
        BaseResponseDTO response = new BaseResponseDTO();

        Optional<SalesPerson> userOptional = salesPersonRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get().getUser();
            logger.info("User found: {}", user);

            if (passwordEncoder.matches(passwordChange.getOldPassword(), user.getPassword())) {
                logger.info("Old password matches.");

                if (passwordChange.getNewPassword().equals(passwordChange.getConfirmPassword())) {
                    logger.info("New password and confirm password match.");

                    user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
                    userRepository.save(user);

                    response.setCode(String.valueOf(HttpStatus.OK.value()));
                    response.setMessage("Password changed");
                } else {
                    response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                    logger.error("New password and confirm password do not match.");
                    throw new InvalidPasswordException("New password and confirm password does not match");
                }
            } else {
                response.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
                logger.error("Old password does not match.");
                throw new InvalidPasswordException("Invalid Password");
            }

        } else {
            logger.error("User not found.");
            throw new UserNotFoundExceptions("No user found");
        }
        return response;
    }



    private SalesPersonDto convertToDto(SalesPerson salesPerson) {
        SalesPersonDto dto = new SalesPersonDto();
        dto.setSalesPersonId(salesPerson.getSalesPersonId());
        dto.setAddress(salesPerson.getAddress());
        dto.setCity(salesPerson.getCity());
        dto.setFirstName(salesPerson.getFirstName());
        dto.setLastName(salesPerson.getLastName());
        dto.setArea(salesPerson.getArea());
        dto.setDocumentId(salesPerson.getDocumentId());
        dto.setJoiningdate(salesPerson.getJoiningdate());
        dto.setStatus(salesPerson.getStatus());

        User user = salesPerson.getUser();
        if (user != null) {
            dto.setUserId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setMobileNo(user.getMobileNo());

        }
        return dto;
    }
}
