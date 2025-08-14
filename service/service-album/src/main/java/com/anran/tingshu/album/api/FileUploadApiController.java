package com.anran.tingshu.album.api;

import com.anran.tingshu.common.minio.service.FileUploadService;
import com.anran.tingshu.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "上传管理接口")
@RestController
@RequestMapping("api/album")
public class FileUploadApiController {

    @Resource
    private FileUploadService fileUploadService;

    @Operation(summary = "图片文件的上传")
    @PostMapping("/fileUpload")
    public Result fileUpload(MultipartFile file){
        String pictureUrl =  fileUploadService.fileUpload(file);
        return Result.ok(pictureUrl);
    }
}
