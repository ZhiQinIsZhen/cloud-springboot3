package com.liyz.cloud.common.util;

import com.liyz.cloud.common.exception.CommonExceptionCodeEnum;
import com.liyz.cloud.common.exception.RemoteServiceException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * DESC:虚拟线程工具类
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025/1/4 16:00
 */
@Slf4j
@UtilityClass
public class VirtualThreadUtil {

    /**
     * 提交任务
     *
     * @param task 任务
     */
    public static void submit(Runnable task) {
        try(ExecutorService es = Executors.newVirtualThreadPerTaskExecutor()) {
            es.submit(task);
        }
    }

    /**
     * 提交多个任务
     *
     * @param tasks 任务
     */
    public static void submit(List<Runnable> tasks) {
        try(ExecutorService es = Executors.newVirtualThreadPerTaskExecutor()) {
            tasks.forEach(es::submit);
        }
    }

    /**
     * 提交任务
     *
     * @param tasks 任务
     */
    public static void submit(Runnable... tasks) {
        try(ExecutorService es = Executors.newVirtualThreadPerTaskExecutor()) {
            Arrays.stream(tasks).forEach(es::submit);
        }
    }

    /**
     * 提交任务
     *
     * @param task 任务
     * @param result 结果
     * @return 结果
     * @param <T> 结果泛型
     */
    public static <T> T submitResult(Runnable task, T result) {
        try(ExecutorService es = Executors.newVirtualThreadPerTaskExecutor()) {
            try {
                return es.submit(task, result).get();
            } catch (InterruptedException e) {
                throw new RemoteServiceException(CommonExceptionCodeEnum.THREAD_INTERRUPTED);
            } catch (ExecutionException e) {
                log.error("virtual thread submit error", e);
                throw new RemoteServiceException(CommonExceptionCodeEnum.EXECUTION_EXCEPTION);
            }
        }
    }

    /**
     * 提交任务
     *
     * @param task 任务
     * @return 结果
     * @param <T> 泛型
     */
    public static <T> T submit(Callable<T> task) {
        try(ExecutorService es = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<T> submit = es.submit(task);
            try {
                return submit.get();
            } catch (InterruptedException e) {
                throw new RemoteServiceException(CommonExceptionCodeEnum.THREAD_INTERRUPTED);
            } catch (ExecutionException e) {
                log.error("virtual thread submit error", e);
                throw new RemoteServiceException(CommonExceptionCodeEnum.EXECUTION_EXCEPTION);
            }
        }
    }
}
