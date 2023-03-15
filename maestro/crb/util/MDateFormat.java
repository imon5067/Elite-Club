package com.maestro.crb.util;

import java.io.PrintStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

public class MDateFormat
{
  public static String bdToMysqlFormat(String paramString)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "/");
    if (localStringTokenizer.countTokens() < 3)
      return null;
    String str1 = localStringTokenizer.nextToken();
    String str2 = localStringTokenizer.nextToken();
    String str3 = localStringTokenizer.nextToken();
    try
    {
      Integer.parseInt(str1);
      Integer.parseInt(str2);
      Integer.parseInt(str3);
    }
    catch (Exception localException)
    {
      return null;
    }
    if (str1.length() < 2)
      str1 = "0" + str1;
    if (str2.length() < 2)
      str2 = "0" + str2;
    str3 = "20" + str3;
    return str3 + '-' + str2 + '-' + str1;
  }

  public static String mysqlToBdFormat(String paramString)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "-");
    if (localStringTokenizer.countTokens() < 3)
      return null;
    String str1 = localStringTokenizer.nextToken();
    String str2 = localStringTokenizer.nextToken();
    String str3 = localStringTokenizer.nextToken();
    try
    {
      Integer.parseInt(str3);
      Integer.parseInt(str2);
      Integer.parseInt(str1);
    }
    catch (Exception localException)
    {
      return null;
    }
    if (str1.length() > 3)
      str1 = str1.substring(2);
    return str3 + "/" + str2 + "/" + str1;
  }

  public static String getDateBd()
  {
    DateFormat localDateFormat = DateFormat.getDateInstance(3, Locale.UK);
    String str = localDateFormat.format(new Date());
    return str;
  }

  public static String getDateUS()
  {
    DateFormat localDateFormat = DateFormat.getDateInstance(3, Locale.US);
    String str = localDateFormat.format(new Date());
    return str;
  }

  public static String getDateMysql()
  {
    DateFormat localDateFormat = DateFormat.getDateInstance(3, Locale.JAPAN);
    String str = localDateFormat.format(new Date());
    str = str.replace('/', '-');
    str = "20" + str;
    return str;
  }

  public static String getDatetimeMysql()
  {
    String str1 = "";
    String str2 = "";
    String str3 = "";
    String str4 = "";
    String str5 = "";
    try
    {
      DateFormat localDateFormat = DateFormat.getDateTimeInstance(3, 2, Locale.JAPAN);
      String str6 = localDateFormat.format(new Date());
      str6 = str6.replace('/', '-');
      str6 = "20" + str6;
      StringTokenizer localStringTokenizer = new StringTokenizer(str6, " ");
      str1 = localStringTokenizer.nextToken();
      String str7 = localStringTokenizer.nextToken();
      localStringTokenizer = new StringTokenizer(str7, ":");
      str3 = localStringTokenizer.nextToken();
      str4 = localStringTokenizer.nextToken();
      str5 = localStringTokenizer.nextToken();
      if (str3.length() < 2)
        str3 = "0" + str3;
    }
    catch (Exception localException)
    {
      System.out.println("getDatatimeMysql " + localException.getMessage());
    }
    return str1 + " " + str3 + ":" + str4 + ":" + str5;
  }

  public static String mysqlDateTime(long paramLong)
  {
    String str1 = "";
    try
    {
      DateFormat localDateFormat = DateFormat.getDateTimeInstance(3, 2, Locale.JAPAN);
      str1 = localDateFormat.format(new Date(paramLong));
      String str2 = str1.substring(0, str1.indexOf(' '));
      String str3 = str1.substring(str1.indexOf(' ') + 1);
      String str4 = str3.substring(0, str3.indexOf(':'));
      if (str4.length() < 2)
        str3 = "0" + str3;
      str1 = "20" + str2.replace('/', '-') + " " + str3;
      System.out.println(str1);
    }
    catch (Exception localException)
    {
      System.out.println(">>>In mySqlDateTime(long ts)  " + localException.getMessage());
    }
    return str1;
  }

  public static String bdToOracleFormat(String paramString)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "/");
    if (localStringTokenizer.countTokens() < 3)
      return null;
    String str1 = localStringTokenizer.nextToken();
    String str2 = localStringTokenizer.nextToken();
    String str3 = localStringTokenizer.nextToken();
    int i = -1;
    try
    {
      Integer.parseInt(str1);
      i = Integer.parseInt(str2);
      Integer.parseInt(str3);
    }
    catch (Exception localException)
    {
      return null;
    }
    str2 = getOraMonth(i);
    if (str2 == null)
      return null;
    if (str3.length() != 4)
      return null;
    if (str1.length() < 2)
      str1 = "0" + str1;
    return str1 + '-' + str2 + '-' + str3;
  }

  static String getOraMonth(int paramInt)
  {
    switch (paramInt)
    {
    case 1:
      return "JAN";
    case 2:
      return "FEB";
    case 3:
      return "MAR";
    case 4:
      return "APR";
    case 5:
      return "MAY";
    case 6:
      return "JUN";
    case 7:
      return "JUL";
    case 8:
      return "AUG";
    case 9:
      return "SEP";
    case 10:
      return "OCT";
    case 11:
      return "NOV";
    case 12:
      return "DEC";
    }
    return null;
  }

  public static void main(String[] paramArrayOfString)
  {
    System.out.println(bdToOracleFormat("19/4/2002"));
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.util.MDateFormat
 * JD-Core Version:    0.6.0
 */