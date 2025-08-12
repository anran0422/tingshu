package com.anran.tingshu.album.service.impl;

import com.anran.tingshu.album.config.MinioProperties;
import com.anran.tingshu.album.service.FileUploadService;
import com.anran.tingshu.common.execption.BusinessException;
import io.minio.*;
import io.minio.errors.MinioException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Resource
    private MinioProperties minioProperties;

    @Resource
    private MinioClient minioClient;

    @Override
    public String fileUpload(MultipartFile file) {
        try {
            // 1. 上传文件
            PutObjectArgs.Builder putObjectArgsBuilder = PutObjectArgs.builder();
            PutObjectArgs putObjectArgs = putObjectArgsBuilder
                    .bucket(minioProperties.getBucketName())
                    .object(file.getOriginalFilename())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            log.info("上传文件到 minio 成功");

            // 返回图片 url
            return minioProperties.getEndpointUrl() + "/" + minioProperties.getBucketName() + "/" + file.getOriginalFilename();
        } catch (Exception e) {
            log.error("Minio 上传错误：" + e.getMessage());
            throw new BusinessException(201, "上传文件到 minio 失败");
        }
    }
}
