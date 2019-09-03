package snod.com.cn.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;

import snod.com.cn.common.PageParam;
import snod.com.cn.config.ScheduleManager;
import snod.com.cn.dao.ScheduleJobDao;
import snod.com.cn.entity.ScheduleJob;
import snod.com.cn.enums.ScheduleStatus;

/**
 * 
 * @author lvjj
 */
@Service
public class ScheduleJobService{
	
	@Autowired
	private ScheduleJobDao scheduleJobDao;
	@Autowired
	private ScheduleManager scheduleManager;
	/**
	 * 项目启动时，初始化定时器
	 */
	@PostConstruct
	public void init(){
		List<ScheduleJob> scheduleJobs=scheduleJobDao.queryScheduleJobListAll();
		for (ScheduleJob scheduleJob : scheduleJobs) {
			if (ScheduleStatus.NORMAL.getType().equals(scheduleJob.getStatus())) {
				scheduleManager.resumeJob(scheduleJob);
			} else if (ScheduleStatus.PAUSE.getType().equals(scheduleJob.getStatus())) {
				scheduleManager.pauseJob(scheduleJob);
			}
		}
	}
	@Transactional(rollbackFor = Exception.class)
	public void saveAndStart(ScheduleJob scheduleJob) {
		scheduleJob.setCreateTime(new Date());
		scheduleJob.setStatus(ScheduleStatus.NORMAL.getType());
		scheduleJobDao.insertScheduleJob(scheduleJob);
        
        scheduleManager.createScheduleJob(scheduleJob);
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateScheduleJob(ScheduleJob scheduleJob) {
		scheduleManager.updateScheduleJob(scheduleJob);
		scheduleJobDao.updateScheduleJobInfo(scheduleJob);
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] jobIds) {

		List<Long> ids = Arrays.asList(jobIds);
		List<ScheduleJob> scheduleJobs=scheduleJobDao.queryScheduleJobInfoByIds(ids);
		for (ScheduleJob scheduleJob : scheduleJobs) {
			scheduleManager.deleteScheduleJob(scheduleJob);
		}
		
		scheduleJobDao.deleteBatchIds(ids);
	}

	public int updateBatch(Long[] jobIds, int status) {
		return scheduleJobDao.updateBatch(jobIds,status);
	}
	
	@Transactional(rollbackFor = Exception.class)
    public void run(Long[] jobIds) {
    	for(Long jobId : jobIds){
    		scheduleManager.run(scheduleJobDao.queryScheduleJobInfo(jobId));
    	}
    }

	@Transactional(rollbackFor = Exception.class)
    public void pause(Long[] jobIds) {
		List<Long> ids = Arrays.asList(jobIds);
		List<ScheduleJob> scheduleJobs=scheduleJobDao.queryScheduleJobInfoByIds(ids);
		for (ScheduleJob scheduleJob : scheduleJobs) {
			scheduleManager.pauseJob(scheduleJob);
		}
        
    	updateBatch(jobIds, ScheduleStatus.PAUSE.getType());
    }

	@Transactional(rollbackFor = Exception.class)
    public void resume(Long[] jobIds) {
		List<Long> ids = Arrays.asList(jobIds);
		List<ScheduleJob> scheduleJobs=scheduleJobDao.queryScheduleJobInfoByIds(ids);
		for (ScheduleJob scheduleJob : scheduleJobs) {
			scheduleManager.resumeJob(scheduleJob);
		}

    	updateBatch(jobIds, ScheduleStatus.NORMAL.getType());
    }
	public IPage<ScheduleJob> queryScheduleJobList(PageParam<ScheduleJob> page, String beanName) {
		page.setRecords(scheduleJobDao.queryScheduleJobList(page.getCurrent()-1,page.getSize(),beanName));
		page.setTotal(scheduleJobDao.queryScheduleJobListCount(beanName));
		return page;
	}
	public ScheduleJob queryScheduleJobInfo(Long jobId) {
		return scheduleJobDao.queryScheduleJobInfo(jobId);
	}
}
