package com.dist.message.server.model;

import java.security.Principal;

public class MyPrincipal implements Principal {

	private User user;
	
	 public MyPrincipal(User user) {
         this.user = user;
     }
	@Override
	public String getName() {
		return this.user.getId();
	}
}
