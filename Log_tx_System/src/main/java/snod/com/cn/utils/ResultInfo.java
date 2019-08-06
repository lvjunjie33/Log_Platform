package snod.com.cn.utils;
/**
 * @author lvjj
 * */
public class ResultInfo {
	    public long code;
	    public String message;
	    public Object data;
	    
	    public long getCode() {
	        return code;
	    }

	    public void setCode(long code) {
	        this.code = code;
	    }

	    public String getMessage() {
	        if(message==null){
	            message="处理成功！";
	        }
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public Object getData() {
	        return data;
	    }

	    public void setData(Object data) {
	        this.data = data;
	    }

		@Override
		public String toString() {
			return "{\"code\":"+ code + ",\"message\":\"" + message + "\",\"data\":\"" + data +"\"}";
		}
	    
}


