package com.spring.jwt.Bidding.Controller;

import com.spring.jwt.Bidding.DTO.AllSalesPersonDto;
import com.spring.jwt.Bidding.DTO.SalesPersonDto;
import com.spring.jwt.Bidding.DTO.SingleSalesPersonDto;
import com.spring.jwt.Bidding.Interface.SalesPersonService;
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

@RestController
@RequestMapping("/salesPerson")
@RequiredArgsConstructor
public class SalesPersonController {
    private final SalesPersonService salesPersonService;


    @GetMapping("/getByUserId")
    public ResponseEntity<?> getbyUserId(@RequestParam Integer userId) {
        try {
            SingleSalesPersonDto singleProfileDto = new SingleSalesPersonDto("Success");
            singleProfileDto.setResponse(salesPersonService.getProfileByUserId(userId));
            return ResponseEntity.status(HttpStatus.OK).body(singleProfileDto);
        } catch (UserNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("unSuccess", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("unSuccess", e.getMessage()));
        }
    }

    @PutMapping("/passwordChange/{id}")
    public ResponseEntity<BaseResponseDTO> changePassword(@PathVariable int id, @RequestBody PasswordChange passwordChange){

        try{
            BaseResponseDTO result = salesPersonService.changePassword(id,passwordChange);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Successful",result.getMessage()));
        }catch (UserNotFoundExceptions exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessfully","UserNotFoundException"));
        } catch (InvalidPasswordException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessfully","InvalidPasswordException"));
        }
    }

    @GetMapping("/GetAllInspProfiles")
    public ResponseEntity<?> getAllInspProfiles(@RequestParam(value = "pageNo") int pageNo,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            Page<SalesPersonDto> allProfilesPage = salesPersonService.getAllProfiles(pageNo, pageSize);
            AllSalesPersonDto profilesDTO = new AllSalesPersonDto("Success");
            profilesDTO.setTotalPages(allProfilesPage.getTotalPages());
            profilesDTO.setList(allProfilesPage.getContent());
            return ResponseEntity.status(HttpStatus.OK).body(profilesDTO);
        } catch (PageNotFoundException e) {
            AllInspectorProfilesDTO profile = new AllInspectorProfilesDTO("Unsuccessful");
            profile.setException(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(profile);
        }
    }

  @PatchMapping("/updateSPersonDetails")
    public ResponseEntity<ResponseDto> updateProfile(@RequestBody SalesPersonDto salesPersonDto, @RequestParam Integer salesPersonId) {
        try {
            String result = salesPersonService.updateProfile(salesPersonDto, salesPersonId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success", result));
        } catch (UserNotFoundExceptions  profileNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("unsuccess", "Profile not found"));
        }catch (DuplicateRecordException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto("unsuccess", e.getMessage()));
        }
    }

    @DeleteMapping ("/deletById")
    public ResponseEntity<?> delete(@RequestParam  Integer salePersonId) throws UserNotFoundExceptions{
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Success",  salesPersonService.deleteProfile(salePersonId)));
        }catch (UserNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("unSuccess",e.getMessage()));
        }

    }

}
