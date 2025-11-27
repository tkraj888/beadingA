package com.spring.jwt.userForm.Interface;

import com.spring.jwt.userForm.Dto.userFormDto;
import com.spring.jwt.userForm.Dto.userFormDtoPost;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface userFormService {

    userFormDtoPost addForm (userFormDtoPost userFormDtoPost);

    userFormDto getByFormId(Integer userFormId);

    userFormDto updateForm(Integer userFormId, userFormDto userFormDto);

    public userFormDto updateFormStatus(Integer userFormId, userFormDto userFormDto);

    void deleteForm(Integer userFormId);

    public List<userFormDto> getAllForms(int page, int size);

    public List<userFormDto> getByUserId(Integer userId);

    public List<userFormDto> getBySalesPersonId(Integer salesPersonId, int page, int size);

    public List<userFormDto> getByInspectorId(Integer inspectorId , int page, int size);

    public List<userFormDto> getByStatus(String Status);


}
