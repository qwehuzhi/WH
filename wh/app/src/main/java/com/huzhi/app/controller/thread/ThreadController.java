package com.huzhi.app.controller.thread;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
@RestController
public class ThreadController {

    /**
     * 线程不安全
     * @return
     */
    @RequestMapping("/ceshi/builder")
    public String ceshiBuilder(){
        StringBuilder builder=new StringBuilder();
        ExecutorService executorService = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(200);
        CountDownLatch countDownLatch = new CountDownLatch(4000);
        for (int i = 0; i < 4000 ; i++) {
            //线程池提交一个任务，该方法返回void
            executorService.execute(() -> {
                try {
                    //阻塞，等待许可证
                    semaphore.acquire();
                    //StringBuilder计数
                    builder.append(1);
                    //添加许可证
                    semaphore.release();
                } catch (Exception e) {
                    return;
                }
                //用于使计数器减一，其一般是执行任务的线程调用
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        }catch (Exception e){
            return e.getMessage();
        }
        executorService.shutdown();
        Integer lengh=builder.length();
        return lengh.toString();
    }

    /**
     * 线程安全
     * @return
     */
    @RequestMapping("/ceshi/buffer")
    public String  ceshiBuffer(){
        StringBuffer buffer=new StringBuffer();//
        ExecutorService executorService = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(200);
        CountDownLatch countDownLatch = new CountDownLatch(4000);
        for (int i = 0; i < 4000 ; i++) {
            //线程池提交一个任务，该方法返回void
            executorService.execute(() -> {
                try {
                    //阻塞，等待许可证
                    semaphore.acquire();
                    //StringBuilder计数
                    buffer.append(1);
                    //添加许可证
                    semaphore.release();
                } catch (Exception e) {
                    return;
                }
                //用于使计数器减一，其一般是执行任务的线程调用
                countDownLatch.countDown();

            });
        }
        try {
            countDownLatch.await();
        }catch (Exception e){
            return e.getMessage();
        }
        executorService.shutdown();
        Integer lengh=buffer.length();
        return lengh.toString();
    }
}
