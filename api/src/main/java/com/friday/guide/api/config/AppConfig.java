package com.friday.guide.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;

@Configuration
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = false)
@ComponentScan(basePackages = "com.friday.guide", excludeFilters = @ComponentScan.Filter({RestController.class, Configuration.class}))
@PropertySources({@PropertySource(value = "classpath:/persistence.properties", ignoreResourceNotFound = true), @PropertySource(value = "classpath:/system.properties", ignoreResourceNotFound = true)})
public class AppConfig {

    @Value("${general.max.pool.size:50}")
    private Integer generalMaxPoolSize;

    @Value("${general.core.pool.size:10}")
    private Integer generalCorePoolSize;

    @Value("${support.max.pool.size:20}")
    private Integer supportMaxPoolSize;

    @Value("${support.core.pool.size:1}")
    private Integer supportCorePoolSize;

    @Value("${task.scheduler.pool.size:20}")
    private Integer taskSchedulerPoolSize;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @Primary
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(generalCorePoolSize);
        executor.setMaxPoolSize(generalMaxPoolSize);
        executor.setThreadNamePrefix("asyncExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor supportExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(supportCorePoolSize);
        executor.setMaxPoolSize(supportMaxPoolSize);
        executor.setThreadNamePrefix("supportExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean(destroyMethod = "destroy")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(taskSchedulerPoolSize);
        return scheduler;
    }
}
