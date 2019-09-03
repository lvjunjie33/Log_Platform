package snod.com.cn.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import snod.com.cn.common.PageParam;
import snod.com.cn.dao.ScheduleJobLogDao;
import snod.com.cn.entity.ScheduleJobLog;

/**
 * @author lgh
 */
@Service("scheduleJobLogService")
public class ScheduleJobLogService{

	@Autowired
	private ScheduleJobLogDao scheduleJobLogDao;

	public IPage<ScheduleJobLog> queryListScheduleJobLog(PageParam<ScheduleJobLog> page, Long jobId) {
		page.setRecords(scheduleJobLogDao.queryListScheduleJobLog(page.getCurrent()-1,page.getSize(),jobId));
		page.setTotal(scheduleJobLogDao.queryListScheduleJobLogCount(jobId));
		return page;
	}

	public void insertScheduleJobLog(ScheduleJobLog jobLog) {
		scheduleJobLogDao.insertScheduleJobLog(jobLog);
		
	}

}
