package com.spring.jwt.controller.DO;

import com.spring.jwt.Interfaces.IBidPhoto;
import com.spring.jwt.dto.BidCarDto;
import net.coobird.thumbnailator.Thumbnails;
import org.bytedeco.javacv.*;
import org.bytedeco.ffmpeg.global.avcodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class R2StorageService {

    private final S3Client s3Client;

    private final IBidPhoto iDocument;

    @Value("${cloudflare.r2.bucket}")
    private String bucketName;

    public R2StorageService(S3Client s3Client,IBidPhoto iDocument) {
        this.s3Client = s3Client;
        this.iDocument=iDocument;
    }

    public String uploadFile(MultipartFile file) throws IOException {

        String key = UUID.randomUUID() + "-" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        return key;   // 🔥 return key, not filename
    }

//    public String uploadFile(File file) throws IOException {
//
//        try (InputStream is = new FileInputStream(file)) {
//
//            PutObjectRequest req = PutObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(UUID.randomUUID() + "-" + file.getName())
//                    .contentType(Files.probeContentType(file.toPath()))
//                    .build();
//
//            s3Client.putObject(req, RequestBody.fromInputStream(is, file.length()));
//        }
//
//        return file.getName();
//    }



//    public String uploadFile(MultipartFile compressedFile) throws IOException {
//        String key = UUID.randomUUID() + "-" + compressedFile.getOriginalFilename();
//
//        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(key)
//                .contentType(compressedFile.getContentType())
//                .build();
//
//        s3Client.putObject(putObjectRequest,
//                software.amazon.awssdk.core.sync.RequestBody.fromBytes(compressedFile.getBytes()));
//
//        return key; // you can return full public URL if bucket is public
//    }
//    public String uploadFile(MultipartFile file) throws IOException {
//        String key = UUID.randomUUID() + "-" + file.getOriginalFilename();
//
//        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(key)
//                .contentType(file.getContentType())
//                .build();
//
//        s3Client.putObject(putObjectRequest,
//                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
//
//        return key; // you can return full public URL if bucket is public
//    }


    @Async
    public void compressUploadAndSave(
            MultipartFile file,
            String documentType,
            String doc,
            String doctype,
            String subtype,
            String comment,
            Integer beadingCarId) {

        try {
            String contentType = file.getContentType();
            File compressedFile;

            // Detect type
            if (contentType != null && contentType.startsWith("image")) {
                compressedFile = compressImageFast(file);
            } else if (contentType != null && contentType.startsWith("video")) {
                compressedFile = compressVideoFast(file);
            } else {
                throw new RuntimeException("Unsupported file type");
            }

            // Upload
            String key = uploadFile(compressedFile);
            String documentLink = "https://photos.caryanamindia.com/" + key;

            // Save DB entry
            BidCarDto dto = new BidCarDto();
            dto.setComment(comment);
            dto.setDoctype(doctype);
            dto.setSubtype(subtype);
            dto.setDoc(doc);
            dto.setBeadingCarId(beadingCarId);
            dto.setDocumentType(documentType);
            dto.setDocumentLink(documentLink);

            iDocument.addDocument(dto);

        } catch (Exception e) {
            e.printStackTrace();
            // 🟡 optional: add retry logic
        }
    }
    public File compressImageFast(MultipartFile file) throws IOException {

        String name = UUID.randomUUID() + ".jpg";
        File output = new File(System.getProperty("java.io.tmpdir"), name);

        Thumbnails.of(file.getInputStream())
                .size(1024, 1024)
                .outputQuality(0.45)
                .outputFormat("jpg")
                .toFile(output);

        return output;
    }

    public String uploadFile(File file) throws IOException {

        String key = UUID.randomUUID() + "-" + file.getName();   // generate once

        try (InputStream is = new FileInputStream(file)) {

            PutObjectRequest req = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(Files.probeContentType(file.toPath()))
                    .build();

            s3Client.putObject(req, RequestBody.fromInputStream(is, file.length()));
        }

        return key;     // 🔥 FIXED
    }

    public File compressVideoFast(MultipartFile file) throws Exception {

        File input = File.createTempFile("input_", ".mp4");
        file.transferTo(input);

        File output = File.createTempFile("compressed_", ".mp4");

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input);
        grabber.start();

        int width = grabber.getImageWidth();
        int height = grabber.getImageHeight();
        double frameRate = grabber.getFrameRate();
        int audioChannels = grabber.getAudioChannels();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, width, height, audioChannels);

        recorder.setFormat("mp4");

        // 🔥 Compression settings (keeps good quality but reduces size)
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setVideoBitrate(800_000);  // 0.8 Mbps (reduce size)
        recorder.setFrameRate(frameRate);

        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        recorder.setAudioBitrate(96_000);  // reduce audio size

        recorder.start();

        Frame frame;
        while ((frame = grabber.grab()) != null) {
            recorder.record(frame);
        }

        recorder.stop();
        grabber.stop();

        return output;
    }
}
