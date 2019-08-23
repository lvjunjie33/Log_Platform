package snod.com.cn.common;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedExceptionBase extends AuthenticationException{

	public UnauthorizedExceptionBase(String msg) {
		super(msg);
	}

}
