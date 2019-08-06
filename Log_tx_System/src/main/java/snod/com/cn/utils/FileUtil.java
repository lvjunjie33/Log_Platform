package snod.com.cn.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import snod.com.cn.entity.PackageInfo;



public class FileUtil {
	private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	/**
     * 从输入流中获取数据
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        return outStream.toByteArray();
    } 
    /**
     * 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，在并发情况下很容易发生
     * 正在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检 查是否还有线程在读或写
     * @param mappedByteBuffer
     */
    public static void freedMappedByteBuffer(final MappedByteBuffer mappedByteBuffer) {
        try {
            if (mappedByteBuffer == null) {
                return;
            }
            mappedByteBuffer.force();
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        Method getCleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner", new Class[0]);
                        //可以访问private的权限
                        getCleanerMethod.setAccessible(true);
                        //在具有指定参数的 方法对象上调用此 方法对象表示的底层方法
                        sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(mappedByteBuffer,
                                new Object[0]);
                        cleaner.clean();
                    } catch (Exception e) {
                        logger.error("clean MappedByteBuffer error!!!", e);
                    }
                    logger.info("clean MappedByteBuffer completed!!!");
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    public static void getDownload(InputStream inputStream, HttpServletRequest request,
    		HttpServletResponse response, List<PackageInfo> endTimeTempList) {
    
	    // Get your file stream from wherever.
//	    String fullPath = "D:\\upload_test\\" + name;
//	    File downloadFile = new File(fullPath);
	
//	    ServletContext context = request.getServletContext();
	    // get MIME type of the file
//	    String mimeType = context.getMimeType(fullPath);
//	    if (mimeType == null) {
//	         set to binary type if MIME mapping not found
//	        mimeType = "application/octet-stream";
//	    }
	
	    // set content attributes for the response
	    response.setContentType("application/octet-stream");
	    response.setContentLengthLong(endTimeTempList.get(0).getPackeageSize());
	
	    // set headers for the response
	    String headerKey = "Content-Disposition";
	    String headerValue = String.format("attachment; filename=\"%s\"", endTimeTempList.get(0).getPackageName());
	    response.setHeader(headerKey, headerValue);
	    
	    long downloadSize = endTimeTempList.get(0).getPackeageSize();
	    long fromPos = 0, toPos = 0;
	    if (request.getHeader("Range") == null) {
	        response.setHeader("Content-Length", downloadSize + "");
	    } else {
	        // 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
	        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
	        String range = request.getHeader("Range");
	        String bytes = range.replaceAll("bytes=", "");
	        String[] ary = bytes.split("-");
	        fromPos = Long.parseLong(ary[0]);
	        if (ary.length == 2) {
	            toPos = Long.parseLong(ary[1]);
	        }
	        int size;
	        if (toPos > fromPos) {
	            size = (int) (toPos - fromPos);
	        } else {
	            size = (int) (downloadSize - fromPos);
	        }
	        response.setHeader("Content-Length", size + "");
	        downloadSize = size;
	    }
	    // 解析断点续传相关信息
	    response.setHeader("Accept-Ranges", "bytes"+ fromPos + "-" + toPos + "/" + downloadSize);
	    // Copy the stream to the response's output stream.
	    InputStream in = null;
	    OutputStream out = null; 
	    try {
	        in = inputStream;
	        // 设置下载起始位置
	        if (fromPos > 0) {
	        	inputStream.skip(fromPos);
	        }
//	        byte[] btImg = readInputStream(inputStream);
//	        System.out.println(btImg.length);
	        // 缓冲区大小
	        int bufLen = (int) (downloadSize < 5096 ? downloadSize : 5096);
	        byte[] buffer = new byte[bufLen];
	        int num;
	        int count = 0; // 当前写到客户端的大小
	        out = response.getOutputStream();
	        while ((num = inputStream.read(buffer)) != -1) {
	            out.write(buffer, 0, num);
	            count += num;
	            //处理最后一段，计算不满缓冲区的大小
	            if (downloadSize - count < bufLen) {
	                bufLen = (int) (downloadSize-count);
	                if(bufLen==0){
	                    break;
	                }
	                buffer = new byte[bufLen];
	            }
	        }
	        response.flushBuffer();
	    } catch (IOException e) {
	    	EcpStackTrace.getExceptionStackTrace(e);
	        e.printStackTrace();
	        
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        if (null != out) {
	            try {
	                out.close();
	            } catch (IOException e) {
	            	  EcpStackTrace.getExceptionStackTrace(e);
	                e.printStackTrace();
	            }
	        }
	        if (null != in) {
	            try {
	                in.close();
	            } catch (IOException e) {
	            	EcpStackTrace.getExceptionStackTrace(e);
	                e.printStackTrace();
	            }
	        }
	    }
    }

}
