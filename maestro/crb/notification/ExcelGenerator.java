package com.maestro.crb.notification;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

public class ExcelGenerator
{
  public String generateRatePlan(RateChartDTO paramRateChartDTO, String paramString)
    throws ParsePropertyException, IOException
  {
    String str1 = "";
    System.out.println("1 " + paramRateChartDTO);
    System.out.println("1 " + paramRateChartDTO.getDataList());
    System.out.println("SIZE = " + paramRateChartDTO.getDataList().size());
    HashMap localHashMap = new HashMap();
    localHashMap.put("name", paramRateChartDTO.getName());
    localHashMap.put("ratechart", paramRateChartDTO.getDataList());
    XLSTransformer localXLSTransformer = new XLSTransformer();
    String str2 = paramString + File.separator + "template.xls";
    String str3 = "Ratechart_" + System.currentTimeMillis() + ".xls";
    localXLSTransformer.transformXLS(str2, localHashMap, paramString + File.separator + str3);
    return str3;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.notification.ExcelGenerator
 * JD-Core Version:    0.6.0
 */