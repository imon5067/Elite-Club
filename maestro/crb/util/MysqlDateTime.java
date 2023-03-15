package com.maestro.crb.util;

import java.util.*;

public class MysqlDateTime
{

	public static String getMysqlDateTime(String date)
		{
			//System.out.println(date);
		StringTokenizer st = new StringTokenizer(date,"=");
		st.nextToken();
		String s1 = st.nextToken();
		String month="";
		String year ="";
		String datePart = "";
		String time = "";

		if(s1.indexOf(".")>0) // from cr
		{
			st = new StringTokenizer(s1,".");
			time = st.nextToken();
			s1 = st.nextToken();
			st = new StringTokenizer(s1," ");
			st.nextToken(); // .930
			st.nextToken(); // UTC
			st.nextToken(); // Tue
			month = st.nextToken();
			datePart = st.nextToken();
			year = st.nextToken();
		}
		else // from asterisk
		{
			st = new StringTokenizer(s1," ");
			time = st.nextToken(); // time
			st.nextToken(); // UTC
			st.nextToken(); // Tue
			month = st.nextToken();
			datePart = st.nextToken();
			year = st.nextToken();
		}

		if(month.equals("Jan")) month = "01";
		else if(month.equals("Feb")) month = "02";
		else if(month.equals("Mar")) month = "03";
		else if(month.equals("Apr")) month = "04";
		else if(month.equals("May")) month = "05";
		else if(month.equals("Jun")) month = "06";
		else if(month.equals("Jul")) month = "07";
		else if(month.equals("Aug")) month = "08";
		else if(month.equals("Sep")) month = "09";
		else if(month.equals("Oct")) month = "10";
		else if(month.equals("Nov")) month = "11";
		else if(month.equals("Dec")) month = "12";

		String datetime = year+"-"+month+"-"+datePart+" "+time;
		return datetime;
	}


}
