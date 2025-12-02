package com.spring.jwt.controller;

import com.spring.jwt.Interfaces.InspectorProfileService;
import com.spring.jwt.dto.*;
import com.spring.jwt.exception.DuplicateRecordException;
import com.spring.jwt.exception.InvalidPasswordException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/ispProfile")
@RequiredArgsConstructor
public class InspectorProfileController {

    private final InspectorProfileService inspectorProfileService;

    @PatchMapping("/update")
    public ResponseEntity<ResponseDto> updateProfile(@RequestBody InspectorProfileDto inspectorProfileDto, @RequestParam Integer inspectorProfileId) {
        try {
            if (inspectorProfileDto.getStatus() != null) {
                inspectorProfileDto.setStatusProvided(true);
            }
            String result = inspectorProfileService.updateProfile(inspectorProfileDto, inspectorProfileId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success", result));
        } catch (UserNotFoundExceptions  profileNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("unsuccess", "Profile not found"));
        }catch (DuplicateRecordException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto("unsuccess", e.getMessage()));
        }
    }

    @GetMapping("/inspector")
    public ResponseEntity<?> getByProfile(@RequestParam Integer inspectorProfileId) {
        try {
            SingleProfileDto singleProfileDto = new SingleProfileDto("Success");
            singleProfileDto.setResponse(inspectorProfileService.getProfileById(inspectorProfileId));
            return ResponseEntity.status(HttpStatus.OK).body(singleProfileDto);
        }catch (UserNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("unSuccess",e.getMessage()));
        }
    }
    @DeleteMapping ("/deletById")
    public ResponseEntity<?> delete(@RequestParam  Integer inspectorProfileId) throws UserNotFoundExceptions{
        try {
          return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Success",  inspectorProfileService.deleteProfile(inspectorProfileId)));
        }catch (UserNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("unSuccess",e.getMessage()));
        }

}

    @GetMapping("/getByUserId")
    public ResponseEntity<?> getbyUserId(@RequestParam Integer userId) {
    try {
        SingleProfileDto singleProfileDto = new SingleProfileDto("Success");
        singleProfileDto.setResponse(inspectorProfileService.getProfileByUserId(userId));
        return ResponseEntity.status(HttpStatus.OK).body(singleProfileDto);
    } catch (UserNotFoundExceptions e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("unSuccess", e.getMessage()));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("unSuccess", e.getMessage()));
    }
}

    @GetMapping("/GetAllInspProfiles")
    public ResponseEntity<?> getAllInspProfiles(@RequestParam(value = "pageNo") int pageNo,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            Page<InspectorProfileDto> allProfilesPage = inspectorProfileService.getAllProfiles(pageNo, pageSize);
            AllInspectorProfilesDTO profilesDTO = new AllInspectorProfilesDTO("Success");
            profilesDTO.setTotalPages(allProfilesPage.getTotalPages());
            profilesDTO.setList(allProfilesPage.getContent());
            return ResponseEntity.status(HttpStatus.OK).body(profilesDTO);
        } catch (PageNotFoundException e) {
            AllInspectorProfilesDTO profile = new AllInspectorProfilesDTO("Unsuccessful");
            profile.setException(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(profile);
        }
    }

    @PutMapping("/ispChangePassword/{id}")
    public ResponseEntity<BaseResponseDTO> changePassword(@PathVariable int id, @RequestBody PasswordChange passwordChange){

        try{
            BaseResponseDTO result =inspectorProfileService.changePassword(id,passwordChange);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Successful",result.getMessage()));
        }catch (UserNotFoundExceptions exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessfully","UserNotFoundException"));
        } catch (InvalidPasswordException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessfully","InvalidPasswordException"));
        }
    }


}


