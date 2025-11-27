package com.spring.jwt.userForm.controller;

import com.spring.jwt.dto.ResponceDto;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.userForm.Dto.ResponseAllUserFormDto;
import com.spring.jwt.userForm.Dto.userFormDto;
import com.spring.jwt.userForm.Dto.userFormDtoPost;
import com.spring.jwt.userForm.exception.FormsNotFoundException;
import com.spring.jwt.userForm.service.userFormServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/userFormController")
public class userFormController {

    @Autowired
    private userFormServiceImpl userFormService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addForm(@RequestBody userFormDtoPost userFormDtoPost) {
        try {
            userFormDtoPost createdForm = userFormService.addForm(userFormDtoPost);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto("success", "Form created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_EXTENDED)
                    .body(new ResponseDto("unsuccess " + e.getMessage(), null));
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getByFormId(@RequestParam Integer userFormId) {
        try {
            userFormDto form = userFormService.getByFormId(userFormId);
            return ResponseEntity.ok(new ResponceDto("Form retrieved successfully", form));
        } catch (FormsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("unsuccessful", "Form not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("unsuccessful " + e.getMessage(), null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateForm(@RequestParam Integer userFormId, @RequestBody userFormDto userFormDto) {
        try {
            userFormDto updatedForm = userFormService.updateForm(userFormId, userFormDto);
            return ResponseEntity.ok(new ResponseDto(" success", "Form updated successfully"));
        } catch (FormsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("unsuccessful", "Form not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("unsuccessful " + e.getMessage(), null));
        }
    }

    @PatchMapping("/updateStatus")
    public ResponseEntity<?> updateFormwithStatus(@RequestParam Integer userFormId, @RequestBody userFormDto userFormDto) {
        try {
            userFormDto updatedForm = userFormService.updateFormStatus(userFormId, userFormDto);
            return ResponseEntity.ok(new ResponseDto("success","Form updated successfully"));
        } catch (FormsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("unsuccessful", "Form not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("unsuccessful " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteForm(@RequestParam Integer userFormId) {
        try {
            userFormService.deleteForm(userFormId);
            return ResponseEntity.ok(new ResponseDto("successfully", "Form deleted successfully"));
        } catch (FormsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("unsuccessful", "Form not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("unsuccessful " + e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseAllUserFormDto> getAllForms(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        try {
            List<userFormDto> forms = userFormService.getAllForms(page, size);
            ResponseAllUserFormDto response = new ResponseAllUserFormDto("Forms retrieved successfully");
            response.setList(forms);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseAllUserFormDto response = new ResponseAllUserFormDto("Unsuccessful");
            response.setException("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ResponseAllUserFormDto> getByUserId(@RequestParam Integer userId) {
        try {
            List<userFormDto> forms = userFormService.getByUserId(userId);
            ResponseAllUserFormDto response = new ResponseAllUserFormDto("Forms retrieved successfully");
            response.setList(forms);
            return ResponseEntity.ok(response);
        } catch (FormsNotFoundException e) {
            ResponseAllUserFormDto response = new ResponseAllUserFormDto("Unsuccessful");
            response.setException("Forms not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseAllUserFormDto response = new ResponseAllUserFormDto("Unsuccessful");
            response.setException("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/salesPerson")
    public ResponseEntity<ResponseAllUserFormDto> getBySalesPersonId(@RequestParam Integer salesPersonId,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        try {
            List<userFormDto> forms = userFormService.getBySalesPersonId(salesPersonId, page, size);
            ResponseAllUserFormDto response = new ResponseAllUserFormDto("Forms retrieved successfully");
            response.setList(forms);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseAllUserFormDto response = new ResponseAllUserFormDto("Unsuccessful");
            response.setException("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/inspector")
    public ResponseEntity<ResponseAllUserFormDto> getByInspectorId(@RequestParam Integer inspectorId,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        try {
            List<userFormDto> forms = userFormService.getByInspectorId(inspectorId, page, size);
            ResponseAllUserFormDto response = new ResponseAllUserFormDto("Forms retrieved successfully");
            response.setList(forms);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseAllUserFormDto response = new ResponseAllUserFormDto("Unsuccessful");
            response.setException("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<ResponseAllUserFormDto> getByStatus(@RequestParam String status) {
        try {
            List<userFormDto> forms = userFormService.getByStatus(status);
            ResponseAllUserFormDto response = new ResponseAllUserFormDto("Forms retrieved successfully");
            response.setList(forms);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseAllUserFormDto response = new ResponseAllUserFormDto("Unsuccessful");
            response.setException("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
