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
import javax.servlet.http.*;
import java.util.*;
import java.text.*;
import java.util.StringTokenizer;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import java.net.URL;
import com.maestro.util.URLReader;

public class TransferRefund implements Job
{
	public void execute(JobExecutionContext paramJobExecutionContext) throws JobExecutionException
	{
		refund();
	}

	public void refund()
	{

		ConnectDB con = null;
		java.sql.Statement stmt = null;
		ResultSet rs = null;
		try
		{
			con = new ConnectDB();
			stmt = con.connect();
			System.out.println("Refund Started...");
			OracleCallableStatement ps = (OracleCallableStatement) stmt.getConnection().prepareCall( "BEGIN PROC_TRANSFER_REFUND(); END;" );

			ps.execute();
			System.out.println("Refund finished...");
			stmt.getConnection().commit();

		} catch (SQLException localSQLException1)
		{
			System.out.print("TransferRefund Job failed: " + localSQLException1.getMessage());
			try
			{
				if(stmt != null) stmt.getConnection().rollback();
			}catch(Exception e)
			{}
		} catch (Exception localException)
		{
			System.out.print("TransferRefund Job failed: " + localException.getMessage());
			try
			{
				if(stmt != null) stmt.getConnection().rollback();
			}catch(Exception e)
			{}

		} finally
		{
			try
			{
				if (rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(con !=null)	con.close();
			}
			catch (SQLException localSQLException2)
			{
			}
		}
	}
}