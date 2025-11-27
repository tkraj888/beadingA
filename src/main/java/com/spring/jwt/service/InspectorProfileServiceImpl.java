package com.spring.jwt.service;

import com.spring.jwt.Interfaces.InspectorProfileService;
import com.spring.jwt.controller.PlaceBidController;
import com.spring.jwt.dto.InspectorProfileDto;
import com.spring.jwt.dto.PasswordChange;
import com.spring.jwt.entity.InspectorProfile;
import com.spring.jwt.entity.User;
import com.spring.jwt.entity.Userprofile;
import com.spring.jwt.exception.DuplicateRecordException;
import com.spring.jwt.exception.InvalidPasswordException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.repository.InspectorProfileRepo;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InspectorProfileServiceImpl implements InspectorProfileService {

    private final InspectorProfileRepo inspectorProfileRepo;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(InspectorProfileServiceImpl.class);



    @Override
    public String updateProfile(InspectorProfileDto inspectorProfileDto, Integer inspectorProfileId) {

        InspectorProfile profile = inspectorProfileRepo.findById(inspectorProfileId)
                .orElseThrow(() -> new UserNotFoundExceptions("Profile not found", HttpStatus.NOT_FOUND));

        if (inspectorProfileDto.getAddress() != null) {
            profile.setAddress(inspectorProfileDto.getAddress());
        }
        if (inspectorProfileDto.getCity() != null) {
            profile.setCity(inspectorProfileDto.getCity());
        }
        if (inspectorProfileDto.getFirstName() != null) {
            profile.setFirstName(inspectorProfileDto.getFirstName());
        }
        if (inspectorProfileDto.getLastName() != null) {
            profile.setLastName(inspectorProfileDto.getLastName());
        }
        if (inspectorProfileDto.isStatusProvided()) {
            profile.setStatus(inspectorProfileDto.getStatus());
        }

        inspectorProfileRepo.save(profile);

        User user = profile.getUser();
        if (user != null) {
            if (inspectorProfileDto.getMobileNo() != null && !inspectorProfileDto.getMobileNo().isEmpty()) {
                if (!inspectorProfileDto.getMobileNo().equals(user.getMobileNo())) {
                    boolean mobileExists = userRepository.existsByMobileNo(inspectorProfileDto.getMobileNo());
                    if (mobileExists) {
                        throw new DuplicateRecordException("The Mobile Number you entered is already in use. Please try another one", HttpStatus.CONFLICT);
                    }
                    user.setMobileNo(inspectorProfileDto.getMobileNo());
                }
            }

            if (inspectorProfileDto.getEmail() != null && !inspectorProfileDto.getEmail().isEmpty()) {
                if (!inspectorProfileDto.getEmail().equals(user.getEmail())) {
                    boolean emailExists = userRepository.existsByEmail(inspectorProfileDto.getEmail());
                    if (emailExists) {
                        throw new DuplicateRecordException("The email address you entered is already in use. Please try another one", HttpStatus.CONFLICT);
                    }
                    user.setEmail(inspectorProfileDto.getEmail());
                }
            }

            userRepository.save(user);
        }

        return "Profile fields updated successfully";
    }

    @Override
    public InspectorProfileDto getProfileById(Integer inspectorProfileId) {
        Optional<InspectorProfile> profileOptional = inspectorProfileRepo.findById(inspectorProfileId);
        if (profileOptional.isPresent()) {
            InspectorProfile profile = profileOptional.get();
            User user = profile.getUser();

            InspectorProfileDto profileDto = new InspectorProfileDto();
            profileDto.setAddress(profile.getAddress());
            profileDto.setCity(profile.getCity());
            profileDto.setFirstName(profile.getFirstName());
            Boolean status = profile.getStatus();
            if (status != null) {
                profileDto.setStatus(status);
            }
            profileDto.setLastName(profile.getLastName());

            if (user != null) {
                profileDto.setEmail(user.getEmail());
                profileDto.setMobileNo(user.getMobileNo());
            }

            return profileDto;
        } else {
            throw new UserNotFoundExceptions("Profile Not Found");
        }
    }

    @Override
    public String deleteProfile(Integer inspectorProfileId) {
        InspectorProfile profiles = inspectorProfileRepo.findById(inspectorProfileId).orElseThrow(() -> new UserNotFoundExceptions("Profile Not Found"));
        inspectorProfileRepo.deleteById(inspectorProfileId);
        User user = profiles.getUser();
        user.getRoles().clear();
        userRepository.deleteById(user.getId());


        return "Profile deleted successfully";
    }

    @Override
    public InspectorProfileDto getProfileByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundExceptions("User not found"));

        InspectorProfile inspectorProfile = user.getInspectorProfile();
        if (inspectorProfile == null) {
            throw new RuntimeException("Inspector profile not found for user with id: " + userId);
        }
        InspectorProfileDto inspectorProfileDto = new InspectorProfileDto();
        inspectorProfileDto.setAddress(inspectorProfile.getAddress());
        inspectorProfileDto.setCity(inspectorProfile.getCity());
        inspectorProfileDto.setFirstName(inspectorProfile.getFirstName());
        inspectorProfileDto.setLastName(inspectorProfile.getLastName());
        inspectorProfileDto.setStatus(inspectorProfile.getStatus());
        inspectorProfileDto.setEmail(user.getEmail());
        inspectorProfileDto.setMobileNo(user.getMobileNo());

        return inspectorProfileDto;
    }

    @Override
    public Page<InspectorProfileDto> getAllProfiles(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        Page<InspectorProfile> allProfiles = inspectorProfileRepo.findAll(pageable);

        if (allProfiles.isEmpty()) {
            throw new PageNotFoundException("No Data Found");
        }

        List<InspectorProfileDto> profileDtoList = allProfiles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(profileDtoList, pageable, allProfiles.getTotalElements());
    }

    @Override
    public BaseResponseDTO changePassword(int id, PasswordChange passwordChange) {
        BaseResponseDTO response = new BaseResponseDTO();

        Optional<InspectorProfile> userOptional = inspectorProfileRepo.findById(id);

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

    private InspectorProfileDto convertToDto(InspectorProfile inspectorProfile) {
        InspectorProfileDto dto = new InspectorProfileDto();
        dto.setInspectorProfileId(inspectorProfile.getId());
        dto.setAddress(inspectorProfile.getAddress());
        dto.setCity(inspectorProfile.getCity());
        dto.setFirstName(inspectorProfile.getFirstName());
        dto.setLastName(inspectorProfile.getLastName());
        Boolean status = inspectorProfile.getStatus();
        if (status != null) {
            dto.setStatus(status);
        }
        User user = inspectorProfile.getUser();
        if (user != null) {
            dto.setUserId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setMobileNo(user.getMobileNo());
        }
        return dto;

    }
}