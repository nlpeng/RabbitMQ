package com.nlpeng.config.task;

/**
 * @author Ferry NLP
 * @create 2019-09-05
 * @see
 * @since 1.0v
 **/
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling//定时任务
public class TaskSchedulerConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
    }

    @Bean(destroyMethod="shutdown")
    public Executor taskScheduler(){
        return Executors.newScheduledThreadPool(100);//创建一个线程池
    }

}