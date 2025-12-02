package com.spring.jwt.controller.DO;

import com.spring.jwt.Interfaces.IBidPhoto;
import com.spring.jwt.dto.BidCarDto;
import com.spring.jwt.dto.ResponceDto;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.repository.IBidDoc;
import com.spring.jwt.service.DOService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/uploadFileBidCar")
@RequiredArgsConstructor
public class DOBidCarUploadController {

    private final R2StorageService r2StorageService;
    private final IBidPhoto iDocument;
    @Value("${cloudflare.r2.account-id}")
    private String accountId;

    @Value("${cloudflare.r2.bucket}")
    private String bucketName;
    @Value("${do.CDN.No}")
    private String CDNNo;
    private final String uploadDir = "uploads";
    private DOService doService = new DOService();
    //    private final String NODEJS_SERVER_URL = "https://digitaloceannodeservice.up.railway.app" ;
    private final String NODEJS_SERVER_URL = "https://digitaloceannodeimageservice-production.up.railway.app" ;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/addWithoutPhoto")
    public ResponseEntity<?> uploadWithoutImage(@RequestParam String documentType,@RequestParam String doc,@RequestParam String doctype,@RequestParam String subtype,@RequestParam String comment,@RequestParam Integer beadingCarId) throws InvalidKeyException, NoSuchAlgorithmException {
        try {
            String serviceResponse = null;

              BidCarDto documentDto = new BidCarDto();
                documentDto.setComment(comment);
                documentDto.setDoctype(doctype);
                documentDto.setSubtype(subtype);
                documentDto.setDoc(doc);
                documentDto.setBeadingCarId(beadingCarId);
                documentDto.setDocumentType(documentType);

                serviceResponse = iDocument.addDocument(documentDto);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponceDto("success", serviceResponse));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", String.valueOf(e)));
        }  catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponceDto("unsuccess", "Failed to upload image"));

        }

    }
    @PostMapping("/add")
    public ResponseEntity<ResponceDto> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam String documentType,
            @RequestParam String doc,
            @RequestParam String doctype,
            @RequestParam String subtype,
            @RequestParam String comment,
            @RequestParam Integer beadingCarId) {

        System.out.println("Uploading image for car ID: " + beadingCarId + ", size: " + file.getSize());

        try {
            // 1. Upload to R2 bucket
            String key = r2StorageService.uploadFile(file);

            // 2. Build public link (R2 public URL format)
            String documentLink = "https://photos.caryanamindia.com/" + key;

            // 3. Create DTO and populate all fields
            BidCarDto documentDto = new BidCarDto();
            documentDto.setComment(comment);
            documentDto.setDoctype(doctype);
            documentDto.setSubtype(subtype);
            documentDto.setDoc(doc);
            documentDto.setBeadingCarId(beadingCarId);
            documentDto.setDocumentType(documentType);
            documentDto.setDocumentLink(documentLink);

            // 4. Save metadata in DB
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

    @DeleteMapping("/delete")
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
    private ResponseEntity<?> getDocumentByUserIdAndDocId(@RequestParam Integer beadingCarId, @RequestParam String DocumentType) {
        try {
            Object documents = iDocument.getByDocumentType(beadingCarId, DocumentType);
            ResponceDto responceDto = new ResponceDto("success",documents);
            return ResponseEntity.status(HttpStatus.OK).body(responceDto);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", String.valueOf(e)));

        }
    }

    @GetMapping("/getById")
    private ResponseEntity<?> getById(@RequestParam Integer documentId) {
        try {
            Object documents =iDocument.getById(documentId);
            ResponceDto responceDto = new ResponceDto("success",documents);
            return ResponseEntity.status(HttpStatus.OK).body(responceDto);
        } catch (Exception e) {


            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", String.valueOf(e)));

        }
    }

    @GetMapping("/getByBidCarID")
    private ResponseEntity<?> getByBidCarID(@RequestParam Integer beadingCarId) {
        try {
            Object documents =iDocument.getByBidCarID(beadingCarId);
            ResponceDto responceDto = new ResponceDto("success",documents);
            return ResponseEntity.status(HttpStatus.OK).body(responceDto);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", String.valueOf(e)));

        }
    }
    @GetMapping("/getBidCarIdType")
    private ResponseEntity<?> getBidCarIdType(@RequestParam Integer beadingCarId,@RequestParam String docType) {
        try {
            Object documents =iDocument.getBidCarIdType(beadingCarId,docType);
            ResponceDto responceDto = new ResponceDto("success",documents);
            return ResponseEntity.status(HttpStatus.OK).body(responceDto);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", String.valueOf(e)));

        }
    }


    @PatchMapping("/update")
    private ResponseEntity<?> update(@RequestParam String doc,@RequestParam String doctype,@RequestParam String subtype,@RequestParam String comment,@RequestParam Integer bidDocumentId) {
        try {
            String documents =iDocument.update( doc,doctype,subtype, comment,bidDocumentId);
            ResponceDto responceDto = new ResponceDto("success",documents);
            return ResponseEntity.status(HttpStatus.OK).body(responceDto);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", String.valueOf(e)));

        }
    }
}