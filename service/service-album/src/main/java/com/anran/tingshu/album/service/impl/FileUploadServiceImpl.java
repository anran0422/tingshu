package com.anran.tingshu.album.service.impl;

import com.anran.tingshu.album.config.MinioProperties;
import com.anran.tingshu.album.service.FileUploadService;
import io.minio.*;
import io.minio.errors.MinioException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Resource
    private MinioProperties minioProperties;

    @Override
    public String fileUpload(MultipartFile file) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            // 1. 创建 MinIO 客户端
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(minioProperties.getEndpointUrl())
                            .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                            .build();

            // 2. 创建存储桶
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            } else {
                System.out.println("存储桶创建成功");
            }

            // 设置桶为 public
            String policyJson = """
                {
                  "Version": "2012-10-17",
                  "Statement": [
                    {
                      "Action": ["s3:GetObject"],
                      "Effect": "Allow",
                      "Principal": "*",
                      "Resource": ["arn:aws:s3:::%s/*"]
                    }
                  ]
                }
                """.formatted(minioProperties.getBucketName());

            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .config(policyJson)
                            .build()
            );

            // 3. 上传文件
            PutObjectArgs.Builder putObjectArgsBuilder = PutObjectArgs.builder();
            PutObjectArgs putObjectArgs = putObjectArgsBuilder
                    .bucket(minioProperties.getBucketName())
                    .object(file.getOriginalFilename())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build();

            minioClient.putObject(putObjectArgs);
        } catch (MinioException e) {
            System.out.println("Minio 上传错误：" + e.getMessage());
        }
        // 返回名称
        return String.format("http://{}/{}/{}", minioProperties.getEndpointUrl(), minioProperties.getBucketName(), file.getOriginalFilename());
    }
}
