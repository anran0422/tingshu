package com.anran.tingshu.album.service.impl;

import com.anran.tingshu.album.service.FileUploadService;
import io.minio.*;
import io.minio.errors.MinioException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Override
    public String fileUpload(MultipartFile file) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            // 1. 创建 MinIO 客户端
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://localhost:9000")
                            .credentials("admin", "admin123456")
                            .build();

            // 2. 创建存储桶
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("tingshu").build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("tingshu").build());
            } else {
                System.out.println("存储桶已经存在");
            }

            // 3. 上传文件
            PutObjectArgs.Builder putObjectArgsBuilder = PutObjectArgs.builder();
            PutObjectArgs putObjectArgs = putObjectArgsBuilder
                    .bucket("tingshu")
                    .object(file.getOriginalFilename())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build();

            minioClient.putObject(putObjectArgs);
        } catch (MinioException e) {
            System.out.println("Minio 上传错误：" + e.getMessage());
        }
        return null;
    }
}
