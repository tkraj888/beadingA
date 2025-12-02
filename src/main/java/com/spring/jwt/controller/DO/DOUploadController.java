package com.spring.jwt.controller.DO;

import com.spring.jwt.Interfaces.IDocument;
import com.spring.jwt.controller.S3Controller.MultipartInputStreamFileResource;
import com.spring.jwt.dto.DocumentDto;
import com.spring.jwt.dto.ResponceDto;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.service.DOService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
import java.util.Random;

@RestController
@RequestMapping("/uploadFile")
@RequiredArgsConstructor
public class DOUploadController {

    private final R2StorageService r2StorageService;
    private final IDocument iDocument;

    @Value("${cloudflare.r2.account-id}")
    private String accountId;

    @Value("${cloudflare.r2.bucket}")
    private String bucketName;

    /**
     * Uploads file directly to Cloudflare R2 and saves metadata
     */
    @PostMapping("/add")
    public ResponseEntity<ResponceDto> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam String documentType,
            @RequestParam Integer userId,
            @RequestParam Integer carId) {

        System.out.println("File size: " + file.getSize());

        try {
            // 1. Upload to R2 bucket
            String key = r2StorageService.uploadFile(file);

            // 2. Build public link (R2 public URL format)
            String documentLink = "https://photos.caryanamindia.com/"  + key;

            // 3. Save metadata in DB
            DocumentDto documentDto = new DocumentDto();
            documentDto.setUserId(userId);
            documentDto.setCarId(carId);
            documentDto.setDocumentType(documentType);
            documentDto.setDocumentLink(documentLink);

            String serviceResponse = iDocument.addDocument(documentDto);

            return ResponseEntity.ok(new ResponceDto("success", serviceResponse));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponceDto("unsuccess", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceDto("unsuccess", "Failed to upload image: " + e.getMessage()));
        }
    }
//    @PostMapping("/add")
//    public ResponseEntity<?> uploadImage(
//            @RequestParam("image") MultipartFile file,
//            @RequestParam String documentType,
//            @RequestParam Integer userId,
//            @RequestParam Integer carId) {
//        System.out.println("File size: " + file.getSize());
//
//        try {
//            // 1. Prepare the file as a Resource to actually send the file content
//            ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
//                @Override
//                public String getFilename() {
//                    return file.getOriginalFilename();
//                }
//            };
//
//            // 2. Prepare the body for multipart/form-data
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            body.add("file", fileAsResource); // must be the actual file content!
//            body.add("key", file.getOriginalFilename());
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//            RestTemplate restTemplate = new RestTemplate();
//            String url = "https://images.prodchunca.in.net/api/r2/upload";
//
//            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
//
//            String serviceResponse = null;
//            if (response.getBody() != null && !response.getBody().isEmpty()) {
//                DocumentDto documentDto = new DocumentDto();
//                documentDto.setUserId(userId);
//                documentDto.setCarId(carId);
//                documentDto.setDocumentType(documentType);
//                // You may need to adjust link based on actual API response
//                documentDto.setDocumentLink("https://pub-c4cb464ddd9146d7bc0bf3b4262771ac.r2.dev/caryanamindia/" + file.getOriginalFilename());
//                serviceResponse = iDocument.addDocument(documentDto);
//            }
//
//            return ResponseEntity.status(HttpStatus.OK).body(new ResponceDto("success", serviceResponse));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", String.valueOf(e)));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponceDto("unsuccess", "Failed to upload image"));
//        }
//    }

    @DeleteMapping("/deleteDocumentId")
    private ResponseEntity<?> delete(@RequestParam Integer DocumentId) {
        try {
            String documents =iDocument.deleteById(DocumentId);
            ResponseDto responceDto = new ResponseDto("success",documents);
            return ResponseEntity.status(HttpStatus.OK).body(responceDto);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", String.valueOf(e)));

        }
    }

    @GetMapping("/getDocuments")
    private ResponseEntity<?> getDocumentByUserIdAndDocId(@RequestParam Integer userId, @RequestParam String DocumentType) {
        try {
            Object documents = iDocument.getAllDocument(userId, DocumentType);
            ResponceDto responceDto = new ResponceDto("success",documents);
            return ResponseEntity.status(HttpStatus.OK).body(responceDto);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", String.valueOf(e)));

        }
    }

    @GetMapping("/getByUserId")
    private ResponseEntity<?> getByUserId(@RequestParam Integer userId) {
        try {
            Object documents =iDocument.getByUserId(userId);
            ResponceDto responceDto = new ResponceDto("success",documents);
            return ResponseEntity.status(HttpStatus.OK).body(responceDto);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", String.valueOf(e)));

        }
    }
    @DeleteMapping("/deleteCarId")
    private ResponseEntity<?> deleteCar(@RequestParam Integer carId) {
        try {
            Object documents =iDocument.delete(carId);

            return ResponseEntity.status(HttpStatus.OK).body(documents);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", String.valueOf(e)));

        }
    }
    @GetMapping("/getByCarID")
    private ResponseEntity<?> getByCarID(@RequestParam Integer carId) {
        try {
            Object documents =iDocument.getByCarID(carId);
            ResponceDto responceDto = new ResponceDto("success",documents);
            return ResponseEntity.status(HttpStatus.OK).body(responceDto);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", String.valueOf(e)));

        }
    }
    @GetMapping("/getCarIdType")
    private ResponseEntity<?> getCarIdType(@RequestParam Integer carId,@RequestParam String docType) {
        try {
            Object documents =iDocument.getCarIdType(carId,docType);
            ResponceDto responceDto = new ResponceDto("success",documents);
            return ResponseEntity.status(HttpStatus.OK).body(responceDto);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", String.valueOf(e)));

        }
    }
}