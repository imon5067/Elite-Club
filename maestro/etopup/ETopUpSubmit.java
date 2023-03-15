package com.maestro.etopup.util;

import com.maestro.crb.orig_billing.ConnectDB;
import com.maestro.crb.orig_billing.info.Inf;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.net.URL;
import com.maestro.util.URLReader;

public class ETopUpSubmit
  implements Job
{
  public void execute(JobExecutionContext paramJobExecutionContext)
    throws JobExecutionException
  {
    eTopSubmit();
  }

  public void eTopSubmit()
  {
    boolean bool = false;
	ConnectDB con = null;
    java.sql.Statement stmt = null;
    java.sql.Statement stmt1 = null;
    ResultSet rs = null;
    String qry = "";
    String responseStr = "";
    String msgid="";
    String mobileNo="";
    String theAmount="";

    try
    {
		con = new ConnectDB();
		stmt = con.connect();
		stmt1 = con.connect();

		qry ="Select msgid, dst_no, amount, response from topup_request where processed is null and lower(response) like '%error=6%' and topup_request_time >= sysdate - 10";
		System.out.println(qry);
		rs = stmt.executeQuery(qry);
		while(rs.next())
		{
			msgid=rs.getString(1);
			mobileNo = rs.getString(2);
			theAmount = rs.getString(3);
			responseStr = rs.getString(4);
			qry = "Update topup_request set response=null where msgid = '"+msgid+"'";
			System.out.println(qry);
			stmt1.executeUpdate(qry);
			System.out.println("::http://108.166.202.218/m.etopup/mobile/flexi.jsp?Request=TopUp&CountryCode=BD&UserId=hasib&MobileNumber="+mobileNo+"&Amount="+theAmount+"&NumberType=1");
			responseStr = URLReader.readURL(new URL("http://108.166.202.218/m.etopup/mobile/flexi.jsp?Request=TopUp&CountryCode=BD&UserId=hasib&MobileNumber="+mobileNo+"&Amount="+theAmount+"&NumberType=1"));
			if (responseStr==null) responseStr = "System Error";
			else responseStr = responseStr.trim();
			qry = "Update topup_request set response='"+responseStr+"' where msgid = '"+msgid+"'";
			System.out.println(qry);
			stmt1.executeUpdate(qry);


		}
		stmt1.getConnection().commit();

    }
    catch (SQLException localSQLException1)
    {
      System.out.print("eTopup Job failed: " + localSQLException1.getMessage());
    }
    catch (Exception localException)
    {
      System.out.print("eTopup Job failed: " + localException.getMessage());
    }
    finally
    {
      try
      {
        if (rs != null) rs.close();
		if(stmt != null) stmt.close();
		if(stmt1 != null) stmt1.close();
		if(con !=null)	con.close();
      }
      catch (SQLException localSQLException2)
      {
      }
    }
  }
}