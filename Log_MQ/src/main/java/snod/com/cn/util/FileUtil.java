package snod.com.cn.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import snod.com.cn.entity.LogOtaUpgrade;

public class FileUtil {
	private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 从输入流中获取数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		return outStream.toByteArray();
	}

	/**
	 * 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，在并发情况下很容易发生
	 * 正在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检 查是否还有线程在读或写
	 * 
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
						// 可以访问private的权限
						getCleanerMethod.setAccessible(true);
						// 在具有指定参数的 方法对象上调用此 方法对象表示的底层方法
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

	public static List<File> getFiles(File f, List<File> files) {
		if (f.isFile()) {
			if (f.getName().contains(".log") || f.getName().contains(".txt")) {
				files.add(f);
			}
		}
		if (f.isDirectory()) {
			File[] fs = f.listFiles();
			for (File temp : fs) {
				getFiles(temp, files);
			}
		}
		return files;
	}

}
