package com.anran.tingshu.user.service.impl;

import com.anran.tingshu.model.user.UserPaidTrack;
import com.anran.tingshu.user.mapper.UserPaidAlbumMapper;
import com.anran.tingshu.user.mapper.UserPaidTrackMapper;
import com.anran.tingshu.user.service.UserPaidTrackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class UserPaidTrackServiceImpl extends ServiceImpl<UserPaidTrackMapper, UserPaidTrack> implements UserPaidTrackService {

	@Autowired
	private UserPaidAlbumMapper userPaidAlbumMapper;

}
