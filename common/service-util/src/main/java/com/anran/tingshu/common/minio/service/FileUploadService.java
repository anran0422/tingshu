package com.anran.tingshu.common.minio.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface FileUploadService {
    String fileUpload(MultipartFile file);
}
