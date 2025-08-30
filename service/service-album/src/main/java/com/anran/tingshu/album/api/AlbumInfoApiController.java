package com.anran.tingshu.album.api;

import com.anran.tingshu.album.service.AlbumInfoService;
import com.anran.tingshu.common.login.annotation.TingshuLogin;
import com.anran.tingshu.common.result.Result;
import com.anran.tingshu.vo.album.AlbumInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "专辑管理")
@RestController
@RequestMapping("api/album/albumInfo")
@SuppressWarnings({"unchecked", "rawtypes"})
public class AlbumInfoApiController {

    @Autowired
    private AlbumInfoService albumInfoService;


    @PostMapping("/saveAlbumInfo")
    @TingshuLogin
    @Operation(summary = "保存专辑信息")
    public Result saveAlbumInfo(@RequestBody AlbumInfoVo albumInfoVo) {
        albumInfoService.saveAlbmInfo(albumInfoVo);
        return Result.ok();
    }

}

