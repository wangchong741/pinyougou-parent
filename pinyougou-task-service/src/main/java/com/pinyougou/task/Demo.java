package com.pinyougou.task;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务小demo
 * @author wangchong
 * 2019年2月19日
 */
@Component
public class Demo {
	
//	@Scheduled(cron="2/5 * * * * ?")
	public void refreshSeckillGoods(){
		System.out.println("执行了任务调度"+new Date());		
	}	

}
