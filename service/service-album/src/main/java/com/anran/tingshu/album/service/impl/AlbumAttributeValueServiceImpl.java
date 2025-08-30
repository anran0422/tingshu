package com.anran.tingshu.album.service.impl;

import com.anran.tingshu.album.mapper.AlbumAttributeValueMapper;
import com.anran.tingshu.album.service.AlbumAttributeValueService;
import com.anran.tingshu.model.album.AlbumAttributeValue;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AlbumAttributeValueServiceImpl extends ServiceImpl<AlbumAttributeValueMapper, AlbumAttributeValue>
        implements AlbumAttributeValueService {
}
