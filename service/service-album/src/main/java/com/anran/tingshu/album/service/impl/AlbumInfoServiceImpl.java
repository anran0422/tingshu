package com.anran.tingshu.album.service.impl;

import com.anran.tingshu.album.mapper.AlbumAttributeValueMapper;
import com.anran.tingshu.album.mapper.AlbumInfoMapper;
import com.anran.tingshu.album.mapper.AlbumStatMapper;
import com.anran.tingshu.album.service.AlbumAttributeValueService;
import com.anran.tingshu.album.service.AlbumInfoService;
import com.anran.tingshu.common.constant.SystemConstant;
import com.anran.tingshu.common.util.AuthContextHolder;
import com.anran.tingshu.model.album.AlbumAttributeValue;
import com.anran.tingshu.model.album.AlbumInfo;
import com.anran.tingshu.model.album.AlbumStat;
import com.anran.tingshu.vo.album.AlbumAttributeValueVo;
import com.anran.tingshu.vo.album.AlbumInfoVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class AlbumInfoServiceImpl extends ServiceImpl<AlbumInfoMapper, AlbumInfo> implements AlbumInfoService {

	@Autowired
	private AlbumInfoMapper albumInfoMapper;

    @Autowired
    private AlbumAttributeValueMapper albumAttributeValueMapper;

    @Autowired
    private AlbumAttributeValueService albumAttributeValueService;

    @Autowired
    private AlbumStatMapper albumStatMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAlbmInfo(AlbumInfoVo albumInfoVo) {
        Long userId = AuthContextHolder.getUserId();

        // 1. 保存专辑基本信息（album_info 表中插入数据）
        // 实体类专门用来和数据库列做映射
        AlbumInfo albumInfo = new AlbumInfo();
        BeanUtils.copyProperties(albumInfoVo, albumInfo);
        albumInfo.setUserId(userId);
        albumInfo.setStatus(SystemConstant.ALBUM_STATUS_PASS); // 默认审核通过 （TODO 对接后续审核系统）

        // 获取专辑的付费类型 0101 免费 0102 vip免费 0103 付费
        String payType = albumInfoVo.getPayType();
        if(!SystemConstant.ALBUM_PAY_TYPE_FREE.equals(payType)) {
            // 默认设置 5 级免费
            albumInfo.setTracksForFree(5);
        }

        int insert = albumInfoMapper.insert(albumInfo);
        log.info("保存专辑基本信息：{}", insert > 0 ? "保存成功" : "保存失败");

        // 保存专辑的标签属性信息（album_attribute_value 表中插入数据）
        List<AlbumAttributeValueVo> albumAttributeValueVoList = albumInfoVo.getAlbumAttributeValueVoList();

        List<AlbumAttributeValue> attributeValues = albumAttributeValueVoList.stream()
                .map(albumAttributeValueVo -> {
                    AlbumAttributeValue albumAttributeValue = new AlbumAttributeValue();
                    albumAttributeValue.setAlbumId(albumInfo.getId());
                    albumAttributeValue.setAttributeId(albumAttributeValueVo.getAttributeId());
                    albumAttributeValue.setValueId(albumAttributeValueVo.getValueId());
                    return albumAttributeValue;
                }).collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(attributeValues)) {
            boolean b = albumAttributeValueService.saveBatch(attributeValues);
            log.info("保存专辑标签信息：{}", b ? "保存成功" : "保存失败");
        }

        // 3. 保存专辑的统计（album_stat）
        saveAlbumStat(albumInfo.getId());
    }

    private void saveAlbumStat(Long albumId) {
        ArrayList<String> albumStats = new ArrayList<>();
        albumStats.add(SystemConstant.ALBUM_STAT_PLAY);
        albumStats.add(SystemConstant.ALBUM_STAT_SUBSCRIBE);
        albumStats.add(SystemConstant.ALBUM_STAT_BROWSE);
        albumStats.add(SystemConstant.ALBUM_STAT_COMMENT);
        for (String status : albumStats) {
            AlbumStat albumStat = new AlbumStat();
            albumStat.setAlbumId(albumId);
            albumStat.setStatType(status);
            albumStat.setStatNum(0);
            albumStatMapper.insert(albumStat);
        }
    }
}
