package snod.com.cn.dao;

import snod.com.cn.entity.LogFileInfo;

public interface LogFileDao {

	public void saveFile(LogFileInfo logFileInfo);

	public LogFileInfo queryLogFileInfo(String sn);
}
