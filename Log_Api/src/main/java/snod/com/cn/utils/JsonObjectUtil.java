package snod.com.cn.utils;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

public class JsonObjectUtil {
	 /**
     * HttpServletRequest获取body数据并把json数据转成JSONObject
     * */
   public static JSONObject charReader(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();

        String str, wholeStr = "";
        while((str = br.readLine()) != null){
            wholeStr += str;
        }
        JSONObject json=(JSONObject) JSONObject.parse(wholeStr);
        return json;

    }
}
