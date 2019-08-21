package snod.com.cn.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author MrBird
 */
@RestController
public class BrowserSecurityController {

//    private RequestCache requestCache = new HttpSessionRequestCache();

//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

//    @GetMapping("/authentication/require")
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public String requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        SavedRequest savedRequest = requestCache.getRequest(request, response);
//        if (savedRequest != null) {
//            String targetUrl = savedRequest.getRedirectUrl();
//            if (StringUtils.endsWithIgnoreCase(targetUrl, ".html"))
//                redirectStrategy.sendRedirect(request, response, "/login.html");
//        }
//        return "访问的资源需要身份认证！";
//    }

    @GetMapping("/session/invalid")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void sessionInvalid(HttpServletRequest request,HttpServletResponse response,ModelAndView model) throws IOException{
    	 response.setStatus(HttpStatus.BAD_REQUEST.value());
         response.setContentType("application/json;charset=utf-8");
         response.getWriter().write("登录超时，请重新登录");
    }
    
    @GetMapping("index")
    public Object index(Authentication authentication) {
        // return SecurityContextHolder.getContext().getAuthentication();
    	System.out.println(authentication.getPrincipal());
        return authentication;
    }
    
    @GetMapping("/auth/admin")
    @PreAuthorize("hasAuthority('admin')")
    public String authenticationTest() {
        return "您拥有admin权限，可以查看";
    }
    @GetMapping("/auth/test")
    @PreAuthorize("hasAuthority('test')")
    public String authenticationTests() {
        return "您拥有test权限，可以查看";
    }
}
