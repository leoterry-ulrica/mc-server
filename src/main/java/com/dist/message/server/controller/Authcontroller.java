package com.dist.message.server.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.result.Result;
import com.dist.message.server.conf.WSConstants;
import com.dist.message.server.model.AudienceInfo;
import com.dist.message.server.model.LoginInfo;
import com.dist.message.server.model.jwt.AccessToken;
import com.dist.message.server.model.jwt.Audience;
import com.dist.message.server.service.UserService;
import com.dist.message.server.utils.JWTUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags={"API-授权认证服务"}, description = "Authcontroller")
@RestController
@RequestMapping(value="/rest/auth", produces = {"application/json;charset=UTF-8"})
public class Authcontroller extends BaseController {

	private static Logger LOG = LoggerFactory.getLogger(Authcontroller.class);
	
	@Autowired  
    private Audience audienceInfo; 
	@Autowired
	private UserService userService;

	@Override
	protected void initBinder(WebDataBinder binder) {
		// 屏蔽基类的方法，否则会有冲突，提示错误：DataBinder is already initialized
	}
	@ApiOperation(value = "获取audience信息", notes = "getAudience")
	@RequestMapping(value = "/audience/v1", method = { RequestMethod.GET})
	public Result getAudience() {
		AudienceInfo info = new AudienceInfo();
		info.setBase64Secret(this.audienceInfo.getBase64Secret());
		info.setClientId(this.audienceInfo.getClientId());
		info.setName(this.audienceInfo.getName());
		info.setExpiresSecond(this.audienceInfo.getExpiresSecond());
		return super.successResult(info);
	}
	@ApiOperation(value = "获取token", notes = "generateToken")
	@RequestMapping(value = "/token/v1", method = { RequestMethod.POST})
	public Result generateToken(@RequestBody LoginInfo info) {
		
		if(StringUtils.isEmpty(info.getClientId()) || !info.getClientId().equals(audienceInfo.getClientId())) {
			LOG.warn(">>>clientId验证不通过");
			return super.failResult("验证不通过");
		}
		if((boolean) this.userService.checkUser(info.getClientId(), info.getUserId(), info.getPwd())) {
			super.session.setAttribute(WSConstants.SESSION_USERNAME, info.getUserId());
		} else {
			LOG.warn(">>>用户信息验证不通过");
			return super.failResult("验证不通过");
		}
		long nowMillis = System.currentTimeMillis() + this.audienceInfo.getExpiresSecond() * 1000;
		String tokenStr = JWTUtil.createJWT(this.audienceInfo.getName(), info.getUserId(), info.getClientId(), nowMillis, this.audienceInfo.getBase64Secret());
		AccessToken token = new AccessToken();
		token.setAccess_token(tokenStr);
		token.setExpires_in(nowMillis);
		
		return super.successResult(token);
	}
}
