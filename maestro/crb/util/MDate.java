package com.maestro.crb.util;

import com.maestro.crb.orig_billing.info.Inf;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class MDate
{
  public static String getCurDate_old()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    int i = localGregorianCalendar.get(1);
    int j = localGregorianCalendar.get(2) + 1;
    int k = localGregorianCalendar.get(5);
    String str1 = "" + k;
    String str2 = "" + j;
    if (j < 10)
      str2 = "0" + j;
    if (k < 10)
      str1 = "0" + k;
    return "" + i + "-" + str2 + "-" + str1;
  }

  public static String getCurDate()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    int i = localGregorianCalendar.get(1);
    int j = localGregorianCalendar.get(2) + 1;
    int k = localGregorianCalendar.get(5);
    String str1 = "" + k;
    String str2 = "" + j;
    if (j < 10)
      str2 = "0" + j;
    if (k < 10)
      str1 = "0" + k;
    String str3 = "";
    String str4 = MDateFormat.getOraMonth(j);
    String str5 = Integer.toString(i);
    str5 = str5.substring(2, 4);
    if (Inf.getDateFormat().equals("rrrr-mm-dd"))
      str3 = i + "-" + str2 + "-" + str1;
    else if (Inf.getDateFormat().equals("dd-mm-rrrr"))
      str3 = str1 + "-" + str2 + "-" + i;
    else if (Inf.getDateFormat().equals("mm-dd-rrrr"))
      str3 = str2 + "-" + str1 + "-" + i;
    else if (Inf.getDateFormat().equals("dd-mon-yy"))
      str3 = str1 + "-" + str4 + "-" + str5;
    else
      str3 = str1 + "-" + str2 + "-" + i;
    return str3;
  }

  public static String getMonth(int paramInt)
  {
    String str = "";
    if (paramInt == 1)
      str = "Jan";
    else if (paramInt == 2)
      str = "Feb";
    else if (paramInt == 3)
      str = "Mar";
    else if (paramInt == 4)
      str = "Apr";
    else if (paramInt == 5)
      str = "May";
    else if (paramInt == 6)
      str = "Jun";
    else if (paramInt == 7)
      str = "Jul";
    else if (paramInt == 8)
      str = "Aug";
    else if (paramInt == 9)
      str = "Sep";
    else if (paramInt == 10)
      str = "Oct";
    else if (paramInt == 11)
      str = "Nov";
    else if (paramInt == 12)
      str = "Dec";
    return str;
  }

  public static String getformattedCurDate()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    int i = localGregorianCalendar.get(1);
    int j = localGregorianCalendar.get(2) + 1;
    int k = localGregorianCalendar.get(5);
    String str1 = "" + k;
    String str2 = null;
    if (k < 10)
      str1 = "0" + k;
    str2 = getMonth(j);
    return "" + str1 + "/" + str2 + "/" + i;
  }

  public static String getFirstDate()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    int i = localGregorianCalendar.get(1);
    int j = localGregorianCalendar.get(2) + 1;
    int k = localGregorianCalendar.get(5);
    String str1 = "" + k;
    String str2 = "" + j;
    if (j < 10)
      str2 = "0" + j;
    if (k < 10)
      str1 = "0" + k;
    return "" + i + "-" + str2 + "-01";
  }

  public static String getOracleDateTime(String paramString)
  {
    if ((null != paramString) && (!"".equals(paramString)))
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "=");
      localStringTokenizer.nextToken();
      String str1 = localStringTokenizer.nextToken();
      localStringTokenizer = new StringTokenizer(str1, " ");
      String str2 = localStringTokenizer.nextToken();
      localStringTokenizer.nextToken();
      localStringTokenizer.nextToken();
      String str3 = localStringTokenizer.nextToken();
      if (str3.equals("Jan"))
        str3 = "01";
      else if (str3.equals("Feb"))
        str3 = "02";
      else if (str3.equals("Mar"))
        str3 = "03";
      else if (str3.equals("Apr"))
        str3 = "04";
      else if (str3.equals("May"))
        str3 = "05";
      else if (str3.equals("Jun"))
        str3 = "06";
      else if (str3.equals("Jul"))
        str3 = "07";
      else if (str3.equals("Aug"))
        str3 = "08";
      else if (str3.equals("Sep"))
        str3 = "09";
      else if (str3.equals("Oct"))
        str3 = "10";
      else if (str3.equals("Nov"))
        str3 = "11";
      else if (str3.equals("Dec"))
        str3 = "12";
      String str4 = localStringTokenizer.nextToken();
      String str5 = localStringTokenizer.nextToken();
      String str6 = str5 + "-" + str3 + "-" + str4 + " " + str2;
      return str6;
    }
    return "";
  }

  public static String getDifDate(int paramInt)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.add(5, paramInt);
    int i = localGregorianCalendar.get(1);
    int j = localGregorianCalendar.get(2) + 1;
    int k = localGregorianCalendar.get(5);
    String str1 = "" + k;
    String str2 = "" + j;
    if (j < 10)
      str2 = "0" + j;
    if (k < 10)
      str1 = "0" + k;
    String str3 = "";
    String str4 = MDateFormat.getOraMonth(j);
    String str5 = Integer.toString(i);
    str5 = str5.substring(2, 4);
    if (Inf.getDateFormat().equals("rrrr-mm-dd"))
      str3 = i + "-" + str2 + "-" + str1;
    else if (Inf.getDateFormat().equals("dd-mm-rrrr"))
      str3 = str1 + "-" + str2 + "-" + i;
    else if (Inf.getDateFormat().equals("mm-dd-rrrr"))
      str3 = str2 + "-" + str1 + "-" + i;
    else if (Inf.getDateFormat().equals("dd-mon-yy"))
      str3 = str1 + "-" + str4 + "-" + str5;
    else
      str3 = str1 + "-" + str2 + "-" + i;
    return str3;
  }

  public static String getDifDate(String paramString, int paramInt)
  {
    Date localDate = stringToDate(paramString);
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(localDate);
    localGregorianCalendar.add(5, paramInt);
    int i = localGregorianCalendar.get(1);
    int j = localGregorianCalendar.get(2) + 1;
    int k = localGregorianCalendar.get(5);
    String str1 = "" + k;
    String str2 = "" + j;
    if (j < 10)
      str2 = "0" + j;
    if (k < 10)
      str1 = "0" + k;
    String str3 = "";
    String str4 = MDateFormat.getOraMonth(j);
    String str5 = Integer.toString(i);
    str5 = str5.substring(2, 4);
    if (Inf.getDateFormat().equals("rrrr-mm-dd"))
      str3 = i + "-" + str2 + "-" + str1;
    else if (Inf.getDateFormat().equals("dd-mm-rrrr"))
      str3 = str1 + "-" + str2 + "-" + i;
    else if (Inf.getDateFormat().equals("mm-dd-rrrr"))
      str3 = str2 + "-" + str1 + "-" + i;
    else if (Inf.getDateFormat().equals("dd-mon-yy"))
      str3 = str1 + "-" + str4 + "-" + str5;
    else
      str3 = str1 + "-" + str2 + "-" + i;
    return str3;
  }

  public static boolean isValidDate(String paramString)
  {
    boolean i = true;
    String str = getJavaDateformat();
    try
    {
      GregorianCalendar localGregorianCalendar = new GregorianCalendar();
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(str);
      localSimpleDateFormat.setLenient(false);
      localGregorianCalendar.setTime(localSimpleDateFormat.parse(paramString));
    }
    catch (Exception localException)
    {
      i = false;
    }
    return i;
  }

  public static String getJavaDateformat()
  {
    String str1 = Inf.getDateFormat();
    String str2 = "";
    str2 = str1.replaceAll("mon", "MMM");
    str2 = str2.replaceAll("m", "M");
    str2 = str2.replaceAll("r", "y");
    return str2;
  }

  public static Date stringToDate(String paramString)
  {
    Date localDate = null;
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(getJavaDateformat());
    try
    {
      localDate = localSimpleDateFormat.parse(paramString);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localDate;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.util.MDate
 * JD-Core Version:    0.6.0
 */