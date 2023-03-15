/*      */ package com.maestro.rateserver.dao;
/*      */
/*      */ import com.maestro.crb.orig_billing.Connector;
/*      */ import com.maestro.crb.orig_billing.MaestroException;
/*      */ import com.maestro.rateserver.model.BalanceSheet;
/*      */ import com.maestro.rateserver.model.FinanceDayModel;
/*      */ import com.maestro.rateserver.model.FinanceModel;
/*      */ import java.io.PrintStream;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Timestamp;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */
/*      */ public class FinanceDAO
/*      */ {
/*   27 */   Statement stmt = null;
/*      */
/*      */   public FinanceDAO(Statement paramStatement)
/*      */   {
/*   32 */     this.stmt = paramStatement;
/*      */   }
/*      */
/*      */   public FinanceModel getGroups(Date paramDate1, Date paramDate2, String paramString1, String paramString2, String paramString3)
/*      */     throws SQLException
/*      */   {
/*   45 */     FinanceModel localFinanceModel = new FinanceModel();
/*   46 */     Hashtable localHashtable = new Hashtable();
/*      */
/*   48 */     Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
/*   49 */     Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
/*   50 */     int i = 0;
/*      */
/*   52 */     Calendar localCalendar = Calendar.getInstance();
/*   53 */     localCalendar.setTime(paramDate1);
/*   54 */     SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
/*   55 */     Date localDate1 = localCalendar.getTime();
/*   56 */     FinanceDayModel localFinanceDayModel = null;
/*   57 */     while (localDate1.before(paramDate2)) {
/*   58 */       localFinanceDayModel = new FinanceDayModel();
/*   59 */       localHashtable.put(localSimpleDateFormat.format(localDate1), localFinanceDayModel);
/*   60 */       localCalendar.add(5, 1);
/*   61 */       localCalendar.set(12, 0);
/*   62 */       localCalendar.set(13, 0);
/*   63 */       localCalendar.set(11, 0);
/*   64 */       localDate1 = localCalendar.getTime();
/*      */     }
/*      */
/*   67 */     String str1 = "SELECT SUM(dramount) debit,SUM(cramount) credit, tdate, MAX(priority) priority FROM( SELECT d.transaction_type ttype, decode(transaction_type,1,d.schedule_amount) dramount,decode(transaction_type,-1,d.schedule_amount) cramount, to_char(t.schedule_date,'" + paramString2 + "') tdate,t.priority priority FROM transaction_detail@" + "OMS_LINK" + " d, transaction@" + "OMS_LINK" + " t " + " ,FIXED_ACCOUNTS@" + "OMS_LINK" + " f WHERE t.id = d.transaction_id  and f.id=d.FIXED_ACC and d.schedule_amount<>0 AND  t.schedule_date BETWEEN ? AND ? " + "AND f.ACC_ID in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + "))  " + paramString3 + " )GROUP BY tdate";
/*      */
/*   73 */     PreparedStatement localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
/*   74 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*   75 */     localPreparedStatement.setTimestamp(2, localTimestamp2);
/*      */
/*   77 */     ResultSet localResultSet = localPreparedStatement.executeQuery();
/*      */     String str2;
/*      */     double d1;
/*      */     double d2;
/*   81 */     while (localResultSet.next()) {
/*   82 */       str2 = localResultSet.getString("tdate");
/*   83 */       d1 = localResultSet.getDouble("debit");
/*   84 */       d2 = localResultSet.getDouble("credit");
/*   85 */       i = localResultSet.getInt("priority");
/*   86 */       localFinanceDayModel = (FinanceDayModel)localHashtable.get(str2);
/*   87 */       localFinanceDayModel.setPtDebit(d1);
/*   88 */       localFinanceDayModel.setPtCredit(d2);
/*   89 */       localFinanceDayModel.setPriority(i);
/*      */     }
/*   91 */     localPreparedStatement.close();
/*   92 */     str1 = "SELECT SUM(dramount) debit,SUM(cramount) credit, tdate FROM(  SELECT d.transaction_type ttype, decode(transaction_type,1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) dramount,decode(transaction_type,-1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) cramount, to_char(t.transaction_date,'" + paramString2 + "') tdate FROM transaction_detail@" + "OMS_LINK" + " d, transaction@" + "OMS_LINK" + " t " + " ,FIXED_ACCOUNTS@" + "OMS_LINK" + " f WHERE t.id = d.transaction_id and f.id=d.FIXED_ACC AND d.amount<>0 AND  t.transaction_date BETWEEN ? AND ? " + " AND f.ACC_ID in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString3 + " )GROUP BY tdate";
/*      */
/*   98 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
/*   99 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*  100 */     localPreparedStatement.setTimestamp(2, localTimestamp2);
/*      */
/*  102 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/*  104 */     while (localResultSet.next()) {
/*  105 */       str2 = localResultSet.getString("tdate");
/*  106 */       d1 = localResultSet.getDouble("debit");
/*  107 */       d2 = localResultSet.getDouble("credit");
/*  108 */       localFinanceDayModel = (FinanceDayModel)localHashtable.get(str2);
/*  109 */       localFinanceDayModel.setAtDebit(d1);
/*  110 */       localFinanceDayModel.setAtCredit(d2);
/*      */     }
/*  112 */     localPreparedStatement.close();
/*  113 */     str1 = "SELECT SUM(d.transaction_type*d.amount) amount, SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) quantity  FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t  ,FIXED_ACCOUNTS@OMS_LINK f WHERE t.id = d.transaction_id and f.id=d.FIXED_ACC AND d.amount <> 0 AND  t.transaction_date < ?  AND f.ACC_ID in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString3;
/*      */
/*  116 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
/*      */
/*  118 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*      */
/*  120 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/*  122 */     if (localResultSet.next()) {
/*  123 */       localFinanceModel.setInitACB(localResultSet.getDouble("quantity"));
/*      */     }
/*  125 */     localPreparedStatement.close();
/*      */
/*  127 */     str1 = "SELECT SUM(d.transaction_type*d.schedule_amount) amount, SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) quantity FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, fixed_accounts@OMS_LINK f WHERE t.id = d.transaction_id and f.id=d.FIXED_ACC AND d.amount=0 and d.schedule_amount<>0 AND  t.schedule_date < ?  AND f.ACC_ID in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + "))" + paramString3;
/*      */
/*  131 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
/*  132 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*      */
/*  134 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/*  136 */     if (localResultSet.next())
/*  137 */       localFinanceModel.setInitPCB(localResultSet.getDouble("amount"));
/*  138 */     localPreparedStatement.close();
/*      */
/*  140 */     Date localDate2 = new Date();
/*      */
/*  142 */     localCalendar.setTime(paramDate1);
/*  143 */     localDate1 = localCalendar.getTime();
/*  144 */     while (localDate1.before(paramDate2)) {
/*  145 */       localFinanceDayModel = (FinanceDayModel)localHashtable.get(localSimpleDateFormat.format(localDate1));
/*  146 */       if (localDate1.after(localDate2))
/*  147 */         localFinanceModel.setInitACB(localFinanceModel.getInitACB() + localFinanceDayModel.getPtDebit() - localFinanceDayModel.getPtCredit());
/*      */       else
/*  149 */         localFinanceModel.setInitACB(localFinanceModel.getInitACB() + localFinanceDayModel.getAtDebit() - localFinanceDayModel.getAtCredit());
/*  150 */       localFinanceDayModel.setActualBalance(localFinanceModel.getInitACB());
/*  151 */       localFinanceModel.setInitPCB(localFinanceModel.getInitACB() + localFinanceDayModel.getPtDebit() - localFinanceDayModel.getPtCredit());
/*  152 */       localFinanceDayModel.setPtojectedBalance(localFinanceModel.getInitPCB());
/*      */
/*  154 */       localCalendar.add(5, 1);
/*  155 */       localCalendar.set(12, 0);
/*  156 */       localCalendar.set(13, 0);
/*  157 */       localCalendar.set(11, 0);
/*  158 */       localDate1 = localCalendar.getTime();
/*      */     }
/*  160 */     localFinanceModel.setDays(localHashtable);
/*      */
/*  162 */     str1 = "SELECT SUM(d.transaction_type*d.amount) amount,SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) quantity,MAX(t.transaction_date) lt FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t  ,FIXED_ACCOUNTS@OMS_LINK f WHERE t.id = d.transaction_id  and f.id=d.FIXED_ACC AND d.amount <> 0 AND f.ACC_ID in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString3;
/*      */
/*  165 */     Date localDate3 = new Date();
/*  166 */     localResultSet = this.stmt.executeQuery(str1);
/*  167 */     if (localResultSet.next())
/*  168 */       localFinanceModel.setBalance(localResultSet.getDouble("quantity"));
/*  169 */     localFinanceModel.setCblLocal(localResultSet.getDouble("amount"));
/*  170 */     if (localResultSet.getTimestamp("lt") != null) {
/*  171 */       localDate3.setTime(localResultSet.getTimestamp("lt").getTime());
/*  172 */       localFinanceModel.setLastTransaction(localDate3);
/*      */     }
/*  174 */     return localFinanceModel;
/*      */   }
/*      */
/*      */   public Hashtable dayReportsWithUser(Date paramDate1, Date paramDate2, String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/*  180 */     FinanceModel localFinanceModel1 = new FinanceModel();
/*  181 */     Hashtable localHashtable1 = new Hashtable();
/*      */
/*  183 */     Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
/*  184 */     Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
/*  185 */     int i = 0;
/*      */
/*  187 */     Calendar localCalendar = Calendar.getInstance();
/*  188 */     localCalendar.setTime(paramDate1);
/*  189 */     SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
/*  190 */     Date localDate1 = localCalendar.getTime();
/*  191 */     FinanceDayModel localFinanceDayModel = null;
/*  192 */     while (localDate1.before(paramDate2)) {
/*  193 */       localFinanceDayModel = new FinanceDayModel();
/*  194 */       localHashtable1.put(localSimpleDateFormat.format(localDate1), localFinanceDayModel);
/*  195 */       localCalendar.add(5, 1);
/*  196 */       localCalendar.set(12, 0);
/*  197 */       localCalendar.set(13, 0);
/*  198 */       localCalendar.set(11, 0);
/*  199 */       localDate1 = localCalendar.getTime();
/*      */     }
/*      */
/*  202 */     Hashtable localHashtable2 = new Hashtable();
/*  203 */     double d1 = 0.0D;
/*  204 */     double d2 = 0.0D;
/*  205 */     FinanceModel localFinanceModel2 = null;
/*  206 */     String str1 = null;
/*  207 */     PreparedStatement localPreparedStatement = null;
/*  208 */     ResultSet localResultSet = null;
/*  209 */     String str2 = null;
/*  210 */     String str3 = null;
/*      */
/*  212 */     str1 = "SELECT u.id AS userid, sum(d.transaction_type*d.quantity) AS balance,u.first_name AS firstname, u.last_name AS lastname,i.name AS currency, i.purchase_price AS rate,(SELECT a.name FROM operational_hierarchy@OMS_LINK c, area_group@OMS_LINK a WHERE c.area_id = a.id AND c.user_id =u.id) area_name, MAX(t.transaction_date) lt FROM transaction_detail@OMS_LINK d, oms_users@OMS_LINK u, item@OMS_LINK i, fixed_accounts@OMS_LINK f  , transaction@OMS_LINK t  WHERE t.id = d.transaction_id AND f.id = d.fixed_acc AND f.user_id = u.id and d.amount>0 AND u.currency_id = i.id AND f.acc_id IN( SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id IN(" + paramString1 + ")" + ")  " + paramString2 + " GROUP BY u.id,i.name,i.purchase_price,u.first_name, u.last_name";
/*      */
/*  219 */     System.out.println("SQL-5>" + str1);
/*  220 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
/*  221 */     localResultSet = localPreparedStatement.executeQuery();
/*  222 */     Date localDate2 = new Date();
/*  223 */     while (localResultSet.next()) {
/*  224 */       str2 = localResultSet.getString("userid");
/*  225 */       str3 = localResultSet.getString("firstname");
/*  226 */       if ((str2 != null) && (str2.length() > 0) && (!str2.equals("null"))) {
/*  227 */         if (!localHashtable2.containsKey(str2)) {
/*  228 */           localHashtable2 = initUser(str2, str3, localTimestamp1, localTimestamp2, localHashtable2);
/*      */         }
/*  230 */         if ((localHashtable2 == null) || (localHashtable2.size() <= 0) ||
/*  231 */           (!localHashtable2.containsKey(str2))) continue;
/*  232 */         localFinanceModel2 = (FinanceModel)localHashtable2.get(str2);
/*      */
/*  234 */         if ((localResultSet.getString("lastname") != null) && (localResultSet.getString("lastname").length() > 0) && (!localResultSet.getString("lastname").equals("null")))
/*  235 */           str3 = str3 + " " + localResultSet.getString("lastname");
/*  236 */         localFinanceModel2.setUserName(str3);
/*      */
/*  238 */         localFinanceModel2.setAreaName(localResultSet.getString("area_name"));
/*      */
/*  240 */         localFinanceModel2.setRate(localResultSet.getDouble("rate"));
/*  241 */         localFinanceModel2.setCurrencyName(localResultSet.getString("currency"));
/*  242 */         localFinanceModel2.setCblLocal(localResultSet.getDouble("balance"));
/*  243 */         if (localResultSet.getTimestamp("lt") != null) {
/*  244 */           localDate2.setTime(localResultSet.getTimestamp("lt").getTime());
/*  245 */           localFinanceModel2.setLastTransaction(localDate2);
/*      */         }
/*      */       }
/*      */
/*      */     }
/*      */
/*  251 */     localPreparedStatement.close();
/*      */
/*  253 */     str1 = "SELECT SUM(dramount) debit,SUM(cramount) credit, tdate,max(priority) priority,user_id,user_name,area_name FROM( SELECT d.transaction_type ttype,decode(d.transaction_type,1,d.schedule_amount) dramount,decode(d.transaction_type,-1,d.schedule_amount) cramount, d.schedule_amount amount, to_char(t.schedule_date,'ddmmrr') tdate,f.user_id user_id,t.priority priority,(SELECT ou.first_name||' '||ou.last_name from oms_users@OMS_LINK ou where ou.id=f.user_id) user_name, (SELECT a.name FROM operational_hierarchy@OMS_LINK c, area_group@OMS_LINK a WHERE  c.area_id = a.id AND c.user_id =f.user_id) area_name FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, fixed_accounts@OMS_LINK f WHERE t.id = d.transaction_id AND f.id = d.fixed_acc  and d.schedule_amount<>0 AND  t.schedule_date BETWEEN ? AND ?  AND f.acc_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in (" + paramString1 + ")) " + paramString2 + " ) GROUP BY tdate,user_id ";
/*      */
/*  262 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
/*  263 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*  264 */     localPreparedStatement.setTimestamp(2, localTimestamp2);
/*      */
/*  266 */     localResultSet = localPreparedStatement.executeQuery();
/*      */     String str4;
/*      */     double d3;
/*      */     double d4;
/*  271 */     while (localResultSet.next()) {
/*  272 */       str4 = localResultSet.getString("tdate");
/*      */
/*  274 */       i = localResultSet.getInt("priority");
/*  275 */       str2 = localResultSet.getString("user_id");
/*  276 */       str3 = localResultSet.getString("user_name");
/*      */
/*  278 */       d3 = localResultSet.getDouble("debit");
/*  279 */       d4 = localResultSet.getDouble("credit");
/*      */
/*  281 */       if (!localHashtable2.containsKey(str2))
/*  282 */         localHashtable2 = initUser(str2, str3, localTimestamp1, localTimestamp2, localHashtable2);
/*  283 */       if ((localHashtable2 != null) && (localHashtable2.size() > 0)) {
/*  284 */         localFinanceModel2 = (FinanceModel)localHashtable2.get(str2);
/*  285 */         localFinanceModel2.setArea(localResultSet.getString("area_name"));
/*  286 */         localFinanceModel2.setUserName(str3);
/*  287 */         localHashtable1 = localFinanceModel2.getDays();
/*  288 */         if ((localHashtable1 != null) && (localHashtable1.size() > 0)) {
/*  289 */           localFinanceDayModel = (FinanceDayModel)localHashtable1.get(str4);
/*  290 */           localFinanceDayModel.setPtDebit(d3);
/*  291 */           localFinanceDayModel.setPtCredit(d4);
/*  292 */           localFinanceDayModel.setPriority(i);
/*      */         }
/*      */       }
/*      */     }
/*  296 */     localPreparedStatement.close();
/*      */
/*  299 */     str1 = "SELECT SUM(dramount) debit,SUM(cramount) credit, tdate,user_id,user_name FROM( SELECT d.transaction_type ttype,decode(d.transaction_type,1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) dramount, decode(d.transaction_type,-1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) cramount, d.amount amount, to_char(t.transaction_date,'ddmmrr') tdate,f.user_id user_id,(select ou.first_name||' '||ou.last_name from oms_users@OMS_LINK ou where ou.id=f.user_id) user_name, (SELECT a.name FROM operational_hierarchy@OMS_LINK c, area_group@OMS_LINK a WHERE  c.area_id = a.id AND c.user_id =f.user_id) area_name FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, fixed_accounts@OMS_LINK f  WHERE f.id = d.fixed_acc AND t.id = d.transaction_id AND d.amount<>0 AND  t.transaction_date BETWEEN ? AND ? AND f.acc_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in(" + paramString1 + ")) " + paramString2 + " )GROUP BY tdate,user_id ";
/*      */
/*  308 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
/*  309 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*  310 */     localPreparedStatement.setTimestamp(2, localTimestamp2);
/*      */
/*  312 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/*  314 */     while (localResultSet.next()) {
/*  315 */       str4 = localResultSet.getString("tdate");
/*      */
/*  317 */       str2 = localResultSet.getString("user_id");
/*  318 */       str3 = localResultSet.getString("user_name");
/*  319 */       d3 = localResultSet.getDouble("debit");
/*  320 */       d4 = localResultSet.getDouble("credit");
/*      */
/*  322 */       if (!localHashtable2.containsKey(str2)) {
/*  323 */         localHashtable2 = initUser(str2, str3, localTimestamp1, localTimestamp2, localHashtable2);
/*  324 */         localFinanceModel2 = (FinanceModel)localHashtable2.get(str2);
/*  325 */         localFinanceModel2.setArea(localResultSet.getString("area_name"));
/*      */       }
/*      */       else {
/*  328 */         localFinanceModel2 = (FinanceModel)localHashtable2.get(str2);
/*  329 */       }localHashtable1 = localFinanceModel2.getDays();
/*  330 */       localFinanceDayModel = (FinanceDayModel)localHashtable1.get(str4);
/*  331 */       localFinanceDayModel.setAtDebit(d3);
/*  332 */       localFinanceDayModel.setAtCredit(d4);
/*      */     }
/*  334 */     localPreparedStatement.close();
/*      */
/*  336 */     Enumeration localEnumeration1 = null;
/*  337 */     str1 = "SELECT SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) amount,SUM(d.transaction_type*d.quantity) quantity,f.user_id user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t , fixed_accounts@OMS_LINK f  WHERE f.id = d.fixed_acc AND t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date < ?  AND f.acc_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in(" + paramString1 + ")) " + paramString2 + " group by f.user_id";
/*      */
/*  341 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
/*  342 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*  343 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/*  345 */     while (localResultSet.next()) {
/*  346 */       str2 = localResultSet.getString("user_id");
/*  347 */       d2 = localResultSet.getDouble("amount");
/*      */
/*  349 */       if ((str2 == null) || (str2.length() <= 0) || (str2.equals("null")) ||
/*  350 */         (localHashtable2 == null) || (localHashtable2.size() <= 0) ||
/*  351 */         (!localHashtable2.containsKey(str2))) continue;
/*  352 */       localFinanceModel2 = (FinanceModel)localHashtable2.get(str2);
/*  353 */       localFinanceModel2.setInitACB(d2);
/*      */     }
/*      */
/*  359 */     localPreparedStatement.close();
/*      */
/*  361 */     str1 = "SELECT SUM(d.transaction_type*d.schedule_amount) amount,f.user_id user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, fixed_accounts@OMS_LINK f  WHERE f.id = d.fixed_acc AND t.id = d.transaction_id AND d.amount=0 and d.schedule_amount<>0 AND  t.transaction_date < ?  AND f.acc_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in (" + paramString1 + ")) " + paramString2 + " group by f.user_id";
/*      */
/*  365 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
/*  366 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*  367 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/*  369 */     while (localResultSet.next()) {
/*  370 */       str2 = localResultSet.getString("user_id");
/*  371 */       d1 = localResultSet.getDouble("amount");
/*  372 */       if ((str2 == null) || (str2.length() <= 0) || (str2.equals("null")) ||
/*  373 */         (localHashtable2 == null) || (localHashtable2.size() <= 0) ||
/*  374 */         (!localHashtable2.containsKey(str2))) continue;
/*  375 */       localFinanceModel2 = (FinanceModel)localHashtable2.get(str2);
/*  376 */       localFinanceModel2.setInitPCB(d1);
/*      */     }
/*      */
/*  381 */     localPreparedStatement.close();
/*      */
/*  385 */     localCalendar = Calendar.getInstance();
/*  386 */     localDate1 = localCalendar.getTime();
/*  387 */     Date localDate3 = new Date();
/*      */
/*  389 */     localCalendar.setTime(paramDate1);
/*      */
/*  392 */     double d5 = 0.0D;
/*  393 */     double d6 = 0.0D;
/*      */
/*  395 */     Enumeration localEnumeration2 = localHashtable2.keys();
/*  396 */     while (localEnumeration2.hasMoreElements()) {
/*  397 */       localCalendar.setTime(paramDate1);
/*  398 */       localDate1 = localCalendar.getTime();
/*  399 */       str2 = (String)localEnumeration2.nextElement();
/*  400 */       localFinanceModel2 = (FinanceModel)localHashtable2.get(str2);
/*  401 */       d5 = localFinanceModel2.getInitPCB();
/*  402 */       d6 = localFinanceModel2.getInitACB();
/*  403 */       localHashtable1 = localFinanceModel2.getDays();
/*  404 */       localEnumeration1 = localHashtable1.keys();
/*  405 */       while (localDate1.before(paramDate2))
/*      */       {
/*  407 */         localFinanceDayModel = (FinanceDayModel)localHashtable1.get(localSimpleDateFormat.format(localDate1));
/*  408 */         if (localDate1.after(localDate3))
/*  409 */           d6 = d6 + localFinanceDayModel.getPtDebit() - localFinanceDayModel.getPtCredit();
/*      */         else
/*  411 */           d6 = d6 + localFinanceDayModel.getPtDebit() - localFinanceDayModel.getPtCredit();
/*  412 */         localFinanceDayModel.setActualBalance(d6);
/*  413 */         d5 = d5 + localFinanceDayModel.getPtDebit() - localFinanceDayModel.getPtCredit();
/*  414 */         localFinanceDayModel.setPtojectedBalance(d5);
/*      */
/*  416 */         localCalendar.add(5, 1);
/*  417 */         localCalendar.set(12, 0);
/*  418 */         localCalendar.set(13, 0);
/*  419 */         localCalendar.set(11, 0);
/*  420 */         localDate1 = localCalendar.getTime();
/*      */       }
/*      */     }
/*      */
/*  424 */     return localHashtable2;
/*      */   }
/*      */
/*      */   private Hashtable initUser(String paramString1, String paramString2, Date paramDate1, Date paramDate2, Hashtable paramHashtable) throws SQLException
/*      */   {
/*  429 */     FinanceModel localFinanceModel = new FinanceModel();
/*  430 */     localFinanceModel.setUserId(paramString1);
/*  431 */     Calendar localCalendar = Calendar.getInstance();
/*  432 */     localCalendar.setTime(paramDate1);
/*  433 */     Hashtable localHashtable = new Hashtable();
/*  434 */     SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
/*  435 */     Date localDate = localCalendar.getTime();
/*  436 */     FinanceDayModel localFinanceDayModel1 = null;
/*  437 */     FinanceDayModel localFinanceDayModel2 = null;
/*  438 */     while (localDate.before(paramDate2)) {
/*  439 */       localFinanceDayModel1 = new FinanceDayModel();
/*  440 */       localHashtable.put(localSimpleDateFormat.format(localDate), localFinanceDayModel1);
/*  441 */       localCalendar.add(5, 1);
/*  442 */       localCalendar.set(12, 0);
/*  443 */       localCalendar.set(13, 0);
/*  444 */       localCalendar.set(11, 0);
/*  445 */       localDate = localCalendar.getTime();
/*      */     }
/*  447 */     localFinanceDayModel2 = new FinanceDayModel();
/*  448 */     localHashtable.put(localSimpleDateFormat.format(paramDate1) + " to " + localSimpleDateFormat.format(paramDate2), localFinanceDayModel2);
/*  449 */     localFinanceModel.setDays(localHashtable);
/*  450 */     paramHashtable.put(paramString1, localFinanceModel);
/*      */
/*  452 */     return paramHashtable;
/*      */   }
/*      */
/*      */   public Hashtable getShortage(String paramString) throws SQLException {
/*  456 */     Hashtable localHashtable = new Hashtable();
/*  457 */     String str = "SELECT SUM(transaction_type*quantity*(SELECT purchase_price FROM item@OMS_LINK WHERE id = f.currency_id)) amnt FROM transaction_detail@OMS_LINK d, fixed_accounts@OMS_LINK f WHERE d.fixed_acc = f.id AND f.acc_id = 16";
/*      */
/*  461 */     ResultSet localResultSet = this.stmt.executeQuery(str);
/*  462 */     if (localResultSet.next()) {
/*  463 */       localHashtable.put("BALANCE", new Double(localResultSet.getDouble("amnt")));
/*      */     }
/*  465 */     str = "SELECT fa.acc_id,SUM(td.transaction_type*td.quantity*(SELECT purchase_price FROM item@OMS_LINK  WHERE id = fa.currency_id)) amount,(SELECT name FROM account_group@OMS_LINK  WHERE id=fa.acc_id) acname FROM transaction_detail@OMS_LINK  td, fixed_accounts@OMS_LINK  fa, transaction@OMS_LINK tr WHERE td.fixed_acc = fa.id AND td.transaction_id = tr.id AND fa.acc_id <>16 AND td.transaction_id IN( SELECT t.id FROM transaction_detail@OMS_LINK  d, fixed_accounts@OMS_LINK  f, transaction@OMS_LINK  t WHERE f.id = d.fixed_acc AND d.transaction_id = t.id AND f.acc_id = 16 AND schedule_date BETWEEN sysdate AND to_date('" + paramString + "','ddmmrr') " + ")GROUP BY fa.acc_id";
/*      */
/*  474 */     while (localResultSet.next())
/*      */     {
/*  476 */       localHashtable.put(localResultSet.getString("acname"), new Double(localResultSet.getDouble("amount")));
/*      */     }
/*  478 */     return localHashtable;
/*      */   }
/*      */
/*      */   public FinanceModel getGroups(Date paramDate1, Date paramDate2, String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/*  492 */     FinanceModel localFinanceModel = new FinanceModel();
/*  493 */     Hashtable localHashtable = new Hashtable();
/*      */
/*  495 */     Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
/*  496 */     Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
/*  497 */     int i = 0;
/*  498 */     Calendar localCalendar = Calendar.getInstance();
/*  499 */     localCalendar.setTime(paramDate1);
/*  500 */     SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
/*  501 */     String str1 = localSimpleDateFormat.format(paramDate1) + " to " + localSimpleDateFormat.format(localTimestamp2);
/*  502 */     FinanceDayModel localFinanceDayModel1 = new FinanceDayModel();
/*  503 */     Date localDate1 = localCalendar.getTime();
/*  504 */     FinanceDayModel localFinanceDayModel2 = null;
/*  505 */     while (localDate1.before(paramDate2)) {
/*  506 */       localFinanceDayModel2 = new FinanceDayModel();
/*  507 */       localHashtable.put(localSimpleDateFormat.format(localDate1), localFinanceDayModel2);
/*  508 */       localCalendar.add(5, 1);
/*  509 */       localCalendar.set(12, 0);
/*  510 */       localCalendar.set(13, 0);
/*  511 */       localCalendar.set(11, 0);
/*  512 */       localDate1 = localCalendar.getTime();
/*      */     }
/*      */
/*  515 */     String str2 = "SELECT SUM(dramount) debit,SUM(cramount) credit,SUM(dramount1) debit1,SUM(cramount1) credit1, tdate, MAX(priority) priority FROM( SELECT d.transaction_type ttype, decode(transaction_type,1,d.schedule_amount) dramount,decode(transaction_type,-1,d.schedule_amount) cramount, decode(transaction_type,1,decode(t.is_carry_schedule,NULL,d.schedule_amount)) dramount1,decode(transaction_type,-1,decode(t.is_carry_schedule,NULL,d.schedule_amount)) cramount1, to_char(t.schedule_date,'" + paramString2 + "') tdate,t.priority priority FROM transaction_detail@" + "OMS_LINK" + " d, transaction@" + "OMS_LINK" + " t " + " ,FIXED_ACCOUNTS@" + "OMS_LINK" + " f WHERE t.id = d.transaction_id  and f.id=d.FIXED_ACC and d.schedule_amount<>0 AND  t.schedule_date BETWEEN ? AND ? " + "AND f.ACC_ID in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + "))  " + ")GROUP BY tdate";
/*      */
/*  522 */     PreparedStatement localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
/*  523 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*  524 */     localPreparedStatement.setTimestamp(2, localTimestamp2);
/*      */
/*  526 */     ResultSet localResultSet = localPreparedStatement.executeQuery();
/*      */     String str3;
/*      */     double d1;
/*      */     double d2;
/*  530 */     while (localResultSet.next()) {
/*  531 */       str3 = localResultSet.getString("tdate");
/*  532 */       d1 = localResultSet.getDouble("debit");
/*  533 */       d2 = localResultSet.getDouble("credit");
/*  534 */       i = localResultSet.getInt("priority");
/*  535 */       localFinanceDayModel2 = (FinanceDayModel)localHashtable.get(str3);
/*  536 */       localFinanceDayModel2.setPtDebit(d1);
/*  537 */       localFinanceDayModel2.setPtCredit(d2);
/*  538 */       localFinanceDayModel2.setPriority(i);
/*      */
/*  540 */       if (str3.equals(localSimpleDateFormat.format(paramDate1))) {
/*  541 */         localFinanceDayModel1.setPtDebit(localFinanceDayModel1.getPtDebit() + localFinanceDayModel2.getPtDebit());
/*  542 */         localFinanceDayModel1.setPtCredit(localFinanceDayModel1.getPtCredit() + localFinanceDayModel2.getPtCredit()); continue;
/*      */       }
/*  544 */       localFinanceDayModel1.setPtDebit(localFinanceDayModel1.getPtDebit() + localResultSet.getDouble("debit1"));
/*  545 */       localFinanceDayModel1.setPtCredit(localFinanceDayModel1.getPtCredit() + localResultSet.getDouble("credit1"));
/*      */     }
/*      */
/*  548 */     localPreparedStatement.close();
/*  549 */     str2 = "SELECT SUM(dramount) debit,SUM(cramount) credit, tdate FROM(  SELECT d.transaction_type ttype, decode(transaction_type,1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) dramount,decode(transaction_type,-1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) cramount, to_char(t.transaction_date,'" + paramString2 + "') tdate FROM transaction_detail@" + "OMS_LINK" + " d, transaction@" + "OMS_LINK" + " t " + " ,FIXED_ACCOUNTS@" + "OMS_LINK" + " f WHERE t.id = d.transaction_id and f.id=d.FIXED_ACC AND d.amount<>0 AND  t.transaction_date BETWEEN ? AND ? " + " AND f.ACC_ID in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + " )GROUP BY tdate";
/*      */
/*  555 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
/*  556 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*  557 */     localPreparedStatement.setTimestamp(2, localTimestamp2);
/*      */
/*  559 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/*  561 */     while (localResultSet.next()) {
/*  562 */       str3 = localResultSet.getString("tdate");
/*  563 */       d1 = localResultSet.getDouble("debit");
/*  564 */       d2 = localResultSet.getDouble("credit");
/*  565 */       localFinanceDayModel2 = (FinanceDayModel)localHashtable.get(str3);
/*  566 */       localFinanceDayModel2.setAtDebit(d1);
/*  567 */       localFinanceDayModel2.setAtCredit(d2);
/*  568 */       localFinanceDayModel1.setAtDebit(localFinanceDayModel1.getAtDebit() + localFinanceDayModel2.getAtDebit());
/*  569 */       localFinanceDayModel1.setAtCredit(localFinanceDayModel1.getAtCredit() + localFinanceDayModel2.getAtCredit());
/*      */     }
/*  571 */     localPreparedStatement.close();
/*  572 */     str2 = "SELECT SUM(d.transaction_type*d.amount) amount, SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) quantity  FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t  ,FIXED_ACCOUNTS@OMS_LINK f WHERE t.id = d.transaction_id and f.id=d.FIXED_ACC AND d.amount <> 0 AND  t.transaction_date < ?  AND f.ACC_ID in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) ";
/*      */
/*  575 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
/*      */
/*  577 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*      */
/*  579 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/*  581 */     if (localResultSet.next()) {
/*  582 */       localFinanceModel.setInitACB(localResultSet.getDouble("quantity"));
/*      */     }
/*  584 */     localPreparedStatement.close();
/*      */
/*  586 */     str2 = "SELECT SUM(d.transaction_type*d.schedule_amount) amount, SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) quantity FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, fixed_accounts@OMS_LINK f WHERE t.id = d.transaction_id and f.id=d.FIXED_ACC AND d.amount=0 and d.schedule_amount<>0 AND  t.schedule_date < ?  AND f.ACC_ID in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + "))";
/*      */
/*  590 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
/*  591 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*      */
/*  593 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/*  595 */     if (localResultSet.next())
/*  596 */       localFinanceModel.setInitPCB(localResultSet.getDouble("amount"));
/*  597 */     localPreparedStatement.close();
/*      */
/*  599 */     Date localDate2 = new Date();
/*      */
/*  601 */     localCalendar.setTime(paramDate1);
/*  602 */     localDate1 = localCalendar.getTime();
/*      */
/*  604 */     while (localDate1.before(paramDate2)) {
/*  605 */       localFinanceDayModel2 = (FinanceDayModel)localHashtable.get(localSimpleDateFormat.format(localDate1));
/*  606 */       if (localDate1.after(localDate2)) {
/*  607 */         localFinanceModel.setInitACB(localFinanceModel.getInitACB() + localFinanceDayModel2.getPtDebit() - localFinanceDayModel2.getPtCredit());
/*      */       }
/*      */       else {
/*  610 */         localFinanceModel.setInitACB(localFinanceModel.getInitACB() + localFinanceDayModel2.getAtDebit() - localFinanceDayModel2.getAtCredit());
/*      */       }
/*  612 */       localFinanceDayModel2.setActualBalance(localFinanceModel.getInitACB());
/*  613 */       localFinanceModel.setInitPCB(localFinanceModel.getInitACB() + localFinanceDayModel2.getPtDebit() - localFinanceDayModel2.getPtCredit());
/*  614 */       localFinanceDayModel2.setPtojectedBalance(localFinanceModel.getInitPCB());
/*  615 */       localCalendar.add(5, 1);
/*  616 */       localCalendar.set(12, 0);
/*  617 */       localCalendar.set(13, 0);
/*  618 */       localCalendar.set(11, 0);
/*  619 */       localDate1 = localCalendar.getTime();
/*      */     }
/*  621 */     localHashtable.put(str1, localFinanceDayModel1);
/*  622 */     localFinanceModel.setDays(localHashtable);
/*      */
/*  624 */     str2 = "SELECT SUM(d.transaction_type*d.amount) amount,SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) quantity,MAX(t.transaction_date) lt FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t  ,FIXED_ACCOUNTS@OMS_LINK f WHERE t.id = d.transaction_id  and f.id=d.FIXED_ACC AND d.amount <> 0 AND f.ACC_ID in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) ";
/*      */
/*  627 */     Date localDate3 = new Date();
/*  628 */     localResultSet = this.stmt.executeQuery(str2);
/*  629 */     if (localResultSet.next())
/*  630 */       localFinanceModel.setBalance(localResultSet.getDouble("quantity"));
/*  631 */     localFinanceModel.setCblLocal(localResultSet.getDouble("amount"));
/*  632 */     if (localResultSet.getTimestamp("lt") != null) {
/*  633 */       localDate3.setTime(localResultSet.getTimestamp("lt").getTime());
/*  634 */       localFinanceModel.setLastTransaction(localDate3);
/*      */     }
/*  636 */     return localFinanceModel;
/*      */   }
/*      */
/*      */   public Hashtable dayReportsWithUser(Date paramDate1, Date paramDate2, String paramString) throws SQLException {
/*  640 */     FinanceModel localFinanceModel1 = new FinanceModel();
/*  641 */     Hashtable localHashtable1 = new Hashtable();
/*      */
/*  643 */     Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
/*  644 */     Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
/*  645 */     int i = 0;
/*      */
/*  647 */     Calendar localCalendar = Calendar.getInstance();
/*  648 */     localCalendar.setTime(paramDate1);
/*  649 */     SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
/*  650 */     String str1 = localSimpleDateFormat.format(paramDate1) + " to " + localSimpleDateFormat.format(localTimestamp2);
/*  651 */     FinanceDayModel localFinanceDayModel1 = null;
/*  652 */     Date localDate1 = localCalendar.getTime();
/*  653 */     FinanceDayModel localFinanceDayModel2 = null;
/*  654 */     while (localDate1.before(paramDate2)) {
/*  655 */       localFinanceDayModel2 = new FinanceDayModel();
/*  656 */       localHashtable1.put(localSimpleDateFormat.format(localDate1), localFinanceDayModel2);
/*  657 */       localCalendar.add(5, 1);
/*  658 */       localCalendar.set(12, 0);
/*  659 */       localCalendar.set(13, 0);
/*  660 */       localCalendar.set(11, 0);
/*  661 */       localDate1 = localCalendar.getTime();
/*      */     }
/*      */
/*  664 */     Hashtable localHashtable2 = new Hashtable();
/*  665 */     double d1 = 0.0D;
/*  666 */     double d2 = 0.0D;
/*  667 */     FinanceModel localFinanceModel2 = null;
/*  668 */     String str2 = null;
/*  669 */     PreparedStatement localPreparedStatement = null;
/*  670 */     ResultSet localResultSet = null;
/*  671 */     String str3 = null;
/*  672 */     String str4 = null;
/*      */
/*  674 */     str2 = "SELECT u.id AS userid, sum(d.transaction_type*d.quantity) AS balance,u.first_name AS firstname, u.last_name AS lastname,i.name AS currency, i.purchase_price AS rate,(SELECT a.name FROM operational_hierarchy@OMS_LINK c, area_group@OMS_LINK a WHERE c.area_id = a.id AND c.user_id =u.id) area_name, MAX(t.transaction_date) lt FROM transaction_detail@OMS_LINK d, oms_users@OMS_LINK u, item@OMS_LINK i, fixed_accounts@OMS_LINK f  , transaction@OMS_LINK t  WHERE t.id = d.transaction_id AND f.id = d.fixed_acc AND f.user_id = u.id AND u.status<>'Inactive' and d.amount>0 AND u.currency_id = i.id AND f.acc_id IN( SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id IN(" + paramString + ")" + ") GROUP BY u.id,i.name,i.purchase_price,u.first_name, u.last_name";
/*      */
/*  682 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
/*  683 */     localResultSet = localPreparedStatement.executeQuery();
/*  684 */     Date localDate2 = new Date();
/*  685 */     while (localResultSet.next()) {
/*  686 */       str3 = localResultSet.getString("userid");
/*  687 */       str4 = localResultSet.getString("firstname");
/*  688 */       if ((str3 != null) && (str3.length() > 0) && (!str3.equals("null"))) {
/*  689 */         if (!localHashtable2.containsKey(str3)) {
/*  690 */           localHashtable2 = initUser(str3, str4, localTimestamp1, localTimestamp2, localHashtable2);
/*      */         }
/*  692 */         if ((localHashtable2 == null) || (localHashtable2.size() <= 0) ||
/*  693 */           (!localHashtable2.containsKey(str3))) continue;
/*  694 */         localFinanceModel2 = (FinanceModel)localHashtable2.get(str3);
/*      */
/*  696 */         if ((localResultSet.getString("lastname") != null) && (localResultSet.getString("lastname").length() > 0) && (!localResultSet.getString("lastname").equals("null")))
/*  697 */           str4 = str4 + " " + localResultSet.getString("lastname");
/*  698 */         localFinanceModel2.setUserName(str4);
/*      */
/*  700 */         localFinanceModel2.setAreaName(localResultSet.getString("area_name"));
/*      */
/*  702 */         localFinanceModel2.setRate(localResultSet.getDouble("rate"));
/*  703 */         localFinanceModel2.setCurrencyName(localResultSet.getString("currency"));
/*  704 */         localFinanceModel2.setCblLocal(localResultSet.getDouble("balance"));
/*  705 */         if (localResultSet.getTimestamp("lt") != null) {
/*  706 */           localDate2.setTime(localResultSet.getTimestamp("lt").getTime());
/*  707 */           localFinanceModel2.setLastTransaction(localDate2);
/*      */         }
/*      */       }
/*      */
/*      */     }
/*      */
/*  713 */     localPreparedStatement.close();
/*      */
/*  715 */     str2 = "SELECT SUM(dramount) debit,SUM(cramount) credit,SUM(dramount) debit1,SUM(cramount) credit1, tdate,max(priority) priority,user_id,user_name,area_name FROM( SELECT d.transaction_type ttype,decode(d.transaction_type,1,d.schedule_amount) dramount,decode(d.transaction_type,-1,d.schedule_amount) cramount, decode(transaction_type,1,decode(t.is_carry_schedule,NULL,d.schedule_amount)) dramount1,decode(transaction_type,-1,decode(t.is_carry_schedule,NULL,d.schedule_amount)) cramount1, d.schedule_amount amount, to_char(t.schedule_date,'ddmmrr') tdate,f.user_id user_id,t.priority priority,(SELECT ou.first_name||' '||ou.last_name from oms_users@OMS_LINK ou where ou.id=f.user_id) user_name, (SELECT a.name FROM operational_hierarchy@OMS_LINK c, area_group@OMS_LINK a WHERE  c.area_id = a.id AND c.user_id =f.user_id) area_name FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, fixed_accounts@OMS_LINK f WHERE t.id = d.transaction_id AND f.id = d.fixed_acc AND d.user_id NOT IN(select id FROM oms_users@OMS_LINK WHERE status = 'Inactive')  and d.schedule_amount<>0 AND  t.schedule_date BETWEEN ? AND ?  AND f.acc_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in (" + paramString + "))) GROUP BY tdate,user_id ";
/*      */
/*  725 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
/*  726 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*  727 */     localPreparedStatement.setTimestamp(2, localTimestamp2);
/*      */
/*  729 */     localResultSet = localPreparedStatement.executeQuery();
/*      */     String str5;
/*      */     double d3;
/*      */     double d4;
/*  733 */     while (localResultSet.next()) {
/*  734 */       str5 = localResultSet.getString("tdate");
/*      */
/*  736 */       i = localResultSet.getInt("priority");
/*  737 */       str3 = localResultSet.getString("user_id");
/*  738 */       str4 = localResultSet.getString("user_name");
/*      */
/*  740 */       d3 = localResultSet.getDouble("debit");
/*  741 */       d4 = localResultSet.getDouble("credit");
/*      */
/*  743 */       if (!localHashtable2.containsKey(str3))
/*  744 */         localHashtable2 = initUser(str3, str4, localTimestamp1, localTimestamp2, localHashtable2);
/*  745 */       if ((localHashtable2 != null) && (localHashtable2.size() > 0)) {
/*  746 */         localFinanceModel2 = (FinanceModel)localHashtable2.get(str3);
/*  747 */         localFinanceModel2.setArea(localResultSet.getString("area_name"));
/*  748 */         localFinanceModel2.setUserName(str4);
/*  749 */         localHashtable1 = localFinanceModel2.getDays();
/*  750 */         if ((localHashtable1 != null) && (localHashtable1.size() > 0)) {
/*  751 */           localFinanceDayModel2 = (FinanceDayModel)localHashtable1.get(str5);
/*  752 */           localFinanceDayModel1 = (FinanceDayModel)localHashtable1.get(str1);
/*  753 */           localFinanceDayModel2.setPtDebit(d3);
/*  754 */           localFinanceDayModel2.setPtCredit(d4);
/*      */
/*  756 */           if (str5.equals(localSimpleDateFormat.format(paramDate1))) {
/*  757 */             localFinanceDayModel1.setPtDebit(localFinanceDayModel1.getPtDebit() + localFinanceDayModel2.getPtDebit());
/*  758 */             localFinanceDayModel1.setPtCredit(localFinanceDayModel1.getPtCredit() + localFinanceDayModel2.getPtCredit());
/*      */           } else {
/*  760 */             localFinanceDayModel1.setPtDebit(localFinanceDayModel1.getPtDebit() + localResultSet.getDouble("debit1"));
/*  761 */             localFinanceDayModel1.setPtCredit(localFinanceDayModel1.getPtCredit() + localResultSet.getDouble("credit1"));
/*      */           }
/*      */
/*  764 */           localFinanceDayModel2.setPriority(i);
/*      */         }
/*      */       }
/*      */     }
/*  768 */     localPreparedStatement.close();
/*      */
/*  771 */     str2 = "SELECT SUM(dramount) debit,SUM(cramount) credit, tdate,user_id,user_name FROM( SELECT d.transaction_type ttype,decode(d.transaction_type,1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) dramount, decode(d.transaction_type,-1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) cramount, d.amount amount, to_char(t.transaction_date,'ddmmrr') tdate,f.user_id user_id,(select ou.first_name||' '||ou.last_name from oms_users@OMS_LINK ou where ou.id=f.user_id) user_name, (SELECT a.name FROM operational_hierarchy@OMS_LINK c, area_group@OMS_LINK a WHERE  c.area_id = a.id AND c.user_id =f.user_id) area_name FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, fixed_accounts@OMS_LINK f  WHERE f.id = d.fixed_acc AND d.user_id NOT IN(select id FROM oms_users@OMS_LINK WHERE status = 'Inactive') AND t.id = d.transaction_id AND d.amount<>0 AND  t.transaction_date BETWEEN ? AND ? AND f.acc_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in(" + paramString + ")))GROUP BY tdate,user_id ";
/*      */
/*  780 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
/*  781 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*  782 */     localPreparedStatement.setTimestamp(2, localTimestamp2);
/*      */
/*  784 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/*  786 */     while (localResultSet.next()) {
/*  787 */       str5 = localResultSet.getString("tdate");
/*      */
/*  789 */       str3 = localResultSet.getString("user_id");
/*  790 */       str4 = localResultSet.getString("user_name");
/*  791 */       d3 = localResultSet.getDouble("debit");
/*  792 */       d4 = localResultSet.getDouble("credit");
/*      */
/*  794 */       if (!localHashtable2.containsKey(str3)) {
/*  795 */         localHashtable2 = initUser(str3, str4, localTimestamp1, localTimestamp2, localHashtable2);
/*  796 */         localFinanceModel2 = (FinanceModel)localHashtable2.get(str3);
/*  797 */         localFinanceModel2.setArea(localResultSet.getString("area_name"));
/*      */       }
/*      */       else {
/*  800 */         localFinanceModel2 = (FinanceModel)localHashtable2.get(str3);
/*  801 */       }localHashtable1 = localFinanceModel2.getDays();
/*  802 */       localFinanceDayModel2 = (FinanceDayModel)localHashtable1.get(str5);
/*  803 */       localFinanceDayModel1 = (FinanceDayModel)localHashtable1.get(str1);
/*  804 */       localFinanceDayModel2.setAtDebit(d3);
/*  805 */       localFinanceDayModel2.setAtCredit(d4);
/*  806 */       localFinanceDayModel1.setAtDebit(localFinanceDayModel1.getAtDebit() + localFinanceDayModel2.getAtDebit());
/*  807 */       localFinanceDayModel1.setAtCredit(localFinanceDayModel1.getAtCredit() + localFinanceDayModel2.getAtCredit());
/*      */     }
/*  809 */     localPreparedStatement.close();
/*      */
/*  811 */     Enumeration localEnumeration1 = null;
/*  812 */     str2 = "SELECT SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) amount,SUM(d.transaction_type*d.quantity) quantity,f.user_id user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t , fixed_accounts@OMS_LINK f  WHERE f.id = d.fixed_acc AND t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date < ?  AND f.acc_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in(" + paramString + ")) group by f.user_id";
/*      */
/*  816 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
/*  817 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*  818 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/*  820 */     while (localResultSet.next()) {
/*  821 */       str3 = localResultSet.getString("user_id");
/*  822 */       d2 = localResultSet.getDouble("amount");
/*      */
/*  824 */       if ((str3 == null) || (str3.length() <= 0) || (str3.equals("null")) ||
/*  825 */         (localHashtable2 == null) || (localHashtable2.size() <= 0) ||
/*  826 */         (!localHashtable2.containsKey(str3))) continue;
/*  827 */       localFinanceModel2 = (FinanceModel)localHashtable2.get(str3);
/*  828 */       localFinanceModel2.setInitACB(d2);
/*      */     }
/*      */
/*  834 */     localPreparedStatement.close();
/*      */
/*  836 */     str2 = "SELECT SUM(d.transaction_type*d.schedule_amount) amount,f.user_id user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, fixed_accounts@OMS_LINK f  WHERE f.id = d.fixed_acc AND t.id = d.transaction_id AND d.amount=0 and d.schedule_amount<>0 AND  t.transaction_date < ?  AND f.acc_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in (" + paramString + ")) group by f.user_id";
/*      */
/*  840 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
/*  841 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*  842 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/*  844 */     while (localResultSet.next()) {
/*  845 */       str3 = localResultSet.getString("user_id");
/*  846 */       d1 = localResultSet.getDouble("amount");
/*  847 */       if ((str3 == null) || (str3.length() <= 0) || (str3.equals("null")) ||
/*  848 */         (localHashtable2 == null) || (localHashtable2.size() <= 0) ||
/*  849 */         (!localHashtable2.containsKey(str3))) continue;
/*  850 */       localFinanceModel2 = (FinanceModel)localHashtable2.get(str3);
/*  851 */       localFinanceModel2.setInitPCB(d1);
/*      */     }
/*      */
/*  856 */     localPreparedStatement.close();
/*      */
/*  860 */     localCalendar = Calendar.getInstance();
/*  861 */     localDate1 = localCalendar.getTime();
/*  862 */     Date localDate3 = new Date();
/*      */
/*  864 */     localCalendar.setTime(paramDate1);
/*      */
/*  867 */     double d5 = 0.0D;
/*  868 */     double d6 = 0.0D;
/*      */
/*  870 */     Enumeration localEnumeration2 = localHashtable2.keys();
/*  871 */     while (localEnumeration2.hasMoreElements()) {
/*  872 */       localCalendar.setTime(paramDate1);
/*  873 */       localDate1 = localCalendar.getTime();
/*  874 */       str3 = (String)localEnumeration2.nextElement();
/*  875 */       localFinanceModel2 = (FinanceModel)localHashtable2.get(str3);
/*  876 */       d5 = localFinanceModel2.getInitPCB();
/*  877 */       d6 = localFinanceModel2.getInitACB();
/*  878 */       localHashtable1 = localFinanceModel2.getDays();
/*  879 */       localEnumeration1 = localHashtable1.keys();
/*  880 */       while (localDate1.before(paramDate2))
/*      */       {
/*  882 */         localFinanceDayModel2 = (FinanceDayModel)localHashtable1.get(localSimpleDateFormat.format(localDate1));
/*  883 */         if (str3.equals("259")) {
/*  884 */           System.out.println(localDate1 + "    SCB =   " + d6 + "   DR-  " + localFinanceDayModel2.getAtDebit() + "    CR-  " + localFinanceDayModel2.getPtCredit());
/*      */         }
/*      */
/*  887 */         if (localDate1.after(localDate3))
/*  888 */           d6 = d6 + localFinanceDayModel2.getPtDebit() - localFinanceDayModel2.getPtCredit();
/*      */         else {
/*  890 */           d6 = d6 + localFinanceDayModel2.getAtDebit() - localFinanceDayModel2.getAtCredit();
/*      */         }
/*  892 */         localFinanceDayModel2.setActualBalance(d6);
/*  893 */         d5 = d5 + localFinanceDayModel2.getPtDebit() - localFinanceDayModel2.getPtCredit();
/*  894 */         localFinanceDayModel2.setPtojectedBalance(d5);
/*      */
/*  896 */         localCalendar.add(5, 1);
/*  897 */         localCalendar.set(12, 0);
/*  898 */         localCalendar.set(13, 0);
/*  899 */         localCalendar.set(11, 0);
/*  900 */         localDate1 = localCalendar.getTime();
/*      */       }
/*      */     }
/*  903 */     return localHashtable2;
/*      */   }
/*      */
/*      */   public FinanceModel getGroups1(Date paramDate1, Date paramDate2, String paramString1, String paramString2, String paramString3)
/*      */     throws SQLException
/*      */   {
/*  915 */     FinanceModel localFinanceModel = new FinanceModel();
/*  916 */     Hashtable localHashtable = new Hashtable();
/*      */
/*  918 */     Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
/*  919 */     Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
/*  920 */     int i = 0;
/*      */
/*  922 */     Calendar localCalendar = Calendar.getInstance();
/*  923 */     localCalendar.setTime(paramDate1);
/*  924 */     SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
/*  925 */     Date localDate1 = localCalendar.getTime();
/*  926 */     FinanceDayModel localFinanceDayModel = null;
/*  927 */     while (localDate1.before(paramDate2)) {
/*  928 */       localFinanceDayModel = new FinanceDayModel();
/*  929 */       localHashtable.put(localSimpleDateFormat.format(localDate1), localFinanceDayModel);
/*  930 */       localCalendar.add(5, 1);
/*  931 */       localCalendar.set(12, 0);
/*  932 */       localCalendar.set(13, 0);
/*  933 */       localCalendar.set(11, 0);
/*  934 */       localDate1 = localCalendar.getTime();
/*      */     }
/*      */
/*  938 */     PreparedStatement localPreparedStatement = null;
/*  939 */     ResultSet localResultSet = null;
/*  940 */     String str1 = "";
/*      */
/*  942 */     String str2 = "SELECT SUM(dramount) debit,SUM(cramount) credit, tdate FROM(  SELECT d.transaction_type ttype, decode(transaction_type,1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) dramount,decode(transaction_type,-1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) cramount, to_char(t.transaction_date,'" + paramString2 + "') tdate FROM transaction_detail@" + "OMS_LINK" + " d, transaction@" + "OMS_LINK" + " t " + " ,FIXED_ACCOUNTS@" + "OMS_LINK" + " f WHERE t.id = d.transaction_id and f.id=d.FIXED_ACC AND d.amount<>0 AND  t.transaction_date BETWEEN ? AND ? " + " AND f.ACC_ID in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString3 + " )GROUP BY tdate";
/*      */
/*  947 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
/*  948 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*  949 */     localPreparedStatement.setTimestamp(2, localTimestamp2);
/*      */
/*  951 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/*  953 */     while (localResultSet.next()) {
/*  954 */       str1 = localResultSet.getString("tdate");
/*  955 */       double d1 = localResultSet.getDouble("debit");
/*  956 */       double d2 = localResultSet.getDouble("credit");
/*  957 */       localFinanceDayModel = (FinanceDayModel)localHashtable.get(str1);
/*  958 */       localFinanceDayModel.setAtDebit(d1);
/*  959 */       localFinanceDayModel.setAtCredit(d2);
/*  960 */       localFinanceModel.setActualDebits(localFinanceModel.getActualDebits() + localFinanceDayModel.getAtDebit());
/*  961 */       localFinanceModel.setActualCredits(localFinanceModel.getActualCredits() + localFinanceDayModel.getAtCredit());
/*      */     }
/*      */
/*  964 */     localPreparedStatement.close();
/*      */
/*  966 */     str2 = "SELECT SUM(d.transaction_type*d.amount) amount, SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) quantity  FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t  ,FIXED_ACCOUNTS@OMS_LINK f WHERE t.id = d.transaction_id and f.id=d.FIXED_ACC AND d.amount <> 0 AND  t.transaction_date < ?  AND f.ACC_ID in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString3;
/*      */
/*  969 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
/*      */
/*  971 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/*  972 */     localResultSet = localPreparedStatement.executeQuery();
/*  973 */     if (localResultSet.next()) {
/*  974 */       localFinanceModel.setInitACB(localResultSet.getDouble("quantity"));
/*      */     }
/*  976 */     localPreparedStatement.close();
/*      */
/*  978 */     localCalendar.setTime(paramDate1);
/*  979 */     localDate1 = localCalendar.getTime();
/*  980 */     while (localDate1.before(paramDate2)) {
/*  981 */       localFinanceDayModel = (FinanceDayModel)localHashtable.get(localSimpleDateFormat.format(localDate1));
/*  982 */       localFinanceModel.setInitACB(localFinanceModel.getInitACB() + localFinanceDayModel.getAtDebit() - localFinanceDayModel.getAtCredit());
/*  983 */       localFinanceDayModel.setActualBalance(localFinanceModel.getInitACB());
/*  984 */       localCalendar.add(5, 1);
/*  985 */       localCalendar.set(12, 0);
/*  986 */       localCalendar.set(13, 0);
/*  987 */       localCalendar.set(11, 0);
/*  988 */       localDate1 = localCalendar.getTime();
/*      */     }
/*  990 */     localFinanceModel.setDays(localHashtable);
/*      */
/*  992 */     str2 = "SELECT SUM(d.transaction_type*d.amount) amount,SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) quantity,MAX(t.transaction_date) lt FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t  ,FIXED_ACCOUNTS@OMS_LINK f WHERE t.id = d.transaction_id  and f.id=d.FIXED_ACC AND d.amount <> 0 AND f.ACC_ID in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString3;
/*      */
/*  995 */     Date localDate2 = new Date();
/*  996 */     localResultSet = this.stmt.executeQuery(str2);
/*  997 */     if (localResultSet.next())
/*  998 */       localFinanceModel.setBalance(localResultSet.getDouble("quantity"));
/*  999 */     localFinanceModel.setCblLocal(localResultSet.getDouble("amount"));
/* 1000 */     if (localResultSet.getTimestamp("lt") != null) {
/* 1001 */       localDate2.setTime(localResultSet.getTimestamp("lt").getTime());
/* 1002 */       localFinanceModel.setLastTransaction(localDate2);
/*      */     }
/* 1004 */     return localFinanceModel;
/*      */   }
/*      */
/*      */   public Hashtable dayReportsWithUser1(Date paramDate1, Date paramDate2, String paramString1, String paramString2) throws SQLException {
/* 1008 */     FinanceModel localFinanceModel1 = new FinanceModel();
/* 1009 */     Hashtable localHashtable1 = new Hashtable();
/*      */
/* 1011 */     Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
/* 1012 */     Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
/* 1013 */     int i = 0;
/*      */
/* 1015 */     Calendar localCalendar = Calendar.getInstance();
/* 1016 */     localCalendar.setTime(paramDate1);
/* 1017 */     SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
/* 1018 */     Date localDate1 = localCalendar.getTime();
/* 1019 */     FinanceDayModel localFinanceDayModel = null;
/* 1020 */     while (localDate1.before(paramDate2)) {
/* 1021 */       localFinanceDayModel = new FinanceDayModel();
/* 1022 */       localHashtable1.put(localSimpleDateFormat.format(localDate1), localFinanceDayModel);
/* 1023 */       localCalendar.add(5, 1);
/* 1024 */       localCalendar.set(12, 0);
/* 1025 */       localCalendar.set(13, 0);
/* 1026 */       localCalendar.set(11, 0);
/* 1027 */       localDate1 = localCalendar.getTime();
/*      */     }
/*      */
/* 1030 */     Hashtable localHashtable2 = new Hashtable();
/* 1031 */     double d1 = 0.0D;
/* 1032 */     double d2 = 0.0D;
/* 1033 */     FinanceModel localFinanceModel2 = null;
/* 1034 */     String str1 = null;
/* 1035 */     PreparedStatement localPreparedStatement = null;
/* 1036 */     ResultSet localResultSet = null;
/* 1037 */     String str2 = null;
/* 1038 */     String str3 = null;
/*      */
/* 1040 */     str1 = "SELECT u.id AS userid, sum(d.transaction_type*d.quantity) AS balance,u.first_name AS firstname, u.last_name AS lastname,i.name AS currency, i.purchase_price AS rate,(SELECT a.name FROM operational_hierarchy@OMS_LINK c, area_group@OMS_LINK a WHERE c.area_id = a.id AND c.user_id =u.id) area_name, MAX(t.transaction_date) lt FROM transaction_detail@OMS_LINK d, oms_users@OMS_LINK u, item@OMS_LINK i, fixed_accounts@OMS_LINK f  , transaction@OMS_LINK t  WHERE t.id = d.transaction_id AND f.id = d.fixed_acc AND f.user_id = u.id and d.amount>0 AND u.currency_id = i.id AND f.acc_id IN( SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id IN(" + paramString1 + ")" + ")  " + paramString2 + " GROUP BY u.id,i.name,i.purchase_price,u.first_name, u.last_name";
/*      */
/* 1047 */     System.out.println("SQL-5>" + str1);
/* 1048 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
/* 1049 */     localResultSet = localPreparedStatement.executeQuery();
/* 1050 */     Date localDate2 = new Date();
/* 1051 */     while (localResultSet.next()) {
/* 1052 */       str2 = localResultSet.getString("userid");
/* 1053 */       str3 = localResultSet.getString("firstname");
/* 1054 */       if ((str2 != null) && (str2.length() > 0) && (!str2.equals("null"))) {
/* 1055 */         if (!localHashtable2.containsKey(str2)) {
/* 1056 */           localHashtable2 = initUser(str2, str3, localTimestamp1, localTimestamp2, localHashtable2);
/*      */         }
/* 1058 */         if ((localHashtable2 == null) || (localHashtable2.size() <= 0) ||
/* 1059 */           (!localHashtable2.containsKey(str2))) continue;
/* 1060 */         localFinanceModel2 = (FinanceModel)localHashtable2.get(str2);
/*      */
/* 1062 */         if ((localResultSet.getString("lastname") != null) && (localResultSet.getString("lastname").length() > 0) && (!localResultSet.getString("lastname").equals("null")))
/* 1063 */           str3 = str3 + " " + localResultSet.getString("lastname");
/* 1064 */         localFinanceModel2.setUserName(str3);
/*      */
/* 1066 */         localFinanceModel2.setAreaName(localResultSet.getString("area_name"));
/*      */
/* 1068 */         localFinanceModel2.setRate(localResultSet.getDouble("rate"));
/* 1069 */         localFinanceModel2.setCurrencyName(localResultSet.getString("currency"));
/* 1070 */         localFinanceModel2.setCblLocal(localResultSet.getDouble("balance"));
/* 1071 */         if (localResultSet.getTimestamp("lt") != null) {
/* 1072 */           localDate2.setTime(localResultSet.getTimestamp("lt").getTime());
/* 1073 */           localFinanceModel2.setLastTransaction(localDate2);
/*      */         }
/*      */       }
/*      */
/*      */     }
/*      */
/* 1079 */     localPreparedStatement.close();
/*      */
/* 1081 */     String str4 = null;
/* 1082 */     double d3 = 0.0D;
/* 1083 */     double d4 = 0.0D;
/*      */
/* 1085 */     str1 = "SELECT SUM(dramount) debit,SUM(cramount) credit, tdate,user_id,user_name FROM( SELECT d.transaction_type ttype,decode(d.transaction_type,1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) dramount, decode(d.transaction_type,-1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) cramount, d.amount amount, to_char(t.transaction_date,'ddmmrr') tdate,f.user_id user_id,(select ou.first_name||' '||ou.last_name from oms_users@OMS_LINK ou where ou.id=f.user_id) user_name, (SELECT a.name FROM operational_hierarchy@OMS_LINK c, area_group@OMS_LINK a WHERE  c.area_id = a.id AND c.user_id =f.user_id) area_name FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, fixed_accounts@OMS_LINK f  WHERE f.id = d.fixed_acc AND t.id = d.transaction_id AND d.amount<>0 AND  t.transaction_date BETWEEN ? AND ? AND f.acc_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in(" + paramString1 + ")) " + paramString2 + " )GROUP BY tdate,user_id ";
/*      */
/* 1094 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
/* 1095 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/* 1096 */     localPreparedStatement.setTimestamp(2, localTimestamp2);
/*      */
/* 1098 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/* 1100 */     while (localResultSet.next()) {
/* 1101 */       str4 = localResultSet.getString("tdate");
/*      */
/* 1103 */       str2 = localResultSet.getString("user_id");
/* 1104 */       str3 = localResultSet.getString("user_name");
/* 1105 */       d3 = localResultSet.getDouble("debit");
/* 1106 */       d4 = localResultSet.getDouble("credit");
/*      */
/* 1108 */       if (!localHashtable2.containsKey(str2)) {
/* 1109 */         localHashtable2 = initUser(str2, str3, localTimestamp1, localTimestamp2, localHashtable2);
/* 1110 */         localFinanceModel2 = (FinanceModel)localHashtable2.get(str2);
/* 1111 */         localFinanceModel2.setArea(localResultSet.getString("area_name"));
/*      */       }
/*      */       else {
/* 1114 */         localFinanceModel2 = (FinanceModel)localHashtable2.get(str2);
/* 1115 */       }localHashtable1 = localFinanceModel2.getDays();
/* 1116 */       localFinanceDayModel = (FinanceDayModel)localHashtable1.get(str4);
/* 1117 */       localFinanceDayModel.setAtDebit(d3);
/* 1118 */       localFinanceDayModel.setAtCredit(d4);
/*      */     }
/* 1120 */     localPreparedStatement.close();
/*      */
/* 1122 */     Enumeration localEnumeration1 = null;
/* 1123 */     str1 = "SELECT SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) amount,SUM(d.transaction_type*d.quantity) quantity,f.user_id user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t , fixed_accounts@OMS_LINK f  WHERE f.id = d.fixed_acc AND t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date < ?  AND f.acc_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in(" + paramString1 + ")) " + paramString2 + " group by f.user_id";
/*      */
/* 1127 */     localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
/* 1128 */     localPreparedStatement.setTimestamp(1, localTimestamp1);
/* 1129 */     localResultSet = localPreparedStatement.executeQuery();
/*      */
/* 1131 */     while (localResultSet.next()) {
/* 1132 */       str2 = localResultSet.getString("user_id");
/* 1133 */       d2 = localResultSet.getDouble("amount");
/*      */
/* 1135 */       if ((str2 == null) || (str2.length() <= 0) || (str2.equals("null")) ||
/* 1136 */         (localHashtable2 == null) || (localHashtable2.size() <= 0) ||
/* 1137 */         (!localHashtable2.containsKey(str2))) continue;
/* 1138 */       localFinanceModel2 = (FinanceModel)localHashtable2.get(str2);
/* 1139 */       localFinanceModel2.setInitACB(d2);
/*      */     }
/*      */
/* 1145 */     localPreparedStatement.close();
/*      */
/* 1148 */     localCalendar = Calendar.getInstance();
/* 1149 */     localDate1 = localCalendar.getTime();
/*      */
/* 1151 */     localCalendar.setTime(paramDate1);
/* 1152 */     double d5 = 0.0D;
/*      */
/* 1154 */     Enumeration localEnumeration2 = localHashtable2.keys();
/* 1155 */     while (localEnumeration2.hasMoreElements()) {
/* 1156 */       localCalendar.setTime(paramDate1);
/* 1157 */       localDate1 = localCalendar.getTime();
/* 1158 */       str2 = (String)localEnumeration2.nextElement();
/* 1159 */       localFinanceModel2 = (FinanceModel)localHashtable2.get(str2);
/* 1160 */       d5 = localFinanceModel2.getInitACB();
/* 1161 */       localHashtable1 = localFinanceModel2.getDays();
/* 1162 */       localEnumeration1 = localHashtable1.keys();
/* 1163 */       while (localDate1.before(paramDate2)) {
/* 1164 */         localFinanceDayModel = (FinanceDayModel)localHashtable1.get(localSimpleDateFormat.format(localDate1));
/* 1165 */         d5 = d5 + localFinanceDayModel.getAtDebit() - localFinanceDayModel.getAtCredit();
/* 1166 */         localFinanceDayModel.setActualBalance(d5);
/*      */
/* 1168 */         localCalendar.add(5, 1);
/* 1169 */         localCalendar.set(12, 0);
/* 1170 */         localCalendar.set(13, 0);
/* 1171 */         localCalendar.set(11, 0);
/* 1172 */         localDate1 = localCalendar.getTime();
/*      */       }
/*      */     }
/* 1175 */     return localHashtable2;
/*      */   }
/*      */
/*      */   public FinanceModel getBalanceGroups(Connector paramConnector, Date paramDate1, Date paramDate2, String paramString1, String paramString2)
/*      */     throws SQLException, MaestroException
/*      */   {
/* 1188 */     FinanceModel localFinanceModel = new FinanceModel();
/* 1189 */     SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
/* 1190 */     Hashtable localHashtable = new Hashtable();
/*      */
/* 1192 */     double d1 = 0.0D;
/* 1193 */     double d2 = 0.0D;
/* 1194 */     double d3 = 0.0D;
/* 1195 */     double d4 = 0.0D;
/*      */
/* 1197 */     String str = "SELECT SUM(dramount) debit,SUM(cramount) credit FROM(  SELECT decode(transaction_type,1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) dramount,decode(transaction_type,-1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) cramount FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, FIXED_ACCOUNTS@OMS_LINK f WHERE t.id = d.transaction_id and f.id=d.FIXED_ACC AND d.amount<>0 AND to_char(t.transaction_date,'ddmmrr')= " + localSimpleDateFormat.format(paramDate1) + " " + "AND f.ACC_ID in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString2 + " )";
/*      */
/* 1203 */     ResultSet localResultSet = paramConnector.executeQuery(str);
/* 1204 */     while (localResultSet.next()) {
/* 1205 */       d1 = localResultSet.getDouble("debit");
/* 1206 */       d2 = localResultSet.getDouble("credit");
/*      */     }
/*      */
/* 1209 */     str = "SELECT SUM(d.transaction_type*d.amount) amount, SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) quantity  FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, FIXED_ACCOUNTS@OMS_LINK f WHERE t.id = d.transaction_id and f.id=d.FIXED_ACC AND d.amount <> 0 AND  t.transaction_date < to_date('" + localSimpleDateFormat.format(paramDate1) + " 23:59:59','ddmmrr hh24:mi:ss') " + " AND f.ACC_ID in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString2;
/*      */
/* 1213 */     localResultSet = paramConnector.executeQuery(str);
/* 1214 */     while (localResultSet.next())
/* 1215 */       d3 = localResultSet.getDouble("quantity");
/* 1216 */     FinanceDayModel localFinanceDayModel = new FinanceDayModel();
/* 1217 */     localFinanceDayModel.setDate(paramDate1);
/* 1218 */     localFinanceDayModel.setAtDebit(d1);
/* 1219 */     localFinanceDayModel.setAtCredit(d2);
/* 1220 */     localFinanceDayModel.setActualBalance(d3);
/* 1221 */     localHashtable.put(localSimpleDateFormat.format(paramDate1), localFinanceDayModel);
/*      */
/* 1223 */     if (paramDate2 != null) {
/* 1224 */       str = "SELECT SUM(dramount) debit,SUM(cramount) credit FROM(  SELECT decode(transaction_type,1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) dramount,decode(transaction_type,-1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) cramount FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, FIXED_ACCOUNTS@OMS_LINK f WHERE t.id = d.transaction_id and f.id=d.FIXED_ACC AND d.amount<>0 AND to_char(t.transaction_date,'ddmmrr')= " + localSimpleDateFormat.format(paramDate2) + " " + "AND f.ACC_ID in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString2 + " )";
/*      */
/* 1230 */       localResultSet = paramConnector.executeQuery(str);
/* 1231 */       while (localResultSet.next()) {
/* 1232 */         d1 = localResultSet.getDouble("debit");
/* 1233 */         d2 = localResultSet.getDouble("credit");
/*      */       }
/*      */
/* 1236 */       str = "SELECT SUM(d.transaction_type*d.amount) amount, SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) quantity  FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, FIXED_ACCOUNTS@OMS_LINK f WHERE t.id = d.transaction_id and f.id=d.FIXED_ACC AND d.amount <> 0 AND  t.transaction_date < to_date('" + localSimpleDateFormat.format(paramDate1) + " 23:59:59','ddmmrr hh24:mi:ss') " + " AND f.ACC_ID in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString2;
/*      */
/* 1240 */       localResultSet = paramConnector.executeQuery(str);
/* 1241 */       while (localResultSet.next()) {
/* 1242 */         d3 = localResultSet.getDouble("quantity");
/*      */       }
/* 1244 */       localFinanceDayModel = new FinanceDayModel();
/* 1245 */       localFinanceDayModel.setDate(paramDate1);
/* 1246 */       localFinanceDayModel.setAtDebit(d1);
/* 1247 */       localFinanceDayModel.setAtCredit(d2);
/* 1248 */       localFinanceDayModel.setActualBalance(d3);
/* 1249 */       localHashtable.put(localSimpleDateFormat.format(paramDate2), localFinanceDayModel);
/*      */     }
/*      */
/* 1252 */     str = "SELECT SUM(d.transaction_type*d.amount) amount, SUM(d.transaction_type*d.quantity) quantity  FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, FIXED_ACCOUNTS@OMS_LINK f WHERE t.id = d.transaction_id and f.id=d.FIXED_ACC AND d.amount <> 0  AND f.ACC_ID in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString2;
/*      */
/* 1256 */     localResultSet = paramConnector.executeQuery(str);
/* 1257 */     while (localResultSet.next()) {
/* 1258 */       d3 = localResultSet.getDouble("amount");
/* 1259 */       d4 = localResultSet.getDouble("quantity");
/*      */     }
/* 1261 */     localFinanceModel.setCblLocal(d4);
/* 1262 */     localFinanceModel.setBalance(d3);
/* 1263 */     localFinanceModel.setDays(localHashtable);
/* 1264 */     return localFinanceModel;
/*      */   }
/*      */
/*      */   public Hashtable getBalanceByUser(Connector paramConnector, Date paramDate1, Date paramDate2, String paramString1, String paramString2)
/*      */     throws SQLException, MaestroException
/*      */   {
/* 1277 */     FinanceModel localFinanceModel = null;
/* 1278 */     FinanceDayModel localFinanceDayModel = null;
/* 1279 */     SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
/* 1280 */     Hashtable localHashtable1 = null;
/* 1281 */     Hashtable localHashtable2 = new Hashtable();
/*      */
/* 1283 */     double d1 = 0.0D;
/* 1284 */     double d2 = 0.0D;
/* 1285 */     double d3 = 0.0D;
/* 1286 */     double d4 = 0.0D;
/* 1287 */     String str1 = "";
/* 1288 */     String str2 = "";
/*      */
/* 1290 */     String str3 = "SELECT SUM(d.transaction_type*d.amount) amount, SUM(d.transaction_type*d.quantity) quantity,d.user_id,(SELECT first_name FROM oms_users@OMS_LINK WHERE id = d.user_id ) uname  FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount <> 0  AND d.ACC_ID in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString2 + "  GROUP BY d.user_id";
/*      */
/* 1294 */     ResultSet localResultSet = paramConnector.executeQuery(str3);
/* 1295 */     while (localResultSet.next()) {
/* 1296 */       d3 = localResultSet.getDouble("amount");
/* 1297 */       d4 = localResultSet.getDouble("quantity");
/* 1298 */       str1 = localResultSet.getString("user_id");
/* 1299 */       str2 = localResultSet.getString("uname");
/* 1300 */       localFinanceModel = new FinanceModel();
/* 1301 */       localFinanceModel.setBalance(d3);
/* 1302 */       localFinanceModel.setCblLocal(d4);
/* 1303 */       localFinanceModel.setUserName(str2);
/* 1304 */       localHashtable1 = new Hashtable();
/* 1305 */       localHashtable1.put(localSimpleDateFormat.format(paramDate1), new FinanceDayModel());
/* 1306 */       if (paramDate2 != null)
/* 1307 */         localHashtable1.put(localSimpleDateFormat.format(paramDate2), new FinanceDayModel());
/* 1308 */       localFinanceModel.setDays(localHashtable1);
/* 1309 */       localHashtable2.put(str1, localFinanceModel);
/*      */     }
/*      */
/* 1312 */     str3 = "SELECT user_id, SUM(dramount) debit,SUM(cramount) credit FROM(  SELECT decode(transaction_type,1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) dramount, decode(transaction_type,-1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) cramount,d.user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount<>0 AND to_char(t.transaction_date,'ddmmrr')= " + localSimpleDateFormat.format(paramDate1) + " " + "AND d.ACC_ID in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString2 + " ) GROUP BY user_id";
/*      */
/* 1319 */     localResultSet = paramConnector.executeQuery(str3);
/* 1320 */     while (localResultSet.next()) {
/* 1321 */       str1 = localResultSet.getString("user_id");
/* 1322 */       localFinanceModel = (FinanceModel)localHashtable2.get(str1);
/* 1323 */       localHashtable1 = localFinanceModel.getDays();
/* 1324 */       localFinanceDayModel = (FinanceDayModel)localHashtable1.get(localSimpleDateFormat.format(paramDate1));
/* 1325 */       d1 = localResultSet.getDouble("debit");
/* 1326 */       d2 = localResultSet.getDouble("credit");
/* 1327 */       localFinanceDayModel.setAtDebit(d1);
/* 1328 */       localFinanceDayModel.setAtCredit(d2);
/*      */     }
/*      */
/* 1331 */     str3 = "SELECT SUM(d.transaction_type*d.amount) amount, SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) quantity, d.user_id  FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t f WHERE t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date < to_date('" + localSimpleDateFormat.format(paramDate1) + " 23:59:59','ddmmrr hh24:mi:ss') " + " AND d.ACC_ID in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString2 + " " + "GROUP BY d.user_id";
/*      */
/* 1336 */     localResultSet = paramConnector.executeQuery(str3);
/* 1337 */     while (localResultSet.next()) {
/* 1338 */       str1 = localResultSet.getString("user_id");
/* 1339 */       d3 = localResultSet.getDouble("quantity");
/* 1340 */       localFinanceModel = (FinanceModel)localHashtable2.get(str1);
/* 1341 */       localHashtable1 = localFinanceModel.getDays();
/* 1342 */       localFinanceDayModel = (FinanceDayModel)localHashtable1.get(localSimpleDateFormat.format(paramDate1));
/* 1343 */       localFinanceDayModel.setActualBalance(d3);
/*      */     }
/*      */
/* 1346 */     if (paramDate2 != null) {
/* 1347 */       str3 = "SELECT user_id, SUM(dramount) debit,SUM(cramount) credit FROM(  SELECT decode(transaction_type,1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) dramount, decode(transaction_type,-1,d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) cramount,d.user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id d.amount<>0 AND to_char(t.transaction_date,'ddmmrr')= " + localSimpleDateFormat.format(paramDate2) + " " + "AND d.ACC_ID in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString2 + " ) GROUP BY user_id";
/*      */
/* 1355 */       localResultSet = paramConnector.executeQuery(str3);
/* 1356 */       while (localResultSet.next()) {
/* 1357 */         str1 = localResultSet.getString("user_id");
/* 1358 */         localFinanceModel = (FinanceModel)localHashtable2.get(str1);
/* 1359 */         localHashtable1 = localFinanceModel.getDays();
/* 1360 */         localFinanceDayModel = (FinanceDayModel)localHashtable1.get(localSimpleDateFormat.format(paramDate2));
/* 1361 */         d1 = localResultSet.getDouble("debit");
/* 1362 */         d2 = localResultSet.getDouble("credit");
/* 1363 */         localFinanceDayModel.setAtDebit(d1);
/* 1364 */         localFinanceDayModel.setAtCredit(d2);
/*      */       }
/*      */
/* 1367 */       str3 = "SELECT SUM(d.transaction_type*d.amount) amount, SUM(d.transaction_type*d.quantity*(SELECT purchase_price FROM item@oms_link WHERE id = f.currency_id)) quantity, d.user_id  FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t, FIXED_ACCOUNTS@OMS_LINK f WHERE t.id = d.transaction_id and f.id=d.FIXED_ACC AND d.amount <> 0 AND  t.transaction_date < to_date('" + localSimpleDateFormat.format(paramDate2) + " 23:59:59','ddmmrr hh24:mi:ss') " + " AND f.ACC_ID in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + paramString2 + " " + "GROUP BY d.user_id";
/*      */
/* 1372 */       localResultSet = paramConnector.executeQuery(str3);
/* 1373 */       while (localResultSet.next()) {
/* 1374 */         str1 = localResultSet.getString("user_id");
/* 1375 */         d3 = localResultSet.getDouble("quantity");
/* 1376 */         localFinanceModel = (FinanceModel)localHashtable2.get(str1);
/* 1377 */         localHashtable1 = localFinanceModel.getDays();
/* 1378 */         localFinanceDayModel = (FinanceDayModel)localHashtable1.get(localSimpleDateFormat.format(paramDate2));
/* 1379 */         localFinanceDayModel.setActualBalance(d3);
/*      */       }
/*      */     }
/*      */
/* 1383 */     return localHashtable2;
/*      */   }
/*      */
/*      */   public double getTotalAmount(Connector paramConnector, int paramInt1, int paramInt2, String paramString1, String paramString2)
/*      */     throws MaestroException, SQLException
/*      */   {
/* 1395 */     double d = 0.0D;
/* 1396 */     String str = "SELECT SUM(transaction_type*amount) AS balance FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND t.transaction_date BETWEEN to_date('" + paramString1 + "','rrrr-mm-dd') AND to_date('" + paramString2 + " 23:59:59','rrrr-mm-dd hh24:mi:ss') " + "AND d.account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id STARt WITH id = " + paramInt1 + ") " + "AND d.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id STARt WITH id = " + paramInt2 + ")";
/*      */
/* 1401 */     ResultSet localResultSet = paramConnector.executeQuery(str);
/* 1402 */     if (localResultSet.next())
/* 1403 */       d = localResultSet.getDouble("balance");
/* 1404 */     return d;
/*      */   }
/*      */
/*      */   public double getTotalAmount(Connector paramConnector, int paramInt1, int paramInt2, String paramString1, String paramString2, double paramDouble)
/*      */     throws MaestroException, SQLException
/*      */   {
/* 1416 */     double d = 0.0D;
/* 1417 */     String str = "SELECT SUM(transaction_type*quantity*DECODE(d.item_id,30," + paramDouble + ",NVL((Select 1/rate from reference_rate@" + "OMS_LINK" + " r where r.user_id=d.user_id and r.item_id =d.item_id and effect_date <= t.transaction_date and (end_date is null or end_date>=t.transaction_date)) " + ",(Select purchase_price from item@" + "OMS_LINK" + " where id = d.item_id)) * NVL((Select rate from reference_rate@" + "OMS_LINK" + " r where r.user_id=d.user_id and r.item_id =30 and effect_date <= t.transaction_date and (end_date is null or end_date>=t.transaction_date)) " + ",(Select 1/purchase_price from item@" + "OMS_LINK" + " where id = 30))*" + paramDouble + ")) AS balance " + "FROM transaction_detail@" + "OMS_LINK" + " d, transaction@" + "OMS_LINK" + " t, item@" + "OMS_LINK" + " i " + "WHERE t.id = d.transaction_id AND d.amount>0 AND d.item_id = i.id AND t.transaction_date BETWEEN to_date('" + paramString1 + "','rrrr-mm-dd') AND to_date('" + paramString2 + " 23:59:59','rrrr-mm-dd hh24:mi:ss') " + "AND d.account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id STARt WITH id = " + paramInt1 + ") " + "AND d.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id STARt WITH id = " + paramInt2 + ")";
/*      */
/* 1425 */     ResultSet localResultSet = paramConnector.executeQuery(str);
/* 1426 */     if (localResultSet.next())
/* 1427 */       d = localResultSet.getDouble("balance");
/* 1428 */     return d;
/*      */   }
/*      */
/*      */   public double getTotalAmount(Connector paramConnector, int paramInt1, int paramInt2, String paramString1, String paramString2, double paramDouble1, double paramDouble2)
/*      */     throws MaestroException, SQLException
/*      */   {
/* 1440 */     double d = 0.0D;
/* 1441 */     String str = "SELECT SUM(transaction_type*quantity*DECODE(d.item_id,30," + paramDouble1 + ",NVL((Select 1/rate from reference_rate@" + "OMS_LINK" + " r where r.user_id=d.user_id and r.item_id =d.item_id and effect_date <= t.transaction_date and (end_date is null or end_date>=t.transaction_date)) " + ",(Select purchase_price from item@" + "OMS_LINK" + " where id = d.item_id)) * DECODE(sign(NVL((Select rate from reference_rate@" + "OMS_LINK" + " r where r.user_id=d.user_id and r.item_id =30 and effect_date <= t.transaction_date and (end_date is null or end_date>=t.transaction_date)),(Select 1/purchase_price from item@" + "OMS_LINK" + " where id = 30))-70),1," + paramDouble2 + ",NVL((Select rate from reference_rate@" + "OMS_LINK" + " r where r.user_id=d.user_id and r.item_id =30 and effect_date <= t.transaction_date and (end_date is null or end_date>=t.transaction_date)) " + ",(Select 1/purchase_price from item@" + "OMS_LINK" + " where id = 30)))*" + paramDouble1 + ")) AS balance " + "FROM transaction_detail@" + "OMS_LINK" + " d, transaction@" + "OMS_LINK" + " t, item@" + "OMS_LINK" + " i " + "WHERE t.id = d.transaction_id AND d.amount>0 AND d.item_id = i.id AND t.transaction_date BETWEEN to_date('" + paramString1 + "','rrrr-mm-dd') AND to_date('" + paramString2 + " 23:59:59','rrrr-mm-dd hh24:mi:ss') " + "AND d.account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id STARt WITH id = " + paramInt1 + ") " + "AND d.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id STARt WITH id = " + paramInt2 + ")";
/*      */
/* 1448 */     System.out.println("" + str);
/* 1449 */     ResultSet localResultSet = paramConnector.executeQuery(str);
/* 1450 */     if (localResultSet.next())
/* 1451 */       d = localResultSet.getDouble("balance");
/* 1452 */     return d;
/*      */   }
/*      */
/*      */   public Vector getDashBoardTotalAmount(Connector paramConnector, int paramInt1, int paramInt2, String paramString1, String paramString2, double paramDouble1, double paramDouble2)
/*      */     throws MaestroException, SQLException
/*      */   {
/* 1465 */     Vector localVector = null;
/* 1466 */     localVector = new Vector();
/* 1467 */     String str = "";
/* 1468 */     String[] arrayOfString = null;
/*      */
/* 1470 */     str = "SELECT to_date(to_char(transaction_date,'rrrr-mm-dd'),'rrrr-mm-dd')-to_date('" + paramString1 + "','rrrr-mm-dd') dt," + " sum(quantity*DECODE(b.item_id,30," + paramDouble1 + ",NVL((Select 1/rate from reference_rate@" + "OMS_LINK" + "  r where r.user_id=b.user_id and r.item_id =b.item_id" + " and effect_date <= b.transaction_date and (end_date is null or end_date>=b.transaction_date))" + " ,(Select purchase_price from item@" + "OMS_LINK" + " where id = b.item_id)) * DECODE(sign(NVL((Select rate from reference_rate@" + "OMS_LINK" + " r" + " where r.user_id=b.user_id and r.item_id =30 and effect_date <= b.transaction_date" + " and (end_date is null or end_date>=b.transaction_date)),(Select 1/purchase_price from item@" + "OMS_LINK" + " where id = 30))-70),1," + paramDouble2 + "," + " NVL((Select rate from reference_rate@" + "OMS_LINK" + " r where r.user_id=b.user_id and r.item_id =30" + " and effect_date <= b.transaction_date and (end_date is null or end_date>=b.transaction_date))" + " ,(Select 1/purchase_price from item@" + "OMS_LINK" + " where id = 30)))*" + paramDouble1 + ")) balance" + " from balance_sheet_model_view@" + "OMS_LINK" + " b where b.amount <> 0 and" + " b.transaction_date between to_date('" + paramString1 + " 00:00:00','rrrr-mm-dd hh24:mi:ss')" + " and to_date('" + paramString2 + " 23:59:59','rrrr-mm-dd hh24:mi:ss')" + " AND b.account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id STARt WITH id = " + paramInt1 + ")" + " AND b.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id start with id = " + paramInt2 + ")" + " group by b.transaction_date";
/*      */
/* 1485 */     System.out.println("" + str);
/* 1486 */     ResultSet localResultSet = paramConnector.executeQuery(str);
/* 1487 */     while (localResultSet.next())
/*      */     {
/* 1489 */       arrayOfString = new String[2];
/* 1490 */       arrayOfString[0] = localResultSet.getString("dt");
/* 1491 */       arrayOfString[1] = localResultSet.getString("balance");
/* 1492 */       localVector.add(arrayOfString);
/*      */     }
/* 1494 */     return localVector;
/*      */   }
/*      */
/*      */   public Vector getDashBoardTotalAmount(Connector paramConnector, int paramInt1, int paramInt2, String paramString1, String paramString2, double paramDouble) throws MaestroException, SQLException
/*      */   {
/* 1499 */     Vector localVector = null;
/* 1500 */     localVector = new Vector();
/* 1501 */     String str = "";
/* 1502 */     String[] arrayOfString = null;
/*      */
/* 1504 */     str = "SELECT to_date(to_char(transaction_date,'rrrr-mm-dd'),'rrrr-mm-dd')-to_date('" + paramString1 + "','rrrr-mm-dd') dt," + " sum(quantity*DECODE(b.item_id,30," + paramDouble + ",NVL((Select 1/rate from reference_rate@" + "OMS_LINK" + "  r where r.user_id=b.user_id and r.item_id =b.item_id" + " and effect_date <= b.transaction_date and (end_date is null or end_date>=b.transaction_date))" + " ,(Select purchase_price from item@" + "OMS_LINK" + " where id = b.item_id)) * " + " NVL((Select rate from reference_rate@" + "OMS_LINK" + " r where r.user_id=b.user_id and r.item_id =30" + " and effect_date <= b.transaction_date and (end_date is null or end_date>=b.transaction_date))" + " ,(Select 1/purchase_price from item@" + "OMS_LINK" + " where id = 30)))*" + paramDouble + ") balance" + " from balance_sheet_model_view@" + "OMS_LINK" + " b where b.amount <> 0 and" + " b.transaction_date between to_date('" + paramString1 + " 00:00:00','rrrr-mm-dd hh24:mi:ss')" + " and to_date('" + paramString2 + " 23:59:59','rrrr-mm-dd hh24:mi:ss')" + " AND b.account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id STARt WITH id = " + paramInt1 + ")" + " AND b.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id start with id = " + paramInt2 + ")" + " group by b.transaction_date order by dt";
/*      */
/* 1517 */     System.out.println("" + str);
/*      */
/* 1520 */     ResultSet localResultSet = paramConnector.executeQuery(str);
/* 1521 */     while (localResultSet.next())
/*      */     {
/* 1523 */       arrayOfString = new String[2];
/* 1524 */       arrayOfString[0] = localResultSet.getString("dt");
/* 1525 */       arrayOfString[1] = localResultSet.getString("balance");
/* 1526 */       localVector.add(arrayOfString);
/*      */     }
/* 1528 */     return localVector;
/*      */   }
/*      */
/*      */   public Vector getAmountByUser(Connector paramConnector, int paramInt1, int paramInt2, String paramString1, String paramString2)
/*      */     throws MaestroException, SQLException
/*      */   {
/* 1545 */     Vector localVector = new Vector();
/* 1546 */     String str = "SELECT d.user_id,(SELECT first_name FROM oms_users@OMS_LINK WHERE id =d.user_id ) uname, SUM(transaction_type*amount) AS balance FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND t.transaction_date BETWEEN to_date('" + paramString1 + "','rrrr-mm-dd') AND to_date('" + paramString2 + " 23:59:59','rrrr-mm-dd hh24:mi:ss') " + "AND d.account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id STARt WITH id = " + paramInt1 + ") " + "AND d.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id STARt WITH id = " + paramInt2 + ") " + "GROUP BY d.user_id order by balance asc";
/*      */
/* 1553 */     ResultSet localResultSet = paramConnector.executeQuery(str);
/* 1554 */     while (localResultSet.next()) {
/* 1555 */       localVector.add(new String[] { localResultSet.getString("user_id"), localResultSet.getString("uname"), localResultSet.getString("balance") });
/*      */     }
/* 1557 */     return localVector;
/*      */   }
/*      */
/*      */   public Vector getAmountByUser(Connector paramConnector, int paramInt1, int paramInt2, String paramString1, String paramString2, double paramDouble)
/*      */     throws MaestroException, SQLException
/*      */   {
/* 1572 */     Vector localVector = new Vector();
/* 1573 */     String str = "SELECT d.user_id,(SELECT first_name FROM oms_users@OMS_LINK WHERE id =d.user_id ) uname, SUM(transaction_type*quantity*DECODE(d.item_id,30," + paramDouble + ",NVL((Select 1/rate from reference_rate@" + "OMS_LINK" + " r where r.user_id=d.user_id and r.item_id =d.item_id and effect_date <= t.transaction_date and (end_date is null or end_date>=t.transaction_date)) " + ",(Select purchase_price from item@" + "OMS_LINK" + " where id = d.item_id)) * NVL((Select rate from reference_rate@" + "OMS_LINK" + " r where r.user_id=d.user_id and r.item_id =30 and effect_date <= t.transaction_date and (end_date is null or end_date>=t.transaction_date)) " + ",(Select 1/purchase_price from item@" + "OMS_LINK" + " where id = 30))*" + paramDouble + ")) AS balance " + "FROM transaction_detail@" + "OMS_LINK" + " d, transaction@" + "OMS_LINK" + " t,  item@" + "OMS_LINK" + " i " + "WHERE t.id = d.transaction_id AND i.id = d.item_id AND d.amount>0 AND t.transaction_date BETWEEN to_date('" + paramString1 + "','rrrr-mm-dd') AND to_date('" + paramString2 + " 23:59:59','rrrr-mm-dd hh24:mi:ss') " + "AND  d.account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id STARt WITH id = " + paramInt1 + ") " + "AND d.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id STARt WITH id = " + paramInt2 + ") " + "GROUP BY d.user_id order by balance asc";
/*      */
/* 1582 */     ResultSet localResultSet = paramConnector.executeQuery(str);
/* 1583 */     while (localResultSet.next()) {
/* 1584 */       localVector.add(new String[] { localResultSet.getString("user_id"), localResultSet.getString("uname"), localResultSet.getString("balance") });
/*      */     }
/* 1586 */     return localVector;
/*      */   }
/*      */
/*      */   public Vector getAmountByUser(Connector paramConnector, int paramInt1, int paramInt2, String paramString1, String paramString2, double paramDouble1, double paramDouble2)
/*      */     throws MaestroException, SQLException
/*      */   {
/* 1601 */     Vector localVector = new Vector();
/* 1602 */     String str = "SELECT d.user_id,(SELECT first_name FROM oms_users@OMS_LINK WHERE id =d.user_id ) uname, SUM(transaction_type*quantity*DECODE(d.item_id,30," + paramDouble1 + ",NVL((Select 1/rate from reference_rate@" + "OMS_LINK" + " r where r.user_id=d.user_id and r.item_id =d.item_id and effect_date <= t.transaction_date and (end_date is null or end_date>=t.transaction_date)) " + ",(Select purchase_price from item@" + "OMS_LINK" + " where id = d.item_id)) * DECODE(sign(NVL((Select rate from reference_rate@" + "OMS_LINK" + " r where r.user_id=d.user_id and r.item_id =30 and effect_date <= t.transaction_date and (end_date is null or end_date>=t.transaction_date)),(Select 1/purchase_price from item@" + "OMS_LINK" + " where id = 30))-70),1," + paramDouble2 + ",NVL((Select rate from reference_rate@" + "OMS_LINK" + " r where r.user_id=d.user_id and r.item_id =30 and effect_date <= t.transaction_date and (end_date is null or end_date>=t.transaction_date)) " + ",(Select 1/purchase_price from item@" + "OMS_LINK" + " where id = 30)))*" + paramDouble1 + ")) AS balance " + "FROM transaction_detail@" + "OMS_LINK" + " d, transaction@" + "OMS_LINK" + " t,  item@" + "OMS_LINK" + " i " + "WHERE t.id = d.transaction_id AND i.id = d.item_id AND d.amount>0 AND t.transaction_date BETWEEN to_date('" + paramString1 + "','rrrr-mm-dd') AND to_date('" + paramString2 + " 23:59:59','rrrr-mm-dd hh24:mi:ss') " + "AND  d.account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id STARt WITH id = " + paramInt1 + ") " + "AND d.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id STARt WITH id = " + paramInt2 + ") " + "GROUP BY d.user_id order by balance asc";
/*      */
/* 1611 */     ResultSet localResultSet = paramConnector.executeQuery(str);
/* 1612 */     while (localResultSet.next()) {
/* 1613 */       localVector.add(new String[] { localResultSet.getString("user_id"), localResultSet.getString("uname"), localResultSet.getString("balance") });
/*      */     }
/* 1615 */     return localVector;
/*      */   }
/*      */
/*      */   public Vector getUserBalanceSheet(Connector paramConnector, int paramInt1, Date paramDate1, Date paramDate2, int paramInt2, int paramInt3)
/*      */     throws MaestroException, SQLException
/*      */   {
/* 1628 */     boolean bool = false;
/*      */
/* 1630 */     BalanceSheet localBalanceSheet = null;
/* 1631 */     SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
/*      */
/* 1633 */     Hashtable localHashtable = new Hashtable();
/* 1634 */     String str1 = null;
/* 1635 */     ResultSet localResultSet = null;
/* 1636 */     String str2 = null;
/* 1637 */     str1 = "SELECT wm_concat(id) FROM( SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR parent_id = id START WITH id = " + paramInt1 + " ORDER BY level DESC)";
/* 1638 */     localResultSet = paramConnector.executeQuery(str1);
/*      */
/* 1640 */     if (localResultSet.next()) {
/* 1641 */       str2 = localResultSet.getString(1);
/*      */     }
/*      */
/* 1645 */     str1 = "SELECT d.user_id,(SELECT first_name FROM oms_users@OMS_LINK WHERE id = d.user_id) name,SUM(transaction_type*quantity*(SELECT purchase_price FROM item@OMS_LINK WHERE id = d.item_id)) usd, SUM(transaction_type*quantity) local, max(transaction_date) lt FROM transaction_detail@OMS_LINK d,transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount>0 AND d.account_id IN(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id = " + paramInt1 + ") " + "AND d.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + paramInt3 + ") " + "GROUP BY d.user_id";
/*      */
/* 1653 */     localResultSet = paramConnector.executeQuery(str1);
/* 1654 */     while (localResultSet.next()) {
/* 1655 */       localBalanceSheet = new BalanceSheet();
/* 1656 */       localBalanceSheet.setId(localResultSet.getInt("user_id"));
/* 1657 */       localBalanceSheet.setName(localResultSet.getString("name"));
/* 1658 */       localBalanceSheet.setLevel(paramInt2 + 1);
/* 1659 */       localBalanceSheet.setAccountMode(bool);
/* 1660 */       localBalanceSheet.setUSDBalance(localResultSet.getDouble("usd"));
/* 1661 */       localBalanceSheet.setLocalBalance(localResultSet.getDouble("local"));
/* 1662 */       localBalanceSheet.setHierarchy(str2 + ",u" + localBalanceSheet.getId());
/* 1663 */       if (localResultSet.getTimestamp("lt") != null) {
/* 1664 */         localBalanceSheet.setLastTransaction(new Date(localResultSet.getTimestamp("lt").getTime()));
/* 1665 */         localHashtable.put(localBalanceSheet.getId() + "", localBalanceSheet);
/*      */       }
/*      */     }
/* 1668 */     paramConnector.closeCursor();
/*      */
/* 1670 */     double d = 0.0D;
/* 1671 */     Enumeration localEnumeration = localHashtable.elements();
/* 1672 */     while (localEnumeration.hasMoreElements()) {
/* 1673 */       localBalanceSheet = (BalanceSheet)localEnumeration.nextElement();
/* 1674 */       str1 = "SELECT i.name, i.purchase_price FROM oms_users@OMS_LINK u, item@OMS_LINK i WHERE u.currency_id = i.id AND u.id = " + localBalanceSheet.getId();
/* 1675 */       localResultSet = paramConnector.executeQuery(str1);
/* 1676 */       if (localResultSet.next()) {
/* 1677 */         localBalanceSheet.setCurrency(localResultSet.getString("name"));
/* 1678 */         d = localResultSet.getDouble("purchase_price");
/*      */       }
/*      */
/* 1681 */       paramConnector.closeCursor();
/*      */     }
/*      */
/* 1684 */     str1 = "SELECT d.user_id, SUM(transaction_type*quantity*(SELECT purchase_price FROM item@OMS_LINK WHERE id = d.item_id)) local FROM transaction_detail@OMS_LINK d,transaction@OMS_LINK t WHERE t.id = d.transaction_id  AND d.amount>0 AND t.transaction_date < to_date('" + localSimpleDateFormat.format(paramDate1) + " 00:00:00','rrrr-mm-dd hh24:mi:ss') " + "AND d.account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + paramInt1 + ") " + "AND d.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + paramInt3 + ") " + "GROUP BY d.user_id";
/*      */
/* 1691 */     localResultSet = paramConnector.executeQuery(str1);
/* 1692 */     while ((localResultSet != null) && (localResultSet.next())) {
/* 1693 */       localBalanceSheet = (BalanceSheet)localHashtable.get(localResultSet.getString("user_id"));
/* 1694 */       localBalanceSheet.setDate1Balance(localResultSet.getDouble("local"));
/*      */     }
/* 1696 */     paramConnector.closeCursor();
/* 1697 */     str1 = "SELECT d.user_id, SUM(transaction_type*quantity*(SELECT purchase_price FROM item@OMS_LINK WHERE id = d.item_id)) local FROM transaction_detail@OMS_LINK d,transaction@OMS_LINK t WHERE t.id = d.transaction_id  AND d.amount>0 AND t.transaction_date <= to_date('" + localSimpleDateFormat.format(paramDate2) + " 23:59:59','rrrr-mm-dd hh24:mi:ss') " + "AND d.account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + paramInt1 + ") " + "AND d.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + paramInt3 + ") " + "GROUP BY d.user_id";
/*      */
/* 1704 */     System.out.println("TEST SQL>" + str1);
/* 1705 */     localResultSet = paramConnector.executeQuery(str1);
/* 1706 */     while (localResultSet.next()) {
/* 1707 */       localBalanceSheet = (BalanceSheet)localHashtable.get(localResultSet.getString("user_id"));
/* 1708 */       localBalanceSheet.setDate2Balance(localResultSet.getDouble("local"));
/* 1709 */       localBalanceSheet.setChanges(localBalanceSheet.getDate2Balance() - localBalanceSheet.getDate1Balance());
/*      */     }
/* 1711 */     paramConnector.closeCursor();
/*      */
/* 1714 */     return new Vector(localHashtable.values());
/*      */   }
/*      */
/*      */   public Vector getBalanceSheets(Connector paramConnector, int paramInt1, Date paramDate1, Date paramDate2, int paramInt2, int paramInt3, boolean paramBoolean)
/*      */     throws MaestroException, SQLException
/*      */   {
/* 1728 */     int i = 1;
/* 1729 */     Vector localVector = new Vector();
/* 1730 */     BalanceSheet localBalanceSheet1 = null;
/* 1731 */     BalanceSheet localBalanceSheet2 = new BalanceSheet();
/* 1732 */     localBalanceSheet2.setId(paramInt1);
/* 1733 */     localBalanceSheet2.setLevel(paramInt2);
/* 1734 */     SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
/* 1735 */     String str = "SELECT * FROM fixed_accounts@OMS_LINK  WHERE acc_id = " + paramInt1 + " AND (SELECT count(*) FROM account_group@" + "OMS_LINK" + "  WHERE parent_id =" + paramInt1 + " )=0";
/*      */
/* 1737 */     ResultSet localResultSet = paramConnector.executeQuery(str);
/* 1738 */     if (localResultSet.next()) {
/* 1739 */       i = 0;
/*      */     }
/* 1741 */     paramConnector.closeCursor();
/* 1742 */     if (i == 0) {
/* 1743 */       return getUserBalanceSheet(paramConnector, paramInt1, paramDate1, paramDate2, paramInt2, paramInt3);
/*      */     }
/* 1745 */     paramConnector.closeCursor();
/* 1746 */     str = "SELECT name FROM account_group@OMS_LINK  WHERE id = " + paramInt1;
/* 1747 */     localResultSet = paramConnector.executeQuery(str);
/* 1748 */     if (localResultSet.next()) {
/* 1749 */       localBalanceSheet2.setName(localResultSet.getString("name"));
/*      */     }
/*      */
/* 1752 */     paramConnector.closeCursor();
/* 1753 */     if (paramBoolean)
/* 1754 */       localVector.add(localBalanceSheet2);
/* 1755 */     if (i != 0) {
/* 1756 */       str = "SELECT id,name FROM account_group@OMS_LINK ag WHERE parent_id = " + paramInt1 + " ORDER BY name";
/*      */
/* 1758 */       localResultSet = paramConnector.executeQuery(str);
/* 1759 */       while (localResultSet.next()) {
/* 1760 */         localBalanceSheet1 = new BalanceSheet();
/* 1761 */         localBalanceSheet1.setId(localResultSet.getInt("id"));
/* 1762 */         localBalanceSheet1.setName(localResultSet.getString("name"));
/*      */
/* 1764 */         localBalanceSheet1.setLevel(paramInt2 + 1);
/*      */
/* 1767 */         localVector.add(localBalanceSheet1);
/*      */       }
/*      */     }
/* 1770 */     paramConnector.closeCursor();
/* 1771 */     for (int j = 0; j < localVector.size(); j++) {
/* 1772 */       localBalanceSheet1 = (BalanceSheet)localVector.get(j);
/* 1773 */       str = "SELECT wm_concat(id) FROM (SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR parent_id = id START WITH id = " + localBalanceSheet1.getId() + " ORDER BY level DESC )";
/* 1774 */       localResultSet = paramConnector.executeQuery(str);
/* 1775 */       if (localResultSet.next()) {
/* 1776 */         localBalanceSheet1.setHierarchy(localResultSet.getString(1));
/*      */       }
/*      */
/* 1779 */       str = "SELECT SUM(transaction_type*quantity*(SELECT purchase_price FROM item@OMS_LINK WHERE id = d.item_id)) usd,  max(transaction_date) lt FROM transaction_detail@OMS_LINK d,transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount>0 AND d.account_id IN(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id = " + localBalanceSheet1.getId() + ") " + "AND d.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + paramInt3 + ")";
/*      */
/* 1785 */       localResultSet = paramConnector.executeQuery(str);
/* 1786 */       if (localResultSet.next()) {
/* 1787 */         localBalanceSheet1.setUSDBalance(localResultSet.getDouble("usd"));
/* 1788 */         localBalanceSheet1.setLocalBalance(localBalanceSheet1.getUSDBalance());
/* 1789 */         localBalanceSheet1.setCurrency("USD");
/*      */
/* 1791 */         if (localResultSet.getTimestamp("lt") != null) {
/* 1792 */           localBalanceSheet1.setLastTransaction(new Date(localResultSet.getTimestamp("lt").getTime()));
/*      */         } else {
/* 1794 */           localVector.remove(j);
/* 1795 */           j--;
/*      */         }
/*      */       }
/* 1798 */       paramConnector.closeCursor();
/*      */     }
/*      */		int j = 0;
/* 1801 */     for (j = 0; j < localVector.size(); j++) {
/* 1802 */       localBalanceSheet1 = (BalanceSheet)localVector.get(j);
/* 1803 */       str = "SELECT SUM(transaction_type*quantity*(SELECT purchase_price FROM item@OMS_LINK WHERE id = d.item_id)) local FROM transaction_detail@OMS_LINK d,transaction@OMS_LINK t WHERE t.id = d.transaction_id  AND d.amount>0 AND t.transaction_date < to_date('" + localSimpleDateFormat.format(paramDate1) + " 00:00:00','rrrr-mm-dd hh24:mi:ss') " + "AND d.account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + localBalanceSheet1.getId() + ") " + "AND d.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + paramInt3 + ")";
/*      */
/* 1809 */       localResultSet = paramConnector.executeQuery(str);
/* 1810 */       if (localResultSet.next()) {
/* 1811 */         localBalanceSheet1.setDate1Balance(localResultSet.getDouble("local"));
/*      */       }
/* 1813 */       paramConnector.closeCursor();
/*      */     }
/* 1815 */     for (j = 0; j < localVector.size(); j++) {
/* 1816 */       localBalanceSheet1 = (BalanceSheet)localVector.get(j);
/* 1817 */       str = "SELECT SUM(transaction_type*quantity*(SELECT purchase_price FROM item@OMS_LINK WHERE id = d.item_id)) local FROM transaction_detail@OMS_LINK d,transaction@OMS_LINK t WHERE t.id = d.transaction_id  AND d.amount>0 AND t.transaction_date <= to_date('" + localSimpleDateFormat.format(paramDate2) + " 23:59:59','rrrr-mm-dd hh24:mi:ss') " + "AND d.account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + localBalanceSheet1.getId() + ") " + "AND d.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + paramInt3 + ")";
/*      */
/* 1822 */       localResultSet = paramConnector.executeQuery(str);
/* 1823 */       if (localResultSet.next()) {
/* 1824 */         localBalanceSheet1.setDate2Balance(localResultSet.getDouble("local"));
/*      */       }
/* 1826 */       paramConnector.closeCursor();
/* 1827 */       localBalanceSheet1.setChanges(localBalanceSheet1.getDate2Balance() - localBalanceSheet1.getDate1Balance());
/*      */     }
/* 1829 */     return localVector;
/*      */   }
/*      */
/*      */   public double getBalanceSheets(Connector paramConnector, int paramInt1, Date paramDate, int paramInt2)
/*      */     throws MaestroException, SQLException
/*      */   {
/* 1842 */     double d = 0.0D;
/* 1843 */     SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
/*      */
/* 1845 */     String str = "SELECT SUM(transaction_type*quantity*(SELECT purchase_price FROM item@OMS_LINK WHERE id = d.item_id)) balance FROM transaction_detail@OMS_LINK d,transaction@OMS_LINK t WHERE t.id = d.transaction_id  AND d.amount>0 AND t.transaction_date <= to_date('" + localSimpleDateFormat.format(paramDate) + " 23:59:59','rrrr-mm-dd hh24:mi:ss') " + "AND d.account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + paramInt1 + ") " + "AND d.area_id IN(SELECT id FROM area_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + paramInt2 + ") ";
/*      */
/* 1850 */     ResultSet localResultSet = paramConnector.executeQuery(str);
/* 1851 */     if (localResultSet.next())
/* 1852 */       d = localResultSet.getDouble("balance");
/* 1853 */     paramConnector.closeCursor();
/* 1854 */     return d;
/*      */   }
/*      */ }

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.dao.FinanceDAO
 * JD-Core Version:    0.6.0
 */