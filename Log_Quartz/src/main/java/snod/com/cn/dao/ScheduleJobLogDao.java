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

import snod.com.cn.entity.ScheduleJobLog;

public interface ScheduleJobLogDao{

	List<ScheduleJobLog> queryListScheduleJobLog(@Param("index")Long index, @Param("size")Long size, @Param("jobId")Long jobId);

	long queryListScheduleJobLogCount(Long jobId);

	void insertScheduleJobLog(ScheduleJobLog jobLog);

}