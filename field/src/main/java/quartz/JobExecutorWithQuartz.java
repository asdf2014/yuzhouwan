package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * 功能描述：quartz
 *
 * @author asdf2014
 * @since 2015/11/4
 */
public class JobExecutorWithQuartz implements Job {

    private final static Logger _log = LoggerFactory.getLogger(JobExecutorWithQuartz.class);
    private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
    private static Scheduler scheduler;


    private static void init() {

        SchedulerFactory sf = new StdSchedulerFactory();
        try {
            scheduler = sf.getScheduler();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        // Say Hello to the World and display the date/time
        _log.info("Hello World! - " + new Date());

        /**
         * asdf2014 in time:	2015-11-04/13:09:54
         */
        System.out.println("asdf2014 in time:\t" + dateFormater.format(new Date()));
    }

    public static void main(String... args) throws SchedulerException, InterruptedException, ParseException {

        init();

        // define the job and tie it to our HelloJob class
        JobDetail job = newJob(JobExecutorWithQuartz.class)
                .withIdentity("job1", "group1")
                .build();

        // compute a time that is on the next round minute
        Date runTime = evenMinuteDate(dateFormater.parse("2015-11-04/13:04:59"));

        // Trigger the job to run on the next round minute
        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(runTime)
                .build();

        // Tell quartz to schedule the job using our trigger
        scheduler.scheduleJob(job, trigger);

        scheduler.start();

        Thread.sleep(1L * 1000L);

        scheduler.shutdown(true);
    }

}
