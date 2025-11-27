package com.spring.jwt.userForm.service;

import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.userForm.Dto.userFormDto;
import com.spring.jwt.userForm.Dto.userFormDtoPost;
import com.spring.jwt.userForm.Interface.userFormService;
import com.spring.jwt.userForm.entity.UserForm;
import com.spring.jwt.userForm.exception.FormsNotFoundException;
import com.spring.jwt.userForm.exception.ResourceNotFoundException;
import com.spring.jwt.userForm.exception.OperationFailedException;
import com.spring.jwt.userForm.repository.UserFormRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class userFormServiceImpl implements userFormService {

    @Autowired
    private UserFormRepo userFormRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public userFormDtoPost addForm(userFormDtoPost userFormDtoPost) {
        try {
            UserForm entity = convertDtoToEntity(userFormDtoPost);
            entity.setStatus("pending");
            entity = userFormRepository.save(entity);
            return convertEntityToPostDto(entity);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to add form");
        }
    }

    @Override
    public userFormDto getByFormId(Integer userFormId) {
        UserForm entity = userFormRepository.findById(userFormId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with ID: " + userFormId));
        return convertEntityToDto(entity);
    }

    @Override
    public userFormDto updateForm(Integer userFormId, userFormDto userFormDto) {
        UserForm entity = userFormRepository.findById(userFormId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with ID: " + userFormId));

        try {
            if (userFormDto.getCarOwnerName() != null) {
                entity.setCarOwnerName(userFormDto.getCarOwnerName());
            }
            if (userFormDto.getBrand() != null) {
                entity.setBrand(userFormDto.getBrand());
            }
            if (userFormDto.getModel() != null) {
                entity.setModel(userFormDto.getModel());
            }
            if (userFormDto.getVariant() != null) {
                entity.setVariant(userFormDto.getVariant());
            }
            if (userFormDto.getRegNo() != null) {
                entity.setRegNo(userFormDto.getRegNo());
            }
            if (userFormDto.getAddress1() != null) {
                entity.setAddress1(userFormDto.getAddress1());
            }
            if (userFormDto.getAddress2() != null) {
                entity.setAddress2(userFormDto.getAddress2());
            }
            if (userFormDto.getPinCode() != null) {
                entity.setPinCode(userFormDto.getPinCode());
            }
            if (userFormDto.getRc() != null) {
                entity.setRc(userFormDto.getRc());
            }
            if (userFormDto.getInspectionDate() != null) {
                entity.setInspectionDate(userFormDto.getInspectionDate());
            }
            if (userFormDto.getMobileNo() != null) {
                entity.setMobileNo(userFormDto.getMobileNo());
            }
            if (userFormDto.getCreatedTime() != null) {
                entity.setCreatedTime(userFormDto.getCreatedTime());
            }
//            if (userFormDto.getStatus() != null) {
//                entity.setStatus(userFormDto.getStatus());
//            }
//            if (userFormDto.getUserId() != null) {
//                entity.setUserId(userFormDto.getUserId());
//            }
            if (userFormDto.getSalesPersonId() != null) {
                entity.setSalesPersonId(userFormDto.getSalesPersonId());
            }
            if (userFormDto.getInspectorId() != null) {
                entity.setInspectorId(userFormDto.getInspectorId());
            }

            entity = userFormRepository.save(entity);
            return convertEntityToDto(entity);

        } catch (Exception e) {
            throw new OperationFailedException("Failed to patch form with ID: " + userFormId);
        }
    }


    @Override
    public userFormDto updateFormStatus(Integer userFormId, userFormDto userFormDto) {
            UserForm entity = userFormRepository.findById(userFormId)
                    .orElseThrow(() -> new ResourceNotFoundException("Form not found with ID: " + userFormId));

            try {
                // Check and update CarOwnerName, and set status to "In Progress"
                if (userFormDto.getCarOwnerName() != null) {
                    entity.setCarOwnerName(userFormDto.getCarOwnerName());

                }

                if (userFormDto.getBrand() != null) {
                    entity.setBrand(userFormDto.getBrand());
                }
                if (userFormDto.getModel() != null) {
                    entity.setModel(userFormDto.getModel());
                }
                if (userFormDto.getVariant() != null) {
                    entity.setVariant(userFormDto.getVariant());
                }
                if (userFormDto.getRegNo() != null) {
                    entity.setRegNo(userFormDto.getRegNo());
                }
                if (userFormDto.getAddress1() != null) {
                    entity.setAddress1(userFormDto.getAddress1());
                }
                if (userFormDto.getAddress2() != null) {
                    entity.setAddress2(userFormDto.getAddress2());
                }
                if (userFormDto.getPinCode() != null) {
                    entity.setPinCode(userFormDto.getPinCode());
                }
                if (userFormDto.getRc() != null) {
                    entity.setRc(userFormDto.getRc());
                }
                if (userFormDto.getInspectionDate() != null) {
                    entity.setInspectionDate(userFormDto.getInspectionDate());
                }
                if (userFormDto.getMobileNo() != null) {
                    entity.setMobileNo(userFormDto.getMobileNo());
                }
                if (userFormDto.getCreatedTime() != null) {
                    entity.setCreatedTime(userFormDto.getCreatedTime());
                }
                // Only set status if it's provided in userFormDto
                if (userFormDto.getStatus() != null) {
                    entity.setStatus(userFormDto.getStatus());
                }
                if (userFormDto.getSalesPersonId() != null) {
                    entity.setSalesPersonId(userFormDto.getSalesPersonId());
                }
                if (userFormDto.getInspectorId() != null) {
                    entity.setInspectorId(userFormDto.getInspectorId());
                    entity.setStatus("InProgress");
                }


                entity = userFormRepository.save(entity);
                return convertEntityToDto(entity);

            } catch (Exception e) {
                throw new OperationFailedException("Failed to patch form with ID: " + userFormId);
            }
        }

        @Override
    public void deleteForm(Integer userFormId) {
        UserForm entity = userFormRepository.findById(userFormId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with ID: " + userFormId));

        try {
            userFormRepository.delete(entity);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to delete form with ID: " + userFormId);
        }
    }

    @Override
    public List<userFormDto> getAllForms(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("userFormId").descending());
            Page<UserForm> entities = userFormRepository.findAllByOrderByUserFormIdDesc(pageable);
            return entities.stream().map(this::convertEntityToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new OperationFailedException("Failed to retrieve all forms");
        }
    }

//    @Override
//    public List<userFormDto> getByUserId(Integer userId) {
//        if (!userRepository.existsById(userId)) {
//            throw new UserNotFoundExceptions("User with ID " + userId + " not found.");
//        }
//        try {
//            if( )
//            {
//            List<UserForm> entities = userFormRepository.findByUserIdOrderByUserFormIdDesc(userId);
//            return entities.stream().map(this::convertEntityToDto).collect(Collectors.toList());}
//        } catch (Exception e) {
//            throw new OperationFailedException("Failed to retrieve forms by User ID: " + userId);
//        }
//    }
@Override
public List<userFormDto> getByUserId(Integer userId) {
    // Check if the user exists in the user repository
    if (!userRepository.existsById(userId)) {
        throw new UserNotFoundExceptions("User with ID " + userId + " not found.");
    }

    try {
        // Retrieve the list of forms by userId
        List<UserForm> entities = userFormRepository.findByUserIdOrderByUserFormIdDesc(userId);

        // If no forms are found, throw an exception
        if (entities.isEmpty()) {
            throw new FormsNotFoundException("No forms found for user with ID " + userId);
        }

        // Convert the forms to DTOs
        return entities.stream().map(this::convertEntityToDto).collect(Collectors.toList());

    } catch (Exception e) {
        throw new OperationFailedException("Failed to retrieve forms by User ID: " + userId);
    }
}


    @Override
    public List<userFormDto> getBySalesPersonId(Integer salesPersonId, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("userFormId").descending());
            Page<UserForm> entitiesPage = userFormRepository.findBySalesPersonIdOrderByUserFormIdDesc(salesPersonId, pageable);

            return entitiesPage.stream().map(this::convertEntityToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new OperationFailedException("Failed to retrieve forms by Sales Person ID: " + salesPersonId);
        }
    }

    @Override
    public List<userFormDto> getByInspectorId(Integer inspectorId, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("userFormId").descending());
            Page<UserForm> entitiesPage = userFormRepository.findByInspectorIdOrderByUserFormIdDesc(inspectorId, pageable);
            if (entitiesPage.isEmpty()) {
                throw new OperationFailedException("No forms found for Inspector ID: " + inspectorId);
            }
            return entitiesPage.getContent().stream().map(this::convertEntityToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new OperationFailedException("Failed to retrieve forms by Inspector ID: " + inspectorId + ". Error: " + e.getMessage());
        }
    }


    @Override
    public List<userFormDto> getByStatus(String status) {
        try {
            List<UserForm> entities = userFormRepository.findByStatusOrderByUserFormIdDesc(status);
            return entities.stream().map(this::convertEntityToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new OperationFailedException("Failed to retrieve forms by Status: " + status);
        }
    }

    private UserForm convertDtoToEntity(userFormDtoPost dto) {
        UserForm entity = new UserForm();
        entity.setUserFormId(dto.getUserFormId());
        entity.setCarOwnerName(dto.getCarOwnerName());
        entity.setBrand(dto.getBrand());
        entity.setModel(dto.getModel());
        entity.setVariant(dto.getVariant());
        entity.setRegNo(dto.getRegNo());
        entity.setAddress1(dto.getAddress1());
        entity.setAddress2(dto.getAddress2());
        entity.setPinCode(dto.getPinCode());
        entity.setRc(dto.getRc());
        entity.setInspectionDate(dto.getInspectionDate());
        entity.setMobileNo(dto.getMobileNo());
        entity.setCreatedTime(dto.getCreatedTime());
        entity.setStatus(dto.getStatus());
        entity.setUserId(dto.getUserId());
        // SalesPersonId and InspectorId are not present in userFormDtoPost
        return entity;
    }

    private userFormDto convertEntityToDto(UserForm entity) {
        userFormDto dto = new userFormDto();
        dto.setUserFormId(entity.getUserFormId());
        dto.setCarOwnerName(entity.getCarOwnerName());
        dto.setBrand(entity.getBrand());
        dto.setModel(entity.getModel());
        dto.setVariant(entity.getVariant());
        dto.setRegNo(entity.getRegNo());
        dto.setAddress1(entity.getAddress1());
        dto.setAddress2(entity.getAddress2());
        dto.setPinCode(entity.getPinCode());
        dto.setRc(entity.getRc());
        dto.setMobileNo(entity.getMobileNo());
        dto.setInspectionDate(entity.getInspectionDate());
        dto.setCreatedTime(entity.getCreatedTime());
        dto.setStatus(entity.getStatus());
        dto.setUserId(entity.getUserId());
        dto.setSalesPersonId(entity.getSalesPersonId());
        dto.setInspectorId(entity.getInspectorId());
        return dto;
    }

    private userFormDtoPost convertEntityToPostDto(UserForm entity) {
        userFormDtoPost dto = new userFormDtoPost();
        dto.setUserFormId(entity.getUserFormId());
        dto.setCarOwnerName(entity.getCarOwnerName());
        dto.setBrand(entity.getBrand());
        dto.setModel(entity.getModel());
        dto.setVariant(entity.getVariant());
        dto.setRegNo(entity.getRegNo());
        dto.setAddress1(entity.getAddress1());
        dto.setAddress2(entity.getAddress2());
        dto.setPinCode(entity.getPinCode());
        dto.setRc(entity.getRc());
        dto.setMobileNo(entity.getMobileNo());
        dto.setInspectionDate(entity.getInspectionDate());
        dto.setCreatedTime(entity.getCreatedTime());
        dto.setStatus(entity.getStatus());
        dto.setUserId(entity.getUserId());
        // SalesPersonId and InspectorId are not required in userFormDtoPost
        return dto;
    }

}
