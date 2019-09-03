package snod.com.cn.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import snod.com.cn.entity.ScheduleJob;
import snod.com.cn.event.ScheduleJobEvent;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 该类将会被org.springframework.scheduling.quartz.SpringBeanJobFactory 实例化
 * 并使@Autowired 生效
 */
@Slf4j
@DisallowConcurrentExecution
public class QuartzJob implements Job {

	@Autowired
	private ApplicationEventPublisher publisher;

	/**
	 * 任务调度参数key
	 */
	public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

	@Override
	@SneakyThrows
	public void execute(JobExecutionContext jobExecutionContext) {
		ScheduleJob sysJob = (ScheduleJob) jobExecutionContext.getMergedJobDataMap().get(JOB_PARAM_KEY);
		publisher.publishEvent(new ScheduleJobEvent(sysJob));
	}

}
