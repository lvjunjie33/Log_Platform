package snod.com.cn.security.code;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.alibaba.fastjson.JSONObject;

import snod.com.cn.constant.Constant;
import snod.com.cn.entity.SysUser;
import snod.com.cn.utils.JsonObjectUtil;

public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

//	@Override
//	protected String obtainUsername(HttpServletRequest request) {
//		String username = null;
//		try {
//			username = JsonObjectUtil.charReader(request).getString(Constant.PRINCIPAL);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (!StringUtils.isEmpty(username)) {
//			return username;
//		}
//		return super.obtainUsername(request);
//	}
}
