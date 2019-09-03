/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package snod.com.cn.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import snod.com.cn.entity.ScheduleJob;

/**
 * 定时任务，任务调度mapper
 * @author lgh
 */

public interface ScheduleJobDao{
	/**
	 *  批量修改任务状态
	 * @param jobIds 调度任务id
	 * @param status 任务状态
	 * @return 修改成功条数
	 */
	int updateBatch(@Param("jobIds") Long[] jobIds, @Param("status") int status);

	List<ScheduleJob> queryScheduleJobList(@Param("index")Long index, @Param("size")Long size, @Param("beanName")String beanName);

	long queryScheduleJobListCount(String beanName);

	ScheduleJob queryScheduleJobInfo(Long jobId);

	void insertScheduleJob(ScheduleJob scheduleJob);

	void updateScheduleJobInfo(ScheduleJob scheduleJob);


	List<ScheduleJob> queryScheduleJobInfoByIds(List<Long> ids);

	void deleteBatchIds(List<Long> ids);

	List<ScheduleJob> queryScheduleJobListAll();
}