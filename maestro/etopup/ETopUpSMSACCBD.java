package com.maestro.etopup.util;

import com.maestro.crb.orig_billing.ConnectDB;
import com.maestro.crb.orig_billing.info.Inf;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.net.*;
import com.maestro.util.URLReader;
import javax.servlet.http.*;
import java.util.*;
import java.text.*;
import java.util.StringTokenizer;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import java.net.URL;
import com.maestro.util.URLReader;


public class ETopUpSMSACCBD implements Job
{
	public void execute(JobExecutionContext paramJobExecutionContext) throws JobExecutionException
	{
		eTop();
	}

	public void eTop()
	{
		//Oracle Variables
		ConnectDB con = null;
		java.sql.Statement stmt = null;
		ResultSet rs = null;
		String qry = "";

		String modem_id = "71";
		String gateWayModem_id = "12";
		// List
		String[] aVal = null;
		ArrayList aList = new ArrayList();
		DecimalFormat twoDigit = new DecimalFormat("#,##,##0.00");
		// SMS Variables
		String delims = ",:.@&$#*^><!?_-/+;`'~[]{}() ";
		int count=0;
		String id = "";
		String ids = "";
		String mobile = "";
		String msg = "";
		String destPhnNo = "";
		String keyVal = "";
		String key = "";
		int val = -1;
		String amount = "";
		String Ramt = "";
		String prepostVal="";
		String prepost ="0";
		String serviceCharge = "";

		String prefix = "";
		String orig_prefix = "";
		String theType = "";
		String tpPackId = "";
		String packName = "";
		String country_id = "";
		String operator_id ="";

		String country_name ="";
		String countryCode ="";
		String currencyName ="";

		// Check & Other Variables
		boolean meFound = false;
		String me_id="";
		String mePhoneNo = "";
		boolean keyFound = false;
		boolean ignoreFound = false;
		double minDiff = 0.0;
		double timeDiff = 0.0;
		boolean operatorRate = false;
		String airTimeRateplan_id = "872818";
		String qryCond ="";
		String seriesId ="";
		int theCount=0;
		String localAmount = "";
		String amountToRecharge="";
		String variableAmt = "";
		String smsComm = "0";
		String maxPackId = "872816";
		String ucUserId = "9";
		String bKashSeriesId = "872820";
		String CC_NUMBER = "8801850999235";

		String pack_prefix = "";
		String firstName = "";
		String lastName = "";
		String[] temp = null ;
		String rpId = "";
		String succ ="";
		String supplier_id="-1";
		String pinNum="";
		String phone_no= "";
		String flx_id="";
		String areaName="";
		String p_area_id = "2";
		int ret = -2;
		String theRet = "";

		String tmpDestPhone="";
		String txnid = "";
		String remarks="";
		String tmpStr="";
		String theSupp="";
		String urlStr="";
		int theCountt=0;
		int serviceCount=0;

		String delims1 = "|";
		String retCode ="";
		String retText="";
		String supBal="";

		String serial = "";
		String pin = "";
		String instruction = "";
		String theResponse = "";
		double recvAmount = 0.0;
		String recvCurrency ="";
		double crLimit=0.0;
		String telcoName ="";
		String ttype = "";
		String startDate = "";

		try
		{
			con = new ConnectDB();
			stmt = con.connect();

			/********* Search Shop SMS ******************/
			qry = "Select count(*) from sms_inbox@skydb_muci_link where modem_id = "+modem_id+" and status = 'N'";
			System.out.println(qry);
			rs = stmt.executeQuery(qry);
			while(rs.next())
			{
				count = rs.getInt(1);
			}

			/************* SMS FOUND ***********/
			if(count > 0)
			{
				/************* Grab SMS ****************/
				qry ="Select id,mobile,UPPER(REPLACE(REPLACE(msg, CHR(13), ' '), CHR(10), ' ')) from sms_inbox@skydb_muci_link where modem_id = "+modem_id+" and status = 'N'";
				System.out.println(qry);
				rs = stmt.executeQuery(qry);
				while(rs.next())
				{
					id = rs.getString(1);
					mobile = rs.getString(2);
					msg = rs.getString(3);
					if(msg==null) msg = " ";
					aVal = new String[]{id,mobile,msg};
					aList.add(aVal);
				}

				/************* Change Status of Grabbed SMS ****************/
				qry ="Update sms_inbox@skydb_muci_link set status = 'P' where modem_id = "+modem_id+" and status = 'N'";
				System.out.println(qry);
				stmt.executeUpdate(qry);

				/*************** Iterate Grabbed SMS List ***************/
				if (aList!= null && aList.size() >0)
				{

					for(int h=0;h<aList.size();h++)
					{
						/******* Initialize Loop Variables ************/
						meFound = false;
						keyFound = false;
						ignoreFound = false;
						destPhnNo = "";
						msg = "";
						keyVal = "";
						key = "";
						val = -1;
						prepostVal = "";
						prepost = "0";
						operator_id = "-1";

						prefix = "";
						orig_prefix	= "";
						theType = "";
						tpPackId = "-1";
						packName = "";
						country_id = "-1";
						operator_id ="-1";
						amount = "";
						Ramt = "";
						theCount=0;
						localAmount = "";
						amountToRecharge="";
						variableAmt = "";
						smsComm = "0";
						pack_prefix = "";
						firstName = "";
						lastName = "";
						temp = null ;
						rpId = "";
						supplier_id="-1";
						pinNum="";
						phone_no= "";
 						flx_id="";
 						areaName="";
 						ret = -2;
 						theRet = "";
						tmpDestPhone="";
						txnid = "";
						remarks="";
						tmpStr="";
						theSupp="";
						urlStr="";
						theCountt=0;
						serviceCount=0;

						delims1 = "|";
						retCode ="";
						retText="";
						supBal="";
						me_id="";
						mePhoneNo = "";

						serial = "";
						pin = "";
						instruction = "";
						theResponse = "";
						recvAmount = 0.0;
						recvCurrency ="";
						crLimit=0.0;
						telcoName ="";
						serviceCharge = "";
						qryCond = "";
						ttype = "";

						/************* Pick Value And Tokenize ************/
						aVal = (String[])aList.get(h);
						StringTokenizer st = new StringTokenizer(aVal[2], delims);

						if(st.hasMoreElements())
						{
							destPhnNo = (String)st.nextElement();
							if(destPhnNo!=null) destPhnNo = destPhnNo.trim();
						}
						if(st.hasMoreElements())
						{
							keyVal = (String)st.nextElement();
							if(keyVal != null) keyVal = keyVal.trim();
						}
						if(st.hasMoreElements())
						{
							prepostVal = (String)st.nextElement();
							if(prepostVal != null) prepostVal = prepostVal.trim();
							if(prepostVal != null && (prepostVal.equals("p") || prepostVal.equals("P")))
							{
								prepost = "1";
							}else
							{
								prepost = "0";
							}
						}



						/************ Set Destination Number & Processing Status *****************/
						qry ="Update sms_inbox@skydb_muci_link set destination = '"+destPhnNo+"',txn_processed='P' where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
						System.out.println(qry);
						stmt.executeUpdate(qry);



						/********* Ignore From & To Mobile *************/
						qry = "Select * from flexi_ignore@skydb_muci_link where from_number = '"+aVal[1]+"'";
						System.out.println(qry);
						rs = stmt.executeQuery(qry);
						while(rs.next())
						{
							ignoreFound = true;
						}

						qry = "Select * from flexi_ignore@skydb_muci_link where to_number = '"+destPhnNo+"'";
						System.out.println(qry);
						rs = stmt.executeQuery(qry);
						while(rs.next())
						{
							ignoreFound = true;
						}

						if(ignoreFound == true)
						{
							qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							msg = "AAARECHARGE:Sorry Invalid destination number, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
							qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id,sent_result,sent_time) values "+
								  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+",1,sysdate)";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							continue;
						}


						/************* Authenticate Shop ********************/
						if(aVal[1] != null && aVal[1].length()>0)
						{
							qry = "select me_id from emp where mobile='"+aVal[1]+"'";
							System.out.println(qry);
							rs = stmt.executeQuery(qry);
							while (rs.next())
							{
								meFound = true;
								me_id = rs.getString(1);
							}
						}

						if(!meFound)
						{
							qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							msg = "AAARECHARGE:Athentication Failed, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
							qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
								  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
							System.out.println(qry);
							//stmt.executeUpdate(qry);

							continue;

						}else
						{
							qry ="Update sms_inbox@skydb_muci_link set me_id = "+me_id+" where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
							System.out.println(qry);
							stmt.executeUpdate(qry);

						}

						/*********************** Shop Balance *********************************/
						qry = "Select NVL((Select (-1)*sum(quantity*transaction_type) from transaction_detail@oms_link where fixed_acc = (Select fixed_acc_id from emp e1 where e1.me_id = "+me_id+")),0) from dual";
						System.out.println(qry);
						rs = stmt.executeQuery(qry);
						while (rs.next())
						{
							recvAmount = rs.getDouble(1);
						}

						qry = "Select name from item@oms_link where id = (Select currency_id from oms_users@oms_link where id = (Select oms_user_id from emp where me_id = "+me_id+"))";
						rs = stmt.executeQuery(qry);
						while (rs.next())
						{
							recvCurrency = rs.getString(1);
						}

						qry = "Select NVL(limit,0) from credit_limit where me_id = "+me_id+"";
						rs = stmt.executeQuery(qry);
						while (rs.next())
						{
							crLimit = rs.getDouble(1);
						}


						/*************** Handle Keyword Bal *****************************************/
						if(destPhnNo != null && destPhnNo.equals("BAL"))
						{
								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								msg = "AAARECHARGE:Your Balance is "+twoDigit.format(recvAmount)+" "+recvCurrency+". Today Sales:";
								qry ="Select to_char(sysdate+13/24,'rrrr-mm-dd') from dual";
								System.out.println(qry);
								rs= stmt.executeQuery(qry);
								while(rs.next())
								{
									startDate = rs.getString(1);
								}

								qry = " Select shop,REPLACE(DECODE(telco,'Bikash','bKash',DECODE(telco,'Bangladesh','BD FLEXI',"+" DECODE(telco,'India','IND FLEXI',telco))),'SG ','') Telco,telco_country,amt from (    Select "+" (Select name from emp e where e.me_id = t.me_id) shop,    (Select series from cardseries@skydb_muci_link "+" where id = series_id) telco,(Select flexi_country_id from cardseries@skydb_muci_link where id = series_id) "+"  telco_country,sum(amount) amt from topup_request t where topup_request_time between to_date('"+startDate+
								" 00:00:00','rrrr-mm-dd hh24:mi:ss')-13/24 and to_date('"+startDate+" 23:59:59','rrrr-mm-dd hh24:mi:ss') -13/24"+
								"  and me_id ="+me_id+" group by me_id,series_id order by shop,telco,sum(amount))";
								System.out.println(qry);
								rs= stmt.executeQuery(qry);
								while(rs.next())
								{
									msg +=" "+rs.getString(2)+":";
									if(rs.getString(3)!=null && rs.getString(3).equals("6"))
									{
										msg+=" Tk";
									}else if(rs.getString(3)!=null && rs.getString(3).equals("8"))
									{
										msg+=" RS";
									}else if(rs.getString(3)!=null && rs.getString(3).equals("13"))
									{
										msg+=" $";
									}
									msg += rs.getString(4);

								}
								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
								System.out.println(qry);
								stmt.executeUpdate(qry);



								continue;


						}

						/*********** Destination Number Check ***************/
						if(destPhnNo == null)
						{
							destPhnNo = "";
							if(destPhnNo != null && destPhnNo.length()<8)
							{
								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								msg = "AAARECHARGE:Sorry Invalid destination number, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								continue;
							}
						}



						/************* Find KeyWord **************/
/*
						qry = " Select prefix,type,NVL(tp_pack_id,-1),NVL(country_id,-1),NVL(operator_id,-1) from sms_prefix where id in (Select id from (select max(length(prefix)) as aa,id from sms_prefix where instr(upper('"+keyVal+"'), prefix) = 1 group by id order by aa desc) where rownum <2)";
						System.out.println(qry);
						rs = stmt.executeQuery(qry);
						while(rs.next())
						{
							keyFound = true;
							prefix = rs.getString(1);
							theType = rs.getString(2);
							tpPackId = rs.getString(3);
							country_id = rs.getString(4);
							operator_id = rs.getString(5);
						}
*/
						country_id = "6";
						if( keyVal != null &&  keyVal.length()>0)
						{
							keyFound = true;
							if(keyVal.indexOf("B")==0)
							{
								prefix = "BK";
								theType = "TT";
								orig_prefix="B";
							}else if(keyVal.indexOf("A")==0)
							{
								prefix = "AK";
								theType = "TT";
								orig_prefix="A";
							}else
							{
								theType = "FLX";
								orig_prefix="";
							}
						}
						if(keyFound)
						{
							try{
								amount = keyVal.substring(orig_prefix.length());
								System.out.println("prefix::"+orig_prefix+":amount:"+amount);
								val = Integer.parseInt(amount);
							}catch(Exception localException)
							{
								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								msg = "AAARECHARGE:Invalid Keword/Value, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
								System.out.println(qry);
								stmt.executeUpdate(qry);
								continue;
							}
						}else
						{
								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								msg = "AAARECHARGE:Invalid Keword/Value, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
								System.out.println(qry);
								stmt.executeUpdate(qry);
								continue;
						}


						if(theType != null && theType.equals("TT")) // BIKASH
						{

							seriesId = ""+bKashSeriesId;
							operatorRate = false;
							qryCond += " And seriesid="+seriesId+" ";

							/***********  Country Name, Currency, Code Resolve ************/
							qry = "Select name,country_code,(Select remarks from item@oms_link where id = currency_id) from flexi_country@skydb_muci_link where id = "+country_id+"";
							System.out.println(qry);
							rs = stmt.executeQuery(qry);

							while(rs.next())
							{
								country_name = rs.getString(1);
								countryCode = rs.getString(2);
								currencyName = rs.getString(3);
							}


							/*********** Destination Phone Check **************/


							if(countryCode != null && countryCode.equals("880"))
							{
								if(destPhnNo!=null && destPhnNo.indexOf("+") >=0)
								{
									destPhnNo = destPhnNo.substring(1);
								}
								if(destPhnNo!=null && destPhnNo.length() == 11)
								{
									destPhnNo = "88"+destPhnNo;
								}
								if(destPhnNo!=null && destPhnNo.length() != 13)
								{
									System.out.println("destPhnNo:"+destPhnNo+":destPhnNo.length():"+destPhnNo.length());
									msg="AAARECHARGE:Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
								}

								System.out.println(destPhnNo+":::"+destPhnNo.substring(destPhnNo.indexOf("880")+3,destPhnNo.length()));
							}


							/************* Check RateChart *****************/
							if(amount != null && amount.length()>0)
							{

								qry = "Select count(*) from sms_ratechart@skydb_muci_link where purchase_denomination = "+amount+" and (min_value is null OR min_value =0) and (max_value is null OR max_value = 0) and country_id="+country_id+" "+qryCond+"";
								System.out.println(qry);
								rs = stmt.executeQuery(qry);
								while(rs.next())
								{
									theCount = rs.getInt(1);
								}
								if(theCount > 1)
								{
									msg="AAARECHARGE:Sorry Invalid Rate Configuration. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry Invalid Rate !! for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", "+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
								}
								if(theCount == 1)
								{
									qry = "Select sale_denomination from sms_ratechart@skydb_muci_link where purchase_denomination = "+amount+" and (min_value is null OR min_value =0) and (max_value is null OR max_value = 0) and country_id="+country_id+" "+qryCond+"";

									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while(rs.next())
									{
										Ramt = rs.getString(1);
									}

									qry = "Select count(*) from sms_ratechart@skydb_muci_link where (min_value is not null and min_value >0) and (max_value is not null and max_value >0) and   "+amount+" between min_value and max_value and country_id="+country_id+" "+qryCond+"";
									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while(rs.next())
									{
										theCount = rs.getInt(1);
									}
									if(theCount ==0)
									{
										amount="";
									}
									//amount="";
									theCount=0;
								}
							}

							if(amount != null && amount.length()>0)
							{
								qry = "Select count(*) from sms_ratechart@skydb_muci_link where (min_value is not null and min_value >0) and (max_value is not null and max_value >0) and   "+amount+" between min_value and max_value and country_id="+country_id+" "+qryCond+"";
								System.out.println(qry);
								rs = stmt.executeQuery(qry);
								while(rs.next())
								{
									theCount = rs.getInt(1);
								}

								if(theCount == 0 || theCount > 1)
								{
									msg="AAARECHARGE:Sorry Invalid Amount To Recharge. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry Amount To Recharge !! for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", "+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
								}
								if(theCount == 1)
								{
									qry = "Select seriesid, "+amount+"*sale_denomination/purchase_denomination,NVL(service_charge/100,0)*("+amount+"*sale_denomination/purchase_denomination)  from sms_ratechart@skydb_muci_link where (min_value is not null and min_value >0) and (max_value is not null and max_value >0) and   "+amount+" between min_value and max_value and country_id="+country_id+" "+qryCond+"";
									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while(rs.next())
									{
										//seriesId = rs.getString(1);
										localAmount = rs.getString(2);
										serviceCharge = rs.getString(3);
									}
									amountToRecharge = amount;
									variableAmt = "Y";
								}


							}else if(Ramt != null && Ramt.length()>0)
							{
								qry = "Select count(*) from sms_ratechart@skydb_muci_link where country_id="+country_id+" "+qryCond+" and sale_denomination="+Ramt+"";
								System.out.println(qry);
								rs = stmt.executeQuery(qry);
								while(rs.next())
								{
									theCount = rs.getInt(1);
								}
								if(theCount == 0 || theCount > 1)
								{
									msg="AAARECHARGE:Sorry Invalid Amount To Recharge. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry Amount To Recharge !! for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", "+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
								}
								if(theCount == 1)
								{
									qry = "Select seriesid,purchase_denomination,NVL(service_charge/100,0)*sale_denomination from sms_ratechart@skydb_muci_link where country_id="+country_id+" "+qryCond+" and sale_denomination="+Ramt;
									if(operatorRate)
									{
										qry += " and operator_id="+operator_id;
									}else
									{
										qry += " and operator_id is null";
									}

									System.out.println(qry);
									rs = stmt.executeQuery(qry);

									while(rs.next())
									{
										//seriesId = rs.getString(1);
										amountToRecharge = rs.getString(2);
										serviceCharge = rs.getString(3);

									}
									localAmount = Ramt;
									variableAmt = "N";
								}
							}
							if(variableAmt != null && variableAmt.length()<1)
							{
									msg="AAARECHARGE:orry Invalid Amount To Recharge. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry Amount To Recharge !! for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", "+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
							}



							if(seriesId != null && seriesId.length()>0)
							{

								qry = "Select NVL((Select NVL(commission,'0') from acc_item_mapping where series_id="+seriesId+" and me_id="+me_id+"),0) from dual";
								System.out.println(qry);
								rs = stmt.executeQuery(qry);

								smsComm = "0";
								while(rs.next())
								{
									smsComm = rs.getString(1);
								}

							}


							if(prefix!=null && prefix.equals("BK"))
							{
								serviceCharge = "0";  // For BD
								ttype = "0";
							}else if(prefix!=null && prefix.equals("AK"))
							{
								ttype = "1";
							}



							/********************* Credit Limit Check **************************/
							System.out.println("::"+recvAmount*(-1)+Double.parseDouble(localAmount)*(1-Double.parseDouble(smsComm)/100)+"::"+crLimit);
							if(recvAmount*(-1)+Double.parseDouble(localAmount)*(1-Double.parseDouble(smsComm)/100)+ Double.parseDouble(serviceCharge)> crLimit)
							{
									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									msg = "AAARECHARGE:Sorry, You do not have enough balance. Your balance:"+twoDigit.format(recvAmount)+". For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
							}


							/******************* Shop Accounting Here ************************/
							qry = " SELECt pack_prefix,maxim_pack_id,(select nvl(first_name,' ') || '<SP>' || nvl(last_name,' ') from oms_users@oms_link where id = " +
								  " ( select oms_user_id from emp where me_id="+me_id+")  " +
								  " ) uname,(select mobile from emp where me_id="+me_id+") mobile  FROM package_config "+
								  " WHERE maxim_pack_id = ( " +
								  " Select distinct rp.id from rate_plan_m@skydb_muci_link rp, cardseries cs  " +
								  "  where rp.id = cs.RATE_PLAN_ID  " +
								  " and rp.IS_SPEEDDIAL='y' and rp.id="+maxPackId+" and rownum<2 " +
								  " ) AND user_id = "+ucUserId;

							System.out.println(qry);
							rs = stmt.executeQuery(qry);
							if(rs.next())
							{
								rpId = rs.getString("maxim_pack_id");
								pack_prefix = rs.getString("pack_prefix");
								mePhoneNo = rs.getString("mobile");
								if(rs.getString("uname")!=null)
								{
									System.out.println("Getting uname ................");
									temp = rs.getString("uname").split("<SP>");
									System.out.println("Got uname XXXXXX");
								}
								if(temp!=null){
									firstName = temp[0] ;
									lastName = temp[1] ;
								}
								if(firstName.equals(" ") || firstName.equals(""))
									firstName = mePhoneNo;
								if(lastName.equals(" ") || lastName.equals(""))
									lastName = mePhoneNo;
							}


							OracleCallableStatement ps = (OracleCallableStatement) stmt.getConnection().prepareCall( "BEGIN PROC_TRANSFER(?,?,?,?,?,?,?,?,?,?,?,?,?); END;" );
							ps.setString(1,ucUserId);
							ps.setString(2,pack_prefix);
							ps.setString(3,null);
							ps.setInt(4,Integer.parseInt(me_id));
							ps.setString(5,mePhoneNo);
							ps.setDouble(6,Double.parseDouble(localAmount));
							ps.registerOutParameter(7,OracleTypes.VARCHAR );
							ps.setString(8,firstName);
							ps.setString(9,lastName);
							ps.setDouble(10,Double.parseDouble(smsComm));
							ps.setString(11,destPhnNo);
							ps.setDouble(12,Double.parseDouble(serviceCharge));
							ps.setString(13,seriesId);

							ps.execute();
							succ = ps.getString(7);

							System.out.println("succ"+succ+":ucUserId:"+ucUserId+":pack_prefix:"+pack_prefix+":me_id:"+me_id+":localAmount:"+localAmount+":smsComm:"+smsComm+":destPhnNo:"+destPhnNo+":serviceCharge:"+serviceCharge+":seriesId:"+seriesId);
							if(succ.indexOf("Successful")<0)
							{

								//throw new Exception("<font color=\"RED\" size=4>Error: "+succ+"</font>" );
								msg="AAARECHARGE:Sorry Recharge Failed. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								System.out.print("Recharge Failed From Procedure !! for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", "+modem_id+")";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);
								continue;

							}

							qry = " select USERNAME pinnumber,(select MOBILE from emp where me_id="+me_id+" and uc_user_id="+ucUserId +") phoeNo from SUBSCRIBERS@skydb_muci_link "+
								" where id = (select SB_ID from REGISTERED_PHONES@skydb_muci_link where rateplan_id = "+rpId+" and" +
							" PHONE_NO=(select MOBILE from emp where me_id= " + me_id + "  and uc_user_id="+ucUserId +" ))  ";
							rs = stmt.executeQuery(qry);

							if(rs.next())
							{
								pinNum = rs.getString("pinnumber");
								phone_no = rs.getString("phoeNo");
							}

							qry = "select flx_id.nextval from dual";
							System.out.println(qry);
							rs= stmt.executeQuery(qry);
							if(rs.next()) flx_id = rs.getString(1);


							qry = "select name from area_group@oms_link where id="+p_area_id;

							System.out.println(qry);
							rs= stmt.executeQuery(qry);
							if(rs.next())
							{
								areaName = rs.getString(1)+"-BIKASH-"+flx_id;
							}

							if(destPhnNo!=null && destPhnNo.indexOf("880") ==0)
							{
								tmpDestPhone= destPhnNo.substring(2);
							}else
							{
								tmpDestPhone = destPhnNo;
							}

							ps = (OracleCallableStatement) stmt.getConnection().prepareCall( "BEGIN PROC_TRANSFER@skydb_muci_link(?,?,?,?,?,?,?,?,?); END;" );
							ps.setString(1,pinNum);
							ps.setString(2,phone_no);
							ps.setString(3,tmpDestPhone);
							ps.setDouble(4,Double.parseDouble(localAmount));
							ps.setString(5,areaName);
							ps.registerOutParameter(6,OracleTypes.VARCHAR );
							ps.setDouble(7,Double.parseDouble(amountToRecharge));
							ps.setString(8,countryCode);
							ps.setString(9,country_name);

							ps.execute();
							ret = ps.getInt(6);
							String add_m = "0.02";
							/*************** FOR BD bKash *****************/

							add_m = "0";

						if(ret == 0)
						{
								qry = "Update topup_request set me_id ="+me_id+", add_amount=amount*"+add_m+",topup_type="+ttype+",shop_comm="+smsComm+",series_id="+seriesId+", inbox_id= "+aVal[0]+" where msgid='"+areaName+"'";
								System.out.println(qry);
								stmt.executeUpdate(qry);



						}



						}else if(theType != null && theType.equals("FLX"))// Flexi Block
						{

							/*********** Resolve Flexi Series *******************/
							qry = "Select seriesid from sms_ratechart@skydb_muci_link where country_id = "+country_id+" and seriesid in ( Select id from cardseries@skydb_muci_link  where rate_plan_id = "+airTimeRateplan_id+" and is_default='Y' and flexi_country_id = "+country_id+")";
							rs = stmt.executeQuery(qry);
							while (rs.next())
							{
								seriesId = rs.getString(1);
							}


							if(operator_id != null && operator_id.equals("-1"))
							{
								operatorRate = false;
							}else
							{
								operatorRate = true;
								qryCond += " and operator_id = "+operator_id+"";

								qry = "Select seriesid from sms_ratechart@skydb_muci_link where country_id = "+country_id+" and operator_id="+operator_id+" and seriesid in ( Select id from cardseries@skydb_muci_link  where rate_plan_id = "+airTimeRateplan_id+" and is_default='Y' and flexi_country_id = "+country_id+")";
								rs = stmt.executeQuery(qry);
								while (rs.next())
								{
									seriesId = rs.getString(1);
								}

							}

							qryCond = " And rateplan_id="+airTimeRateplan_id+" ";
							qryCond += " And seriesid="+seriesId+" ";

							/***********  Country Name, Currency, Code Resolve ************/
							qry = "Select name,country_code,(Select remarks from item@oms_link where id = currency_id) from flexi_country@skydb_muci_link where id = "+country_id+"";
							System.out.println(qry);
							rs = stmt.executeQuery(qry);

							while(rs.next())
							{
								country_name = rs.getString(1);
								countryCode = rs.getString(2);
								currencyName = rs.getString(3);
							}


							/*********** Destination Phone Check **************/


							if(countryCode != null && countryCode.equals("880"))
							{
								if(destPhnNo!=null && destPhnNo.indexOf("+") >=0)
								{
									destPhnNo = destPhnNo.substring(1);
								}
								if(destPhnNo!=null && destPhnNo.length() == 11)
								{
									destPhnNo = "88"+destPhnNo;
								}
								if(destPhnNo!=null && destPhnNo.length() != 13)
								{
									System.out.println("destPhnNo:"+destPhnNo+":destPhnNo.length():"+destPhnNo.length());
									msg="AAARECHARGE:Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
								}

								System.out.println(destPhnNo+":::"+destPhnNo.substring(destPhnNo.indexOf("880")+3,destPhnNo.length()));
								System.out.println("::"+destPhnNo.substring(3));

								qry = "Select id from flexi_operator@skydb_muci_link where instr('"+destPhnNo.substring(3)+"',code) =1 ";
								System.out.println(qry);
								rs = stmt.executeQuery(qry);
								while (rs.next())
								{
									operator_id = rs.getString(1);
									//operatorRate = true;
								}
							}else if(countryCode != null && countryCode.equals("91"))
							{
								operator_id = "-1";
								//919715595985
								if(destPhnNo!=null && destPhnNo.length() == 10)
								{
									destPhnNo = "91"+destPhnNo;
									qry ="Update sms_inbox@skydb_muci_link set destination = '"+destPhnNo+"',txn_processed='P' where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
								}

								if(destPhnNo!=null && destPhnNo.length() != 12)
								{
									msg="AAARECHARGE:Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", "+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
								}
							}else if(countryCode != null && countryCode.equals("65"))
							{
								if(destPhnNo!=null && destPhnNo.indexOf("+") >=0)
								{
									destPhnNo = destPhnNo.substring(1);
								}

								if(destPhnNo!=null && destPhnNo.length() == 10)
								{
									destPhnNo = destPhnNo.substring(2);
								}
								if(destPhnNo!=null && destPhnNo.length() == 8)
								{
									qry ="Update sms_inbox@skydb_muci_link set destination = '"+destPhnNo+"',txn_processed='P' where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
								}

								if(destPhnNo!=null && destPhnNo.length() != 8)
								{
									msg="AAARECHARGE:Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", "+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
								}
							}



							/************ Check Minimum TimeDiff *************************/

							qry = "Select NVL(time_diff,0) from flexi_country@skydb_muci_link where id ="+country_id+" ";
							rs = stmt.executeQuery(qry);
							while(rs.next())
							{
								minDiff = rs.getDouble(1);
							}

							qry = " Select NVL((Select (sysdate - tr )*24*60 from( Select tr from ( Select topup_request_time tr from topup_request where dst_no='"+destPhnNo+"' and processed = 'Y' order by id desc) where rownum <2)),9999999) a, "+
							" NVL((Select (sysdate - tp )*24*60 from( Select tp from ( Select topup_process_time tp from topup_request where dst_no='"+destPhnNo+"'  and processed = 'Y' order by id desc) where rownum <2)),9999999) b from dual";
							System.out.println(qry);
							rs = stmt.executeQuery(qry);
							while(rs.next())
							{
								timeDiff = minDiff - Math.min(rs.getDouble(1),rs.getDouble(2));
							}

							if(timeDiff > 0)
							{
								timeDiff = Math.ceil(timeDiff);
								msg = "AAARECHARGE:This request can not be processed now. Please try after "+timeDiff+" minute";
								System.out.print("<center><font color=red size=4><b>This request can not be processed now. Please try after "+timeDiff+" minute</b></font>");

								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);
								continue;
							}
							/************* Check RateChart *****************/
							if(amount != null && amount.length()>0)
							{

								qry = "Select count(*) from sms_ratechart@skydb_muci_link where purchase_denomination = "+amount+" and (min_value is null OR min_value =0) and (max_value is null OR max_value = 0) and country_id="+country_id+" "+qryCond+"";
								System.out.println(qry);
								rs = stmt.executeQuery(qry);
								while(rs.next())
								{
									theCount = rs.getInt(1);
								}
								if(theCount > 1)
								{
									msg="AAARECHARGE:Sorry Invalid Rate Configuration. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry Invalid Rate !! for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", "+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
								}
								if(theCount == 1)
								{
									qry = "Select sale_denomination from sms_ratechart@skydb_muci_link where purchase_denomination = "+amount+" and (min_value is null OR min_value =0) and (max_value is null OR max_value = 0) and country_id="+country_id+" "+qryCond+"";

									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while(rs.next())
									{
										Ramt = rs.getString(1);
									}

									qry = "Select count(*) from sms_ratechart@skydb_muci_link where (min_value is not null and min_value >0) and (max_value is not null and max_value >0) and   "+amount+" between min_value and max_value and country_id="+country_id+" "+qryCond+"";
									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while(rs.next())
									{
										theCount = rs.getInt(1);
									}
									if(theCount ==0)
									{
										amount="";
									}
									//amount="";
									theCount=0;
								}
							}

							if(amount != null && amount.length()>0)
							{
								qry = "Select count(*) from sms_ratechart@skydb_muci_link where (min_value is not null and min_value >0) and (max_value is not null and max_value >0) and   "+amount+" between min_value and max_value and country_id="+country_id+" "+qryCond+"";
								System.out.println(qry);
								rs = stmt.executeQuery(qry);
								while(rs.next())
								{
									theCount = rs.getInt(1);
								}

								if(theCount == 0 || theCount > 1)
								{
									msg="AAARECHARGE:Sorry Invalid Amount To Recharge. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry Amount To Recharge !! for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", "+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
								}
								if(theCount == 1)
								{
									qry = "Select seriesid, "+amount+"*sale_denomination/purchase_denomination  from sms_ratechart@skydb_muci_link where (min_value is not null and min_value >0) and (max_value is not null and max_value >0) and   "+amount+" between min_value and max_value and country_id="+country_id+" "+qryCond+"";
									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while(rs.next())
									{
										seriesId = rs.getString(1);
										localAmount = rs.getString(2);
									}
									amountToRecharge = amount;
									variableAmt = "Y";
								}


							}else if(Ramt != null && Ramt.length()>0)
							{
								qry = "Select count(*) from sms_ratechart@skydb_muci_link where country_id="+country_id+" "+qryCond+" and sale_denomination="+Ramt+"";
								System.out.println(qry);
								rs = stmt.executeQuery(qry);
								while(rs.next())
								{
									theCount = rs.getInt(1);
								}
								if(theCount == 0 || theCount > 1)
								{
									msg="AAARECHARGE:Sorry Invalid Amount To Recharge. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry Amount To Recharge !! for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", "+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
								}
								if(theCount == 1)
								{
									qry = "Select seriesid,purchase_denomination from sms_ratechart@skydb_muci_link where country_id="+country_id+" "+qryCond+" and sale_denomination="+Ramt;
									if(operatorRate)
									{
										qry += " and operator_id="+operator_id;
									}else
									{
										qry += " and operator_id is null";
									}

									System.out.println(qry);
									rs = stmt.executeQuery(qry);

									while(rs.next())
									{
										seriesId = rs.getString(1);
										amountToRecharge = rs.getString(2);
									}
									localAmount = Ramt;
									variableAmt = "N";
								}
							}
							if(variableAmt != null && variableAmt.length()<1)
							{
									msg="AAARECHARGE:orry Invalid Amount To Recharge. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry Amount To Recharge !! for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", "+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
							}



							if(seriesId != null && seriesId.length()>0)
							{

								//qry = "Select NVL((Select NVL(commission,'0') from acc_item_mapping where series_id="+seriesId+" and me_id="+me_id+"),0) from dual";
								qry = "Select NVL((select commission from series_value_shop_comm where series_id="+seriesId+" and me_id="+me_id+" and "+amountToRecharge+" between min_value and max_value),(select NVL(commission,0) from series_value_comm where series_id="+seriesId+" and "+amountToRecharge+" between min_value and max_value)) from dual";
								System.out.println(qry);
								rs = stmt.executeQuery(qry);

								smsComm = "0";
								while(rs.next())
								{
									smsComm = rs.getString(1);
								}

							}



							/********************* Credit Limit Check **************************/
							System.out.println("::"+recvAmount*(-1)+Double.parseDouble(localAmount)*(1-Double.parseDouble(smsComm)/100)+"::"+crLimit);
							if(recvAmount*(-1)+Double.parseDouble(localAmount)*(1-Double.parseDouble(smsComm)/100) > crLimit)
							{
									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									msg = "AAARECHARGE:Sorry, You do not have enough balance. Your balance:"+twoDigit.format(recvAmount)+". For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
							}



							/******************* Shop Accounting Here ************************/
							qry = " SELECt pack_prefix,maxim_pack_id,(select nvl(first_name,' ') || '<SP>' || nvl(last_name,' ') from oms_users@oms_link where id = " +
								  " ( select oms_user_id from emp where me_id="+me_id+")  " +
								  " ) uname,(select mobile from emp where me_id="+me_id+") mobile  FROM package_config "+
								  " WHERE maxim_pack_id = ( " +
								  " Select distinct rp.id from rate_plan_m@skydb_muci_link rp, cardseries cs  " +
								  "  where rp.id = cs.RATE_PLAN_ID  " +
								  " and rp.IS_SPEEDDIAL='y' and rp.id="+maxPackId+" and rownum<2 " +
								  " ) AND user_id = "+ucUserId;

							System.out.println(qry);
							rs = stmt.executeQuery(qry);
							if(rs.next())
							{
								rpId = rs.getString("maxim_pack_id");
								pack_prefix = rs.getString("pack_prefix");
								mePhoneNo = rs.getString("mobile");
								if(rs.getString("uname")!=null)
								{
									System.out.println("Getting uname ................");
									temp = rs.getString("uname").split("<SP>");
									System.out.println("Got uname XXXXXX");
								}
								if(temp!=null){
									firstName = temp[0] ;
									lastName = temp[1] ;
								}
								if(firstName.equals(" ") || firstName.equals(""))
									firstName = mePhoneNo;
								if(lastName.equals(" ") || lastName.equals(""))
									lastName = mePhoneNo;
							}



							OracleCallableStatement ps = (OracleCallableStatement) stmt.getConnection().prepareCall( "BEGIN PROC_FLEXI_SMSTOPUP(?,?,?,?,?,?,?,?,?,?,?,?,?); END;" );
							ps.setString(1,ucUserId);
							ps.setString(2,pack_prefix);
							ps.setString(3,null);
							ps.setInt(4,Integer.parseInt(me_id));
							ps.setString(5,mePhoneNo);
							ps.setDouble(6,Double.parseDouble(localAmount));
							ps.registerOutParameter(7,OracleTypes.VARCHAR );
							ps.setString(8,firstName);
							ps.setString(9,lastName);
							ps.setDouble(10,Double.parseDouble(smsComm));
							ps.setString(11,destPhnNo);
							ps.setString(12,seriesId);
							ps.setDouble(13,Double.parseDouble(amountToRecharge));

							System.out.println("declare ret VARCHAR2(1000);BEGIN PROC_FLEXI_SMSTOPUP("+ucUserId+",'"+pack_prefix+"',null,"+me_id+",'"+mePhoneNo+"',"+localAmount+",ret,'"+firstName+"','"+lastName+"',"+smsComm+","+destPhnNo+"); end;");
							ps.execute();
							succ = ps.getString(7);

							if(succ.indexOf("Successful")<0)
							{

								//throw new Exception("<font color=\"RED\" size=4>Error: "+succ+"</font>" );
								msg="AAARECHARGE:Sorry Recharge Failed. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								System.out.print("Recharge Failed From Procedure !! for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", "+modem_id+")";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);
								continue;

							}


							if(operator_id != null && !operator_id.equals("-1"))
							{
								qry = "Select NVL(supplier_id,'-1') from flexi_operator@skydb_muci_link where  id = "+operator_id+"";
								System.out.println(qry);
								rs = stmt.executeQuery(qry);
								while(rs.next())
								{
									supplier_id = rs.getString(1);
								}
							}
							if(supplier_id == null || supplier_id.equals("-1"))
							{
								qry = "Select NVL(supplier_id,'-1') from flexi_country@skydb_muci_link where  id = "+country_id+"";
								System.out.println(qry);
								rs = stmt.executeQuery(qry);
								while(rs.next())
								{
									supplier_id = rs.getString(1);
								}
							}


							if(supplier_id == null || supplier_id.equals("-1"))
							{
									msg="AAARECHARGE:Sorry No Supplier. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry No Supplier!! for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", "+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;

							}

							qry = " select USERNAME pinnumber,(select MOBILE from emp where me_id="+me_id+" and uc_user_id="+ucUserId +") phoeNo from SUBSCRIBERS@skydb_muci_link "+
								" where id = (select SB_ID from REGISTERED_PHONES@skydb_muci_link where rateplan_id = "+rpId+" and" +
							" PHONE_NO=(select MOBILE from emp where me_id= " + me_id + "  and uc_user_id="+ucUserId +" ))  ";
							rs = stmt.executeQuery(qry);

							if(rs.next())
							{
								pinNum = rs.getString("pinnumber");
								phone_no = rs.getString("phoeNo");
							}

							qry = "select flx_id.nextval from dual";
							System.out.println(qry);
							rs= stmt.executeQuery(qry);
							if(rs.next()) flx_id = rs.getString(1);


							qry = "select name from area_group@oms_link where id="+p_area_id;

							System.out.println(qry);
							rs= stmt.executeQuery(qry);
							if(rs.next())
							{
								areaName = rs.getString(1)+"-FLX-"+flx_id;
							}


							ps = (OracleCallableStatement) stmt.getConnection().prepareCall( "BEGIN PROC_SMS_TOPUP@skydb_muci_link(?,?,?,?,?,?,?,?,?); END;" );
							ps.setString(1,pinNum);
							ps.setString(2,phone_no);
							ps.setString(3,destPhnNo);
							ps.setDouble(4,Double.parseDouble(localAmount));
							ps.setString(5,areaName);
							ps.registerOutParameter(6,OracleTypes.VARCHAR );
							ps.setDouble(7,Double.parseDouble(amountToRecharge));
							ps.setString(8,countryCode);
							ps.setString(9,country_name);

							System.out.println("BEGIN PROC_SMS_TOPUP@skydb_muci_link('"+pinNum+"','+phone_no+','"+destPhnNo+"',"+amount+",'"+areaName+"',ret,"+amountToRecharge+","+countryCode+","+country_name+") END;/");

							ps.execute();
							ret = ps.getInt(6);


							if(ret == 0)
							{

								qry = "Update topup_request set me_id ="+me_id+", supp_id="+supplier_id+",shop_comm="+smsComm+",series_id="+seriesId+", inbox_id= "+aVal[0]+" where msgid='"+areaName+"'";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								if(country_id != null && country_id.equals("13"))
								{
									if(operator_id != null && !operator_id.equals("-1"))
									{
										qry = "Select name from flexi_operator@skydb_muci_link where  id = "+operator_id+"";
										System.out.println(qry);
										rs = stmt.executeQuery(qry);
										while(rs.next())
										{
											telcoName = rs.getString(1);
										}
									}
									qry = "Update topup_request set telco='"+telcoName+"'||'-SG' where msgid='"+areaName+"'";
									System.out.println(qry);
									stmt.executeUpdate(qry);

								}


								if(destPhnNo!=null && destPhnNo.indexOf("880") ==0)
								{
									tmpDestPhone= destPhnNo.substring(2);
								}else
								{
									tmpDestPhone = destPhnNo;
								}


								String preKey ="";
								String postKey = "";
								String Gateway="";

								if(supplier_id != null && !supplier_id.equals("12") && !supplier_id.equals("13") && !supplier_id.equals("14"))
								{

									qry = "Select url from flexi_supplier@skydb_muci_link where id="+supplier_id+"";
									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while(rs.next())
									{
										urlStr = rs.getString(1)+"?";
										Gateway = rs.getString(1);
									}

									qry = "Select b.param_name,a.param_name,a.value,a.param_def_id from flexi_supplier_param@skydb_muci_link a, flexi_param_def@skydb_muci_link b where a.param_def_id=b.param_id and a.flexi_supplier_id="+supplier_id;
									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while(rs.next())
									{
										theSupp = rs.getString(1);

										if(supplier_id != null && (supplier_id.equals("15") || supplier_id.equals("16") || supplier_id.equals("17") || supplier_id.equals("18") || supplier_id.equals("19") || supplier_id.equals("20") || supplier_id.equals("21")))
										{

											if (rs.getString(1) != null && rs.getString(1).equals("LOGIN"))
											{
													preKey = rs.getString(3); if(preKey == null || preKey.equals("null")) preKey = "";
											}
											if (rs.getString(1) != null && rs.getString(1).equals("PASS"))
											{
													postKey = rs.getString(3); if(postKey == null ||postKey.equals("null")) postKey = "";
											}
										}

										if(rs.getString(1) != null && (rs.getString(1).equals("LOGIN") || rs.getString(1).equals("PASS")))
										{
											if(theCountt >0) urlStr +="&";
											urlStr += rs.getString(2)+"="+rs.getString(3);
										} else if(rs.getString(1) != null && rs.getString(1).equals("MOBILE"))
										{
											if(theCountt >0) urlStr +="&";
											urlStr += rs.getString(2)+"="+tmpDestPhone;
										} else if(rs.getString(1) != null && rs.getString(1).equals("AMOUNT"))
										{
											if(theCountt >0) urlStr +="&";
											urlStr += rs.getString(2)+"="+amountToRecharge;
										} else if(rs.getString(1) != null && rs.getString(1).equals("EXTRA"))
										{
											if(rs.getString(2) != null && !rs.getString(2).equals("null"))
											{
												if(theCountt >0) urlStr +="&";
												urlStr += rs.getString(2)+"="+rs.getString(3);
											}
										}else if(rs.getString(1) != null && rs.getString(1).equals("SERVICE") && serviceCount ==0 && rs.getString(2) != null && !rs.getString(2).equals("null") && rs.getString(3) != null && !rs.getString(3).equals("null"))
										{
											if(prepost!=null && prepost.equals("0"))
											{
												if(rs.getString(4)!=null && rs.getString(4).equals("107"))
												{
													if(theCountt >0) urlStr +="&";
													urlStr += rs.getString(2)+"="+rs.getString(3);
													serviceCount++;
												}
											}

											if(prepost!=null && prepost.equals("1"))
											{
												if(rs.getString(4)!=null && rs.getString(4).equals("108"))
												{
													if(theCountt >0) urlStr +="&";
													urlStr += rs.getString(2)+"="+rs.getString(3);
													serviceCount++;
												}
											}
										}

										theCountt++;
									} // End result Set while

									if(supplier_id != null && (supplier_id.equals("15") || supplier_id.equals("16") || supplier_id.equals("17") || supplier_id.equals("18") || supplier_id.equals("19") || supplier_id.equals("20")))
									{

										qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+Gateway+"','"+preKey+tmpDestPhone+postKey+amountToRecharge+"',"+aVal[0]+","+gateWayModem_id+")";
										System.out.println(qry);
										stmt.executeUpdate(qry);


									}else
									{

										System.out.println(">>"+urlStr);

										String responseStr = "";
										String xmldata = "";
										String sProductID = "";
										if(urlStr!=null && urlStr.indexOf("api.eload.sg")>=0)
										{
											if(operator_id != null && operator_id.equals("7"))
											{
												sProductID = "1";
											}else if(operator_id != null && operator_id.equals("8"))
											{
												sProductID = "3";
											}else if(operator_id != null && operator_id.equals("9"))
											{
												sProductID = "2";
											}
											xmldata = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
											"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""+
											" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
											"<soap:Body>"+
											"<RequestInput xmlns=\"eloadsg\">"+
											"<sClientUserName>"+preKey+"</sClientUserName>"+
											"<sClientPassword>"+postKey+"</sClientPassword>"+
											"<sClientTxID>"+areaName+"</sClientTxID>"+
											"<sProductID>"+sProductID+"</sProductID>"+
											"<dProductPrice>"+amountToRecharge+"</dProductPrice>"+
											"<sCustomerMobileNumber>"+tmpDestPhone+"</sCustomerMobileNumber>"+
											"<sResponseID></sResponseID>"+
											"<sResponseStatus></sResponseStatus>"+
											"</RequestInput>"+
											"</soap:Body>"+
											"</soap:Envelope>";

											System.out.println(xmldata);
											//URL url = new URL("\""+Gateway+"\"");
											//URL url = new URL("api.eload.sg");
											URL url = new URL("http://api.eload.sg/connect.asmx?WSDL");


											HttpURLConnection connection = (HttpURLConnection) url.openConnection();

											connection.setRequestProperty("Content-Length", String.valueOf(xmldata.length()));
											connection.setRequestProperty("Content-Type", "text/xml");
											connection.setRequestProperty("Connection", "Close");
											connection.setRequestProperty("SoapAction", "eloadsg/RequestInput");
											connection.setDoOutput(true);

											PrintWriter pw = new PrintWriter(connection.getOutputStream());
											pw.write(xmldata);
											pw.flush();

											BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
											String line;
											while((line = rd.readLine()) != null)
											{
												responseStr += line;
											}
											System.out.println(line);

										}else if(urlStr!=null && urlStr.indexOf("bdapi.globaltopup.me")>=0)
										{
											urlStr += "&mtid="+areaName;
											responseStr = URLReader.readURL(new URL(urlStr)) ;
										}else
										{
											responseStr = URLReader.readURL(new URL(urlStr)) ;
										}

										System.out.println(">>>>>>>>>>>"+responseStr);

										if(urlStr!=null && urlStr.indexOf("api.eload.sg")>=0)
										{
											txnid = responseStr.substring(responseStr.indexOf("<sResponseID>")+13,responseStr.indexOf("</sResponseID>"));
											System.out.println("txnid::"+txnid);

											remarks = responseStr.substring(responseStr.indexOf("<sResponseStatus>")+17,responseStr.indexOf("</sResponseStatus>"));
											System.out.println("remarks::"+remarks);
											if(remarks != null && remarks.indexOf("SUCCESS")<0)
											txnid = "-1";


										}else if(urlStr!=null && urlStr.indexOf("takacharge.com")>=0)
										{
											if(responseStr!=null && responseStr.indexOf("UNSUCCESS")>=0)
											{
												txnid = responseStr.substring(responseStr.indexOf("TX: ")+4,responseStr.length());
												if(txnid!=null && txnid.equals("101"))
												{
													msg="Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
													remarks="error mobile no format error";
												}
												if(txnid!=null && txnid.equals("105"))
												{
													msg="This request can not be processed now. Please try after 20 minutes";
													remarks="error you can not recharge this number within 20 minutes";
												}
												if(txnid!=null && txnid.equals("106"))
												{
													msg = "Invalid Tariff for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
													remarks="error no enough balance in your account";
												}
												if(txnid!=null && txnid.equals("107"))
												{
													msg = "Invalid Tariff for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
													remarks="error unauthorized user";
												}
												if(txnid!=null && txnid.equals("108"))
												{
													msg = "Incorrect message syntax for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
													remarks="error enough required information not supplied";
												}
												if(txnid!=null && txnid.equals("109"))
												{
													msg = "Technecal Probelm. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
													remarks="error all channels are busy now";
												}
												if(txnid!=null && txnid.equals("110"))
												{
													msg="Invalid amount for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
													remarks="error minimum or maximum recharge amount not matched";
												}
												if(txnid!=null && txnid.equals("111"))
												{
													msg = "Technecal Probelm. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
													remarks="error unexpected error";
												}
												if(txnid!=null && txnid.equals("112"))
												{
													msg = "Technecal Probelm. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
													remarks="error system deny to recharge for maintanence";
												}
												txnid = "-1";

											} else
											{
												txnid = responseStr.substring(responseStr.indexOf("TX: ")+4,responseStr.length());
												remarks="SUCCESSFUL";

											}
										} else if(urlStr!=null && urlStr.indexOf("mobflexi.com")>=0)
										{
											txnid = responseStr.substring(responseStr.indexOf("<TRID>")+6,responseStr.indexOf("</TRID>"));
											System.out.println("txnid::"+txnid);
											remarks = responseStr.substring(responseStr.indexOf("<STATUS>")+8,responseStr.indexOf("</STATUS>"));

											System.out.println("remarks::"+remarks);
										} else if(urlStr!=null && urlStr.indexOf("topupcard.sg")>=0)
										{
											txnid = responseStr.substring(responseStr.indexOf("tempuri.org")+14,responseStr.indexOf("</string>"));
											System.out.println("txnid::"+txnid);
											remarks = "ON PROCESS";
											System.out.println("remarks::"+remarks);

										}else if(urlStr!=null && urlStr.indexOf("bdapi.globaltopup.me")>=0)
										{
											StringTokenizer st1 = new StringTokenizer(responseStr, delims1);

											if(st1.hasMoreElements())
											{
												retCode = (String)st1.nextElement();
											}

											if(st1.hasMoreElements())
											{
												retText = (String)st1.nextElement();
											}
											if(st1.hasMoreElements())
											{
												txnid = (String)st1.nextElement();
											}
											if(st1.hasMoreElements())
											{
												supBal = (String)st1.nextElement();
											}

											remarks =  responseStr;
											System.out.println("remarks::"+remarks);

										}else if(responseStr!=null && responseStr.indexOf("UNSUCCESS")>=0)
										{
											txnid = "-1";
											msg = "Technecal Probelm. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
											remarks="UNSUCCESSFUL";
										}else if(responseStr!=null && responseStr.indexOf("SUCCESS")>=0)
										{
											txnid = "1";
											remarks="SUCCESSFUL";

										}

										if(txnid!= null & txnid.length()<1) txnid = "-1";


										if(urlStr!=null && urlStr.indexOf("bdapi.globaltopup.me")>=0)
										{
											if(retCode!=null && (retCode.equals("1") || retCode.equals("0")) )
											{
												tmpStr = "Y";
											}else
											{
												tmpStr = "N";
											}
										}else
										{

											if(Double.parseDouble(txnid) < 0)
											{
												tmpStr = "N";
											}
											if(Double.parseDouble(txnid) >=0)
											{
												tmpStr = "Y";
											}
										}

										if(tmpStr != null && tmpStr.equals("Y"))
										{
											if(urlStr!=null && urlStr.indexOf("bdapi.globaltopup.me")>=0)
											{
												msg = "Recharge to "+destPhnNo+" successfully completed by "+amountToRecharge+" "+currencyName+". Transaction no-"+aVal[0]+"";
											}else
											{
												msg = "Recharge to "+destPhnNo+" successfully completed by "+amountToRecharge+" "+currencyName+". Transaction no-"+txnid+"";
											}
										}else if (tmpStr != null && tmpStr.equals("N"))
										{
											ps = (OracleCallableStatement) stmt.getConnection().prepareCall( "BEGIN PROC_TOPUP_REFUND; END;" );

											if(remarks!=null && remarks.indexOf("Invalid phonenumber")>=0)
											{
												msg="Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
											}else if(msg!=null && msg.length()<1)
											{
												msg = "Mobile "+tmpDestPhone+" RECHARGE FAILED FOR Amount"+amountToRecharge+"";
											}
										}


										qry = "Update topup_request set processed='"+tmpStr+"',transaction_id = '"+txnid+"',topup_process_time = sysdate, response='"+remarks+"', inbox_id= "+aVal[0]+" where msgid = '"+areaName+"'";

										System.out.println(qry);
										stmt.executeUpdate(qry);

										qry = "Select NVL((Select (-1)*sum(quantity*transaction_type) from transaction_detail@oms_link where fixed_acc = (Select fixed_acc_id from emp e1 where e1.me_id = "+me_id+")),0) from dual";
										System.out.println(qry);
										rs = stmt.executeQuery(qry);
										while (rs.next())
										{
											recvAmount = rs.getDouble(1);
										}


										msg = "AAARECHARGE:"+msg+" Balance"+twoDigit.format(recvAmount);
										qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
										System.out.println(qry);
										stmt.executeUpdate(qry);

										qry ="Update sms_inbox@skydb_muci_link set txn_processed='"+tmpStr+"' where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
										System.out.println(qry);
										stmt.executeUpdate(qry);
									}


								} // Supplier Id Found and not SG Local


							} // END ret=0











						} //Flexi Block ends



					} // End List Loop
				} // End List Size Check

			}else  // No SMS
			{
				System.out.println("No sms found");
			}
			stmt.getConnection().commit();

		} catch (SQLException localSQLException1)
		{
			System.out.print("eTopupSMSACC Job failed: " + localSQLException1.getMessage());
			try
			{
				if(stmt != null) stmt.getConnection().rollback();
			}catch(Exception e)
			{}
		} catch (Exception localException)
		{
			System.out.print("eTopupSMSACC Job failed: " + localException.getMessage());
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
		}// Try-Catch End


	} // End ETop

} //End Class