package com.zlsoft.bhjira.jobs;


import com.zlsoft.bhjira.services.impl.JiraServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JiraJobs {
    /**
     * 一分钟
     */
    public final static long ONE_Minute =  600 * 1000;
    @Autowired
    private JiraServiceImpl jiraService;

    /**
     * @Scheduled 定时任务
     * fixedDelay 好多毫秒触发一次
     * 3.fixedDelay 和fixedDelayString 表示一个固定延迟时间执行，上个任务完成后，延迟多长时间执行
     * 4.fixedRate 和fixedRateString表示一个固定频率执行，上个任务开始后，多长时间后开始执行
     */
    @Scheduled(fixedDelay=ONE_Minute)
    public void fixedDelayJob(){
//        System.out.println(Dates.format_yyyyMMddHHmmss(new Date())+" >>fixedDelay执行....");
        System.out.println(" >>fixedDelay执行....");
    }

    @Scheduled(fixedRate=ONE_Minute)
    public void fixedRateJob(){
//        System.out.println(Dates.format_yyyyMMddHHmmss(new Date())+" >>fixedRate执行....");
        System.out.println(" >>fixedRate执行....");

    }

    /**
     * 每天凌晨3点15分触发
     */
    @Scheduled(cron="0 15 3 * * ?")
    public void cronJob(){
//        System.out.println(Dates.format_yyyyMMddHHmmss(new Date())+" >>cron执行....");
    	System.out.println(" >>cron执行....");
    }





}











