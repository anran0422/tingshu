package com.anran.tingshu.album.service.impl;

import com.anran.tingshu.album.config.VodConstantProperties;
import com.anran.tingshu.album.service.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VodServiceImpl implements VodService {

    @Autowired
    private VodConstantProperties vodConstantProperties;

}
