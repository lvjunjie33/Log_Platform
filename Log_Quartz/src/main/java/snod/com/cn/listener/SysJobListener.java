

package snod.com.cn.listener;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;



import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import snod.com.cn.entity.ScheduleJob;
import snod.com.cn.entity.ScheduleJobLog;
import snod.com.cn.event.ScheduleJobEvent;
import snod.com.cn.service.ScheduleJobLogService;
import snod.com.cn.util.SpringBeanTaskUtil;


/**
 * 异步监听定时任务事件，解决job线程无故丢失的问题
 */
@Slf4j
@Component
@EnableAsync
public class SysJobListener {

	@Autowired
	private ScheduleJobLogService scheduleJobLogService;

	@Async
	@EventListener(ScheduleJobEvent.class)
	public void scheduleJobEventListener(ScheduleJobEvent event) {
		ScheduleJob scheduleJob = event.getScheduleJob();
		//数据库保存执行记录
		ScheduleJobLog jobLog = new ScheduleJobLog();
		jobLog.setJobId(scheduleJob.getJobId());
		jobLog.setBeanName(scheduleJob.getBeanName());
		jobLog.setMethodName(scheduleJob.getMethodName());
		jobLog.setParams(scheduleJob.getParams());
		jobLog.setCreateTime(new Date());

		//任务开始时间
		long startTime = SystemClock.now();

		try {
			//执行任务
			log.info("任务准备执行，任务ID：" + scheduleJob.getJobId());

			SpringBeanTaskUtil.invokeMethod(scheduleJob);

			//任务执行总时长
			long times = SystemClock.now() - startTime;
			jobLog.setTimes((int)times);
			jobLog.setStatus(1);
			StringBuilder sb = new StringBuilder();

			log.info(sb.append("任务执行完毕，任务ID：").append(jobLog.getJobId()).append("  总共耗时：").append(times).append("毫秒").toString());
		} catch (Exception e) {
			log.error("任务执行失败，任务ID：" + scheduleJob.getJobId(), e);

			//任务执行总时长
			long times = SystemClock.now() - startTime;
			jobLog.setTimes((int)times);
			jobLog.setStatus(0);
			jobLog.setError(StrUtil.sub(e.toString(), 0, 2000));
		}finally {
			scheduleJobLogService.insertScheduleJobLog(jobLog);
		}
		scheduleJobLogService.insertScheduleJobLog(jobLog);

	}
}
