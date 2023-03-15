/*    */ package com.maestro.crb.util;
/*    */ 
/*    */ import java.util.StringTokenizer;
/*    */ 
/*    */ public class EqlSTokenize
/*    */ {
/*    */   public String getEqlSTokenize(String paramString)
/*    */   {
/* 11 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "=");
/*    */ 
/* 14 */     String str = paramString;
/* 15 */     if (localStringTokenizer.countTokens() == 2) {
/* 16 */       localStringTokenizer.nextToken();
/* 17 */       str = localStringTokenizer.nextToken();
/*    */     }
/*    */ 
/* 21 */     return str;
/*    */   }
/*    */ }

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.util.EqlSTokenize
 * JD-Core Version:    0.6.0
 */