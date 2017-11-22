package com.dist.message.server.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dist.message.server.service.UserService;

import org.springframework.transaction.annotation.Propagation;

@Service("Userservice")
@Transactional(propagation = Propagation.REQUIRED)
public class UserserviceImpl implements UserService {

	@Override
	public Object checkUser(String clientId, String userId, String pwd) {
		return true;
	}

	
}
