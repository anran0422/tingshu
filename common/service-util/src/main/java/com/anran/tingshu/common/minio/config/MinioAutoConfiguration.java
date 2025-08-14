package com.anran.tingshu.common.minio.config;


import com.anran.tingshu.common.execption.BusinessException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(MinioProperties.class)
@ConditionalOnProperty(prefix = "minio", value = "enable", havingValue = "true")
public class MinioAutoConfiguration {

    Logger logger  = LoggerFactory.getLogger(this.getClass());

    /**
     * 创建 MinioClient 对象
     * 创建存储桶
     */
    @Bean
    public MinioClient minioClient(MinioProperties minioProperties) {

        try {
            // 1. 创建 MinIO 客户端
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(minioProperties.getEndpointUrl())
                            .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                            .build();

            // 2. 创建存储桶
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            } else {
                logger.info("存储桶已经存在");
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

            return minioClient;
        } catch (Exception e) {
            logger.error("minioClient 对象创建失败:{}", e.getMessage());
            throw new BusinessException(201, "minio 对象创建失败");
        }


    }
}
