package com.anran.tingshu.album;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class APITest {

    @Test
    void testUpload() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            // 1. 创建 MinIO 客户端
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://localhost:9000")
                            .credentials("admin", "admin123456")
                            .build();

            // 2. 创建存储桶
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("demo").build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("demo").build());
            } else {
                System.out.println("存储桶已经存在");
            }

            // 3. 上传文件
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("demo")
                            .object("1.jpeg")
                            .filename("/Users/macbook/Desktop/tmpFile/一些图片/头像/侯明昊.jpeg")
                            .build());
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }
}
