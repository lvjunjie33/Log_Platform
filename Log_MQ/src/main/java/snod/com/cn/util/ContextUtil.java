package snod.com.cn.util;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;


public class ContextUtil {
	private static ApplicationContext applicationContext = null;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext == null) {
        	ContextUtil.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> Map<String, T> getBeanOfType(Class<T> clazz) {
        return getApplicationContext().getBeansOfType(clazz);
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
