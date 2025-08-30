package com.anran.tingshu.album.service;

import com.anran.tingshu.model.album.AlbumInfo;
import com.anran.tingshu.vo.album.AlbumInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AlbumInfoService extends IService<AlbumInfo> {


    void saveAlbmInfo(AlbumInfoVo albumInfoVo);
}
