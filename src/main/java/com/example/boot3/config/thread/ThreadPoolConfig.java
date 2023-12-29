package com.example.boot3.config.thread;

import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 */
@Slf4j
@EnableAsync
@Configuration
@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
public class ThreadPoolConfig {

    @Bean
    @ConditionalOnMissingBean(ThreadPoolExecutor.class)
    public ExecutorService threadPoolExecutor(ThreadPoolConfigProperties properties) {
        // 实例化策略
        RejectedExecutionHandler handler = switch (properties.getPolicy()) {
            case "DiscardPolicy" -> new ThreadPoolExecutor.DiscardPolicy();
            case "DiscardOldestPolicy" -> new ThreadPoolExecutor.DiscardOldestPolicy();
            case "CallerRunsPolicy" -> new ThreadPoolExecutor.CallerRunsPolicy();
            default -> new ThreadPoolExecutor.AbortPolicy();
        };
        ExecutorService threadPoolExecutor = new ThreadPoolExecutor(properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(properties.getBlockQueueSize()),
                Executors.defaultThreadFactory(),
                handler);
        // 创建线程池
        threadPoolExecutor = TtlExecutors.getTtlExecutorService(threadPoolExecutor);
        return threadPoolExecutor;
    }

}
