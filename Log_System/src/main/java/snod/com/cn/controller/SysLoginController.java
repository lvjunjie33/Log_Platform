/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package snod.com.cn.controller;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import snod.com.cn.redis.RedisService;
import snod.com.cn.security.code.ImageCode;
import snod.com.cn.security.smscode.SmsCode;
import snod.com.cn.utils.SecurityUtils;
import snod.com.cn.utils.SimpleCaptcha;

/**
 * 登录相关
 * @author lgh
 */
@RestController
public class SysLoginController {
	public final static String SESSION_KEY_IMAGE_CODE = "SESSION_KEY_IMAGE_CODE";

    public final static String SESSION_KEY_SMS_CODE = "SESSION_KEY_SMS_CODE";

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    
	@Autowired
	private RedisService redisService;

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response,String uuid) throws IOException {
    	//定义图形验证码的长、宽、验证码字符数、干扰元素个数
    	SimpleCaptcha simpleCaptcha = new SimpleCaptcha(200, 50, 4, 20);
    	ImageCode imageCode=new ImageCode(simpleCaptcha.getImage(), simpleCaptcha.getCode(), 60);
    	redisService.set(uuid, imageCode.getCode(),new Long(120));
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY_IMAGE_CODE, imageCode.getCode());
        simpleCaptcha.write(response.getOutputStream());
    }

    @GetMapping("/code/sms")
    public void createSmsCode(HttpServletRequest request, HttpServletResponse response, String mobile) {
        SmsCode smsCode = createSMSCode();
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY_SMS_CODE + mobile, smsCode);
        // 输出验证码到控制台代替短信发送服务
        System.out.println("您的登录验证码为：" + smsCode.getCode() + "，有效时间为60秒");
    }

    private SmsCode createSMSCode() {
        String code = RandomStringUtils.randomNumeric(6);
        return new SmsCode(code, 60);
    }
    
	/**
	 * 退出
	 */
	@PostMapping(value = "/sys/logout")
	public ResponseEntity<String> logout() {
//		Cache cache = cacheManager.getCache("yami_sys_user");
//		if (cache != null) {
//			cache.evict(SecurityUtils.getSysUser().getUsername());
//		}
		SecurityContextHolder.clearContext();
		return ResponseEntity.ok().build();
	}
}
