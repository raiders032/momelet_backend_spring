package com.swm.sprint1.util;

import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.swm.sprint1.exception.NotSupportedExtension;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3 s3Client;
    @Value("${cloud.aws.s3.bucket}") private String s3BucketName;
    @Value("${app.s3.profile.dir}") private String profilePath;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = uploadFile.getName();
        String uuid = UUID.randomUUID().toString();
        String fileNameOnly = fileName.substring(0,fileName.lastIndexOf("."));
        String newFileName = dirName + "/" + fileNameOnly + "_" + uuid + ".jpeg";

        String imageUrl = putS3(uploadFile, newFileName);
        removeNewFile(uploadFile);

        return imageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        s3Client.putObject(new PutObjectRequest(s3BucketName, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(s3BucketName, fileName).toString();
    }

    public String uploadImageFile(MultipartFile imageFile) throws IOException {
        String filename = imageFile.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf("."));
        List<String> supportedExtension = Arrays.asList(".jpg", ".jpeg", ".png");
        if (!supportedExtension.contains(extension)) {
            throw new NotSupportedExtension(extension + "은 지원하지 않는 확장자입니다. jpg, jpeg, png만 지원합니다.");
        }
        return upload(imageFile, profilePath);
    }


    public String changeImageUrl(String imageUrl){
        return imageUrl
                .replace("momelet.s3.ap-northeast-2.amazonaws.com", "dz1rd925xfsaa.cloudfront.net");
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 않았습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}