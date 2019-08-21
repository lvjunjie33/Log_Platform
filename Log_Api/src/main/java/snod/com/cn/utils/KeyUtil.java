package snod.com.cn.utils;

import java.util.Random;

/**
 * @author lvjj
 * */
public class KeyUtil {

    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer num = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(num);
    }
}
