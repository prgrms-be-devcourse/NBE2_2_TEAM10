package com.prgrms2.java.bitta.media.service;

import com.prgrms2.java.bitta.media.exception.MediaException;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final S3Client s3Client;

    private final S3Presigner s3Presigner;

    @Value("${s3.bucket.name}")
    private String bucketName;

    @Override
    public String generatePreSignedUrl(String filepath) {
        try {
            checkFileExists(filepath);

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filepath)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(request -> request
                    .signatureDuration(Duration.ofMinutes(1))
                    .getObjectRequest(getObjectRequest));

            return presignedGetObjectRequest.url().toString();
        } catch (NoSuchKeyException e) {
            throw MediaException.S3_CANNOT_FOUND.get();
        }
    }

    @Override
    public void upload(MultipartFile multipartFile, String filepath) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filepath)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));
        } catch (IOException e) {
            throw MediaException.S3_CANNOT_UPLOAD.get();
        }
    }

    @Override
    public void uploadThumbnail(MultipartFile multipartFile, String filepath) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            Thumbnails.of(multipartFile.getInputStream())
                    .size(200, 200)
                    .keepAspectRatio(true)
                    .outputFormat("jpg")
                    .toOutputStream(byteArrayOutputStream);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filepath)
                    .contentType("image/jpeg")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(byteArrayOutputStream.toByteArray()));

            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw MediaException.S3_CANNOT_UPLOAD.get();
        }
    }

    @Override
    public void delete(String filepath) {
        try {
            checkFileExists(filepath);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filepath)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (NoSuchKeyException e) {
            throw MediaException.S3_CANNOT_DELETE.get();
        }
    }

    private void checkFileExists(String filepath) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(filepath)
                .build();

        s3Client.headObject(headObjectRequest);
    }
}
