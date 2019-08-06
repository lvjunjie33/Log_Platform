package snod.com.cn.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FtpUtil implements EnvironmentAware{
	private static RelaxedPropertyResolver propertyResolver;
	
	public void setEnvironment(Environment env) {
	        propertyResolver = new RelaxedPropertyResolver(env, "ftp.");
	}
    
    // ftp客户端
    private static FTPClient ftpClient = new FTPClient();
    
    /**
     * 
     * 功能：上传文件附件到文件服务器
     * @param filePath 
     * @param buffIn:上传文件流
     * @param fileName：保存文件名称
     * @param needDelete：是否同时删除
     * @return
     * @throws IOException
     */
    public static boolean uploadToFtp(String fileName,InputStream buffIn, String filePath)
            throws FTPConnectionClosedException, IOException,Exception {
        boolean returnValue = false;
        String host = propertyResolver.getProperty("host");
        // 上传文件
        try {
            
                // 建立连接
                connectToServer();
                // 设置传输二进制文件
                setFileType(FTP.BINARY_FILE_TYPE);
                int reply = ftpClient.getReplyCode();   
                if(!FTPReply.isPositiveCompletion(reply))    
                {   
                    ftpClient.disconnect();   
                    throw new IOException("failed to connect to the FTP Server:"+host);   
                }
                /**
                 * ftp server可能每次开启不同的端口来传输数据，但是在linux上或者其他服务器上面，由于安全限制，可能某些端口没有开启，所以就出现阻塞
				在FTPClient.storeFile()方法前调用FTPClient.enterLocalPassiveMode() ，让客户端告诉服务端开通一个端口用来数据传输
                 * */
                ftpClient.enterLocalPassiveMode();
                if(!existDirectory(filePath)){
                	createDirectory(filePath);
                }
                ftpClient.enterLocalPassiveMode();
                // 上传文件到ftp
                returnValue = ftpClient.storeFile(fileName, buffIn);
                // 输出操作结果信息
                if (returnValue) {
                    System.out.println("uploadToFtp INFO: upload file  to ftp : succeed!");
                } else {
                	System.out.println("uploadToFtp INFO: upload file  to ftp : failed!");
                }
                buffIn.close();
                // 关闭连接
                closeConnect();
        } catch (FTPConnectionClosedException e) {
        	e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffIn != null) {
                    buffIn.close();
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }
            if (ftpClient.isConnected()) {
                closeConnect();
            }            
        }
        return returnValue;
    }
    
    
    /**
     * 
     * 功能：根据文件名称，下载文件流
     * @param filename
     * @return
     * @throws IOException
     */
    public static InputStream  downloadFile(String filename)
            throws IOException {
    	 String host = propertyResolver.getProperty("host");
        InputStream in=null;
        try {
            
                // 建立连接
                connectToServer();
                ftpClient.enterLocalPassiveMode();
                // 设置传输二进制文件
                setFileType(FTP.BINARY_FILE_TYPE);
                int reply = ftpClient.getReplyCode();   
                if(!FTPReply.isPositiveCompletion(reply))    
                {   
                    ftpClient.disconnect();
                    throw new IOException("failed to connect to the FTP Server:"+host);   
                }
                ftpClient.changeWorkingDirectory(filename);
 
                // ftp文件获取文件
                in=ftpClient.retrieveFileStream(filename);
 
        } catch (FTPConnectionClosedException e) {
//        	log.error("ftp连接被关闭！", e);
            throw e;
        } catch (Exception e) {
//            log.error("ERR : upload file "+ filename+ " from ftp : failed!", e);
        }
        return in;
    }
    
    /**
     * 转码[GBK -> ISO-8859-1] 不同的平台需要不同的转码
     * 
     * @param obj
     * @return
     */
//    private String gbkToIso8859(Object obj) {
//        try {
//            if (obj == null)
//                return "";
//            else
//                return new String(obj.toString().getBytes("GBK"), "iso-8859-1");
//        } catch (Exception e) {
//            return "";
//        }
//    }
    
    /**
     * 设置传输文件的类型[文本文件或者二进制文件]
     * 
     * @param fileType
     *            --BINARY_FILE_TYPE、ASCII_FILE_TYPE
     */
    private static void setFileType(int fileType) {
        try {
            ftpClient.setFileType(fileType);
        } catch (Exception e) {
//        	log.error("ftp设置传输文件的类型时失败！", e);
        }
    }
    
    /**
     * 
     * 功能：关闭连接
     */
    public static void closeConnect() {
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception e) {
//        	log.error("ftp连接关闭失败！", e);
        }
    }
    
    /** 
     * 连接到ftp服务器 
     */ 
    private static void connectToServer() throws FTPConnectionClosedException,Exception {
    	String host = propertyResolver.getProperty("host");
    	int port = Integer.parseInt(propertyResolver.getProperty("port"));
    	String username = propertyResolver.getProperty("username");
    	String password = propertyResolver.getProperty("password");
        if (!ftpClient.isConnected()) { 
            int reply; 
            try { 
                ftpClient=new FTPClient();
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.connect(host,port);
                ftpClient.login(username,password);
                reply = ftpClient.getReplyCode(); 
 
                if (!FTPReply.isPositiveCompletion(reply)) { 
                    ftpClient.disconnect(); 
//                    log.info("connectToServer FTP server refused connection."); 
                } 
            
            }catch(FTPConnectionClosedException ex){
//                log.error("服务器:IP："+ip+"没有连接数！there are too many connected users,please try later", ex);
                ex.printStackTrace();
            }catch (Exception e) { 
//                log.error("登录ftp服务器【"+ip+"】失败", e); 
                e.printStackTrace();
            } 
        } 
    } 
    // Check the path is exist; exist return true, else false.  
    public static boolean existDirectory(String path) throws IOException {
        boolean flag = false;  
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);  
        for (FTPFile ftpFile : ftpFileArr) {  
            if (ftpFile.isDirectory()  
                    && ftpFile.getName().equalsIgnoreCase(path)) {  
                flag = true;  
                break;  
            }  
        }  
        return flag;  
    } 
    /**
     * 创建FTP文件夹目录
     * @param pathName
     * @return
     * @throws IOException
     */
    public static boolean createDirectory(String pathName) throws IOException { 
    	boolean isSuccess=false;
    	try{
    		String []paths=pathName.split("/");
    		for(int i=1;i<paths.length;i++) {
//    		isSuccess=ftpClient.makeDirectory(pathName);
    			//逐步创建目录
	    		if (!ftpClient.changeWorkingDirectory(paths[i])) {
	    			  //创建目录
	    	          ftpClient.makeDirectory(paths[i]);
	    	          //进入目录
	    	          ftpClient.changeWorkingDirectory(paths[i]);
	    		}
    		}
    		isSuccess=true;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        return isSuccess;  
    }
    /**
     * 带点的
     * @param fileName
     * @return
     */
    public static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos);
    }
    /**
     * 不带点
     * @param fileName
     * @return
     */
    public static String getNoPointExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos+1);
    }
  

}
