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

public class ETopUpSchedulerServlet extends GenericServlet
{
  public ETopUpSchedulerServlet()
  {
    System.out.println("ETopUpSchedulerServlet constructor ....");
  }

  public void init(ServletConfig paramServletConfig)
    throws ServletException
  {
    super.init(paramServletConfig);
    try
    {
		String str = getInitParameter("cronExprETopUp");
		CronTrigger localCronTrigger1 = new CronTrigger("ETopUp", "ETopUp");
		localCronTrigger1.setCronExpression(str);
		JobDetail localJobDetail1 = new JobDetail("ETopUp", "ETopUp", ETopUp.class);
		Scheduler localScheduler1 = StdSchedulerFactory.getDefaultScheduler();
		localScheduler1.scheduleJob(localJobDetail1, localCronTrigger1);

/*
		str = getInitParameter("cronExpr2");
		CronTrigger localCronTrigger2 = new CronTrigger("ETopUpSubmit", "ETopUpSubmit");
		localCronTrigger2.setCronExpression(str);
		JobDetail localJobDetail2 = new JobDetail("ETopUpSubmit", "ETopUpSubmit", ETopUpSubmit.class);
		Scheduler localScheduler2 = StdSchedulerFactory.getDefaultScheduler();
		localScheduler2.scheduleJob(localJobDetail2, localCronTrigger2);
*/

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

