package com.maestro.etopup.util;

import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

public class PinlessSchedulerServlet extends GenericServlet
{
  public PinlessSchedulerServlet()
  {
    System.out.println("PinlessSchedulerServlet constructor ....");
  }

  public void init(ServletConfig paramServletConfig)
    throws ServletException
  {
    super.init(paramServletConfig);
    try
    {
		String str = getInitParameter("cronExprPinless");
		CronTrigger localCronTrigger1 = new CronTrigger("ETopUpSMSPinless", "ETopUpSMSPinless");
		localCronTrigger1.setCronExpression(str);
		JobDetail localJobDetail1 = new JobDetail("ETopUpSMSPinless", "ETopUpSMSPinless",ETopUpSMSPinless.class);
		Scheduler localScheduler1 = StdSchedulerFactory.getDefaultScheduler();
		localScheduler1.scheduleJob(localJobDetail1, localCronTrigger1);


    }
    catch (Exception localException)
    {
      System.out.println("Caught Exception :: during Scheduling ETopUpSMSPinless Job");
      localException.printStackTrace();
    }
  }

  public void service(ServletRequest paramServletRequest, ServletResponse paramServletResponse)
    throws ServletException, IOException
  {
  }
}

