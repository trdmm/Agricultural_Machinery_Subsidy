package com.wdnj.xxb.subsidy.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 描述: Scheduler 定时任务线程池<br/>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2021-05-24 09:21
 */
@Configuration
public class TaskScheedulerConfig {
    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(2);
        return taskScheduler;
    }

}
