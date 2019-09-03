package snod.com.cn.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;

import snod.com.cn.common.PageParam;
import snod.com.cn.entity.ScheduleJobLog;
import snod.com.cn.service.ScheduleJobLogService;

/**
 * 定时任务日志
 * @author lgh
 */
@RestController
@RequestMapping("/sys/scheduleLog")
public class ScheduleJobLogController {
	@Autowired
	private ScheduleJobLogService scheduleJobLogService;
	
	/**
	 * 定时任务日志列表
	 */
	@GetMapping("/page")
	public ResponseEntity<IPage<ScheduleJobLog>> page(Long jobId,PageParam<ScheduleJobLog> page){
		IPage<ScheduleJobLog> list = scheduleJobLogService.queryListScheduleJobLog(page,jobId);
		return ResponseEntity.ok(list);
	}
}
