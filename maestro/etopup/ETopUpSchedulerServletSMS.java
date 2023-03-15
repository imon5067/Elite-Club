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

public class ETopUpSchedulerServletSMS extends GenericServlet
{
  public ETopUpSchedulerServletSMS()
  {
    System.out.println("ETopUpSchedulerServletSMS constructor ....");
  }

  public void init(ServletConfig paramServletConfig)
    throws ServletException
  {
    super.init(paramServletConfig);
    try
    {
		String str = "";

		str = getInitParameter("cronExprETopUpSMS_ACC");
		CronTrigger localCronTrigger2 = new CronTrigger("ETopUpSMSACC", "ETopUpSMSACC");
		localCronTrigger2.setCronExpression(str);
		JobDetail localJobDetail2 = new JobDetail("ETopUpSMSACC", "ETopUpSMSACC",ETopUpSMSACC.class);
		Scheduler localScheduler2 = StdSchedulerFactory.getDefaultScheduler();
		localScheduler2.scheduleJob(localJobDetail2, localCronTrigger2);

    }
    catch (Exception localException)
    {
      System.out.println("Caught Exception :: during Scheduling eTopUp Job");
      localException.printStackTrace();
    }
  }

  public void service(ServletRequest paramServletRequest, ServletResponse paramServletResponse)
    throws ServletException, IOException
  {
  }
}

