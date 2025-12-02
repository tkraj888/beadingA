package com.spring.jwt.controller.DO;

import com.spring.jwt.Interfaces.ProfilePhotoService1;
import com.spring.jwt.dto.BidCarDto;
import com.spring.jwt.dto.ProfilePhotoDto;
import com.spring.jwt.dto.ResponceDto;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.entity.ProfilePhoto1;
import com.spring.jwt.service.DOService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ProfilePhoto")
@RequiredArgsConstructor
public class DoProfilePhotoController {
    @Autowired
    private ProfilePhotoService1 profilePhotoService1;

    @Value("${do.CDN.No}")
    private String CDNNo;
    private final String uploadDir = "uploads";
    private DOService doService = new DOService();
    //    private final String NODEJS_SERVER_URL = "https://digitaloceannodeservice.up.railway.app" ;
    private final String NODEJS_SERVER_URL = "https://digitaloceannodeimageservice-production.up.railway.app" ;

    private final RestTemplate restTemplate = new RestTemplate();


    @PostMapping("/add")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file, @RequestParam Integer userId) throws InvalidKeyException, NoSuchAlgorithmException {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = Paths.get(uploadDir, fileName);

            if (!Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }

            // Save the uploaded file to the specified directory
            file.transferTo(filePath);

            byte[] imageBytes = file.getBytes();
            Files.delete(filePath);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> payloadObject = new HashMap<>();
            payloadObject.put("imageBytes", imageBytes);
            payloadObject.put("contentType", file.getContentType());
            payloadObject.put("contentLength", imageBytes.length);
            String uniqueName = this.doService.generateRandomString(15) + fileName;
            payloadObject.put("imageName", uniqueName);
            if (uniqueName.isEmpty()) {
                throw new RuntimeException("profile photo not found");
            }
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payloadObject, httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(
                    NODEJS_SERVER_URL + "/forward-image",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            String serviceResponse = null;
            if (!response.getBody().isEmpty()) {
                ProfilePhotoDto profilePhotoDto= new ProfilePhotoDto();
                profilePhotoDto.setUserId(userId);

                profilePhotoDto.setDocumentLink(CDNNo + "/" + response.getBody());
                serviceResponse = profilePhotoService1.addPPhoto(profilePhotoDto);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponceDto("success", serviceResponse));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", String.valueOf(e)));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponceDto("unsuccess", "Failed to upload image"));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponceDto("unsuccess", "Failed to upload image"));

        }

    }

    @DeleteMapping("/deletebyuserid")
    public ResponseEntity<?> deleteProfilePhoto(@RequestParam Integer userId) {
        try {
            String responseMessage = profilePhotoService1.deleteById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success", responseMessage));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "Profile photo not found for user ID: " + userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "Failed to delete profile photo"));
        }
    }

    @GetMapping("/getbyuserid")
    public ResponseEntity<?> getProfilePhotoByUserId(@RequestParam Integer userId) {
        try {
            ProfilePhotoDto profilePhoto = (ProfilePhotoDto) profilePhotoService1.getByUserId(userId);
            if (profilePhoto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "Profile photo not found for user ID: " + userId));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponceDto("success", profilePhoto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "Failed to retrieve profile photo"));
        }
    }


}
