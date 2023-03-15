package com.maestro.rateserver.util;

import java.io.PrintStream;
import java.text.NumberFormat;

public class MNumberFormat
{
  public static String formatDouble(String paramString)
  {
    if (paramString == null)
      return "";
    try
    {
      paramString = "" + Double.parseDouble(paramString.trim());
    }
    catch (Exception localException)
    {
      System.out.println("Error in MNumberFormat() = " + localException.getMessage());
    }
    String str1 = "" + paramString;
    int i = str1.length();
    if (str1.charAt(i - 2) == '.')
      str1 = str1 + "0";
    if (str1.charAt(i - 1) == '.')
      str1 = str1 + "00";
    if (str1.indexOf('.') < 0)
      str1 = str1 + ".00";
    if (str1.indexOf('.') < i - 3)
      str1 = str1.substring(0, str1.indexOf('.') + 3);
    i = str1.length();
    if (i <= 6)
      return str1;
    String str2 = str1.substring(i - 6, i);
    if (i == 7)
      str2 = "" + str1.charAt(0) + "," + str2;
    if (i == 8)
      str2 = "" + str1.substring(0, 2) + "," + str2;
    if (i == 9)
      str2 = "" + str1.charAt(0) + "," + str1.substring(1, 3) + "," + str2;
    if (i == 10)
      str2 = "" + str1.substring(0, 2) + "," + str1.substring(2, 4) + "," + str2;
    if (i == 11)
      str2 = "" + str1.charAt(0) + "," + str1.substring(1, 3) + "," + str1.substring(3, 5) + "," + str2;
    if (i == 12)
      str2 = "" + str1.substring(0, 2) + "," + str1.substring(2, 4) + "," + str1.substring(4, 6) + "," + str2;
    if (i == 13)
      str2 = "" + str1.charAt(0) + "," + str1.substring(1, 3) + "," + str1.substring(3, 5) + "," + str1.substring(5, 7) + "," + str2;
    if (i == 14)
      str2 = "" + str1.substring(0, 2) + "," + str1.substring(2, 4) + "," + str1.substring(4, 6) + "," + str1.substring(6, 8) + "," + str2;
    return str2;
  }

  public static String format(String paramString)
  {
    String str = "";
    double d = 0.0D;
    if (paramString == null)
      return str;
    try
    {
      d = Double.parseDouble(paramString);
    }
    catch (Exception localException)
    {
      return str;
    }
    NumberFormat localNumberFormat = NumberFormat.getInstance();
    localNumberFormat.setGroupingUsed(true);
    localNumberFormat.setMaximumFractionDigits(2);
    localNumberFormat.setMinimumFractionDigits(2);
    str = localNumberFormat.format(d);
    return str;
  }

  public static String format(String paramString, int paramInt)
  {
    String str = "";
    double d = 0.0D;
    if (paramString == null)
      return str;
    try
    {
      d = Double.parseDouble(paramString);
    }
    catch (Exception localException)
    {
      return str;
    }
    NumberFormat localNumberFormat = NumberFormat.getInstance();
    localNumberFormat.setGroupingUsed(true);
    localNumberFormat.setMaximumFractionDigits(paramInt);
    localNumberFormat.setMinimumFractionDigits(paramInt);
    str = localNumberFormat.format(d);
    return str;
  }

  public static String format(double paramDouble)
  {
    String str = "";
    NumberFormat localNumberFormat = NumberFormat.getInstance();
    localNumberFormat.setGroupingUsed(true);
    localNumberFormat.setMaximumFractionDigits(2);
    localNumberFormat.setMinimumFractionDigits(2);
    str = localNumberFormat.format(paramDouble);
    return str;
  }

  public static String format(double paramDouble, int paramInt)
  {
    String str = "";
    NumberFormat localNumberFormat = NumberFormat.getInstance();
    localNumberFormat.setGroupingUsed(true);
    localNumberFormat.setMaximumFractionDigits(paramInt);
    localNumberFormat.setMinimumFractionDigits(paramInt);
    str = localNumberFormat.format(paramDouble);
    return str;
  }

  public static String getTimeInHMS(int paramInt)
  {
    String str = "";
    if (paramInt < 0)
      paramInt = 0;
    int i;
    int j;
    int k;
    int m;
    if (paramInt >= 3600)
    {
      i = paramInt / 3600;
      j = paramInt % 3600;
      k = j / 60;
      m = j % 60;
      if (i < 10)
        str = str + "0" + Integer.toString(i) + ":";
      else
        str = str + Integer.toString(i) + ":";
      if (k < 10)
        str = str + "0" + Integer.toString(k) + ":";
      else
        str = str + Integer.toString(k) + ":";
      if ((m < 10) && (m > 0))
        str = str + "0" + Integer.toString(m);
      else if (m >= 10)
        str = str + Integer.toString(m);
      else
        str = str + "00";
    }
    else if (paramInt >= 60)
    {
      i = 0;
      j = paramInt;
      k = j / 60;
      m = j % 60;
      if (i < 10)
        str = str + "0" + Integer.toString(i) + ":";
      else
        str = str + Integer.toString(i) + ":";
      if (k < 10)
        str = str + "0" + Integer.toString(k) + ":";
      else
        str = str + Integer.toString(k) + ":";
      if ((m < 10) && (m > 0))
        str = str + "0" + Integer.toString(m);
      else if (m >= 10)
        str = str + Integer.toString(m);
      else
        str = str + "00";
    }
    else if ((paramInt < 10) && (paramInt > 0))
    {
      str = "00:00:0" + Integer.toString(paramInt);
    }
    else if (paramInt >= 10)
    {
      str = "00:00:" + Integer.toString(paramInt);
    }
    else
    {
      str = "00:00:00";
    }
    return str;
  }

  public static String format(String paramString, int paramInt, boolean paramBoolean)
  {
    String str = "";
    double d = 0.0D;
    if (paramString == null)
      return str;
    try
    {
      d = Double.parseDouble(paramString);
    }
    catch (Exception localException)
    {
      return str;
    }
    NumberFormat localNumberFormat = NumberFormat.getInstance();
    localNumberFormat.setGroupingUsed(paramBoolean);
    localNumberFormat.setMaximumFractionDigits(paramInt);
    localNumberFormat.setMinimumFractionDigits(paramInt);
    str = localNumberFormat.format(d);
    return str;
  }

  public static void main(String[] paramArrayOfString)
  {
    String str = formatDouble(paramArrayOfString[0]);
    System.out.println(str);
  }

  public static String nullChk(String paramString)
  {
    if ((paramString == null) || (paramString.equals("null")))
      paramString = "";
    return paramString;
  }

  public static String getValueAsStr(double paramDouble)
  {
    String str1 = "";
    String str2 = "";
    str1 = paramDouble + "";
    if (str1.indexOf("E") != -1)
    {
      int i = Integer.parseInt(str1.substring(str1.indexOf("E") + 1));
      String str3 = str1.substring(0, str1.indexOf("E"));
      int j = str3.length();
      for (int k = j; k <= i + 1; k++)
        str3 = str3 + "0";
      String str4 = str3.substring(0, str3.indexOf("."));
      String str5 = str3.substring(str3.indexOf(".") + 1);
      str2 = str4 + str5;
    }
    else
    {
      str2 = str1;
    }
    System.out.println("This is from MNumberFormat==getValueAsStr(Double)===" + str2);
    return str2;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.util.MNumberFormat
 * JD-Core Version:    0.6.0
 */