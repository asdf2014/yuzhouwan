package com.yuzhouwan.hacker.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * 功能描述：quartz
 *
 * @author Benedict Jin
 * @since 2015/11/4
 */
public class JobExecutorWithQuartz implements Job {

    private static final Logger _log = LoggerFactory.getLogger(JobExecutorWithQuartz.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
    private static Scheduler scheduler;

    private static void init() {

        SchedulerFactory sf = new StdSchedulerFactory();
        try {
            scheduler = sf.getScheduler();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) throws Exception {

        init();

        // define the job and tie it to our HelloJob class
        JobDetail job = newJob(JobExecutorWithQuartz.class)
                .withIdentity("job1", "group1")
                .build();

        // compute a time that is on the next round minute
        Date runTime = evenMinuteDate(dateFormat.parse("2015-11-04/13:04:59"));

        // Trigger the job to run on the next round minute
        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(runTime)
                .build();

        // Tell quartz to schedule the job using our trigger
        scheduler.scheduleJob(job, trigger);

        scheduler.start();

        Thread.sleep(1000);

        scheduler.shutdown(true);
    }

    @Override
    public void execute(JobExecutionContext context) {

        // Say Hello to the World and display the date/time
        _log.info("Hello World! - " + new Date());

        //asdf2014 in time:	2015-11-04/13:09:54
        System.out.println("asdf2014 in time:\t" + dateFormat.format(new Date()));
    }
}
