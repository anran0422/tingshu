package com.anran.tingshu.common.minio.service.impl;

import com.anran.tingshu.common.minio.config.MinioProperties;
import com.anran.tingshu.common.execption.BusinessException;
import com.anran.tingshu.common.minio.service.FileUploadService;
import com.anran.tingshu.common.util.MD5;
import io.minio.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Resource
    private MinioProperties minioProperties;

    @Resource
    private MinioClient minioClient;

    @Override
    public String fileUpload(MultipartFile file) {

        String picObject = "";

        try {
            // 1. 将文件按照文件内容加密，得到不同的文件名
            byte[] bytes = file.getBytes();
            String content = new String(bytes);
            String prefix = MD5.encrypt(content);
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".", originalFilename.length()));
            picObject = prefix + suffix;

            // 2. Minio 判断是否存在该文件
            StatObjectArgs.Builder builder = StatObjectArgs.builder();
            StatObjectArgs statObjectArgs = builder
                    .bucket(minioProperties.getBucketName())
                    .object(picObject)
                    .build();

            // 逻辑是相反的，如果不抛异常说明存在
            minioClient.statObject(statObjectArgs);
            return minioProperties.getEndpointUrl() + "/" + minioProperties.getBucketName() + "/" + picObject;

        } catch (IOException e) {
            log.error("上传的文件不存在，原因：" + e.getMessage());
            throw new BusinessException(201, "该文件不存在");
        } catch (Exception e) {

            log.info("桶中不存在该对象，可以上传到 minio 中");
            try {
                // 3. 上传文件
                PutObjectArgs.Builder putObjectArgsBuilder = PutObjectArgs.builder();
                PutObjectArgs putObjectArgs = putObjectArgsBuilder
                        .bucket(minioProperties.getBucketName())
                        .object(picObject)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .build();
                minioClient.putObject(putObjectArgs);
                log.info("上传文件到 minio 成功");

                return minioProperties.getEndpointUrl() + "/" + minioProperties.getBucketName() + "/" + picObject;
            } catch (Exception ex) {
                log.error("Minio 上传错误：" + ex.getMessage());
                throw new BusinessException(201, "上传文件到 minio 失败");
            }
        }
    }
}
