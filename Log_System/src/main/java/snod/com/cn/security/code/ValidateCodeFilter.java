package snod.com.cn.security.code;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson.JSONObject;

import snod.com.cn.controller.SysLoginController;
import snod.com.cn.entity.SysUser;
import snod.com.cn.redis.RedisService;
import snod.com.cn.utils.JsonObjectUtil;



@Component
public class ValidateCodeFilter extends OncePerRequestFilter{

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    
    @Autowired
	private RedisService redisService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equalsIgnoreCase("/login", httpServletRequest.getRequestURI())
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(), "post")) {
            try {
//                JSONObject json=JsonObjectUtil.charReader(httpServletRequest);
            	String codeuuid=httpServletRequest.getParameter("sessionUUID");
            	String imageCode=(String) redisService.get(codeuuid);
                validateCode(new ServletWebRequest(httpServletRequest),imageCode);
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void validateCode(ServletWebRequest servletWebRequest, String codeInRequest) throws ServletRequestBindingException {
    	String codeInSession = (String) sessionStrategy.getAttribute(servletWebRequest, SysLoginController.SESSION_KEY_IMAGE_CODE);
//        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "imageCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码已过期！");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在！");
        }
//        if (codeInSession==null) {
//            sessionStrategy.removeAttribute(servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE);
//            throw new ValidateCodeException("验证码已过期！");
//        }
        if (!StringUtils.equalsIgnoreCase(codeInSession, codeInRequest)) {
            throw new ValidateCodeException("验证码不正确！");
        }
        
//        sessionStrategy.removeAttribute(servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE);

    }
    
 
   
}
