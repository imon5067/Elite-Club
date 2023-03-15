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

public class ETopUpSMSPinless implements Job
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

		String modem_id = "21";
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
		String selectedSupplier="-1";

		String prefix = "";
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
		String airTimeRateplan_id = "15567";
		String qryCond ="";
		String seriesId ="";
		int theCount=0;
		String localAmount = "";
		String amountToRecharge="";
		String variableAmt = "";
		String smsComm = "0";
		String maxPackId = "7258";
		String ucUserId = "8";
		String bKashSeriesId = "1107258";
		String CC_NUMBER = "84455516";

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
		String p_area_id = "11";
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

		String theOpCode ="";
		String india_op_id = "";
		String clientTranId = "-1";

		String sec_supplier_id ="";
		String max_topup_val = "";
		int first_val = 0;
		int second_val=0;

		boolean cc_comm_found = false;
		String cc_packId = "";
		String cc_packName = "";
		String cc_packPrefix = "";
		String idd_numbers="";
		boolean custFound = false;
		String cust_id = "";
		String spKey="";
		Vector vt = null;
		String sp="";
		String spText = "";
		String theAl_id="-1";
		String theMap_phone = "";
		String theAlids="";
		String qryText = "";
		boolean pinFound = false;


		try
		{
			con = new ConnectDB();
			stmt = con.connect();

			/********* Search Customer SMS ******************/
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
				qry ="Select id,mobile,UPPER(REPLACE(REPLACE(msg, CHR(13), ' '), CHR(10), ' ')), NVL(supplier_id,'-1') from sms_inbox@skydb_muci_link where modem_id = "+modem_id+" and status = 'N'";
				System.out.println(qry);
				rs = stmt.executeQuery(qry);
				while(rs.next())
				{
					id = rs.getString(1);
					mobile = rs.getString(2);
					msg = rs.getString(3);
					if(msg==null) msg = " ";
					selectedSupplier=rs.getString(4);
					aVal = new String[]{id,mobile,msg,selectedSupplier};
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
						aVal = (String[])aList.get(h);
						/******* Initialize Loop Variables ************/
						meFound = false;
						keyFound = false;
						ignoreFound = false;
						destPhnNo = "";
						msg = "";
						selectedSupplier="-1";
						keyVal = "";
						key = "";
						val = -1;
						prepostVal = "";
						prepost = "0";
						operator_id = "-1";

						prefix = "";
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
						theOpCode ="";
						india_op_id = "";
						clientTranId ="-1";

						sec_supplier_id ="";
						max_topup_val = "";
						first_val = 0;
						second_val=0;


						cc_comm_found = false;
						cc_packId = "";
						cc_packName = "";
						cc_packPrefix = "";
						idd_numbers = "";
						custFound = false;
						cust_id = "";

						spKey="";
						sp="";
						spText = "";
						theAl_id="-1";
						vt = null;
						theMap_phone = "";
						theAlids="";
						pinFound = false;
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

							msg = "Salaam Pinless:Sorry Invalid destination number, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
							qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id,sent_result,sent_time) values "+
								  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+",1,sysdate)";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							continue;
						}


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
						}
						if(st.hasMoreElements())
						{
							prepostVal = (String)st.nextElement();
							if(prepostVal != null && (prepostVal.equals("p") || prepostVal.equals("P")))
							{
								prepost = "1";
							}else
							{
								prepost = "0";
							}
						}

						/*************** Registration & Topup **********************/
						if(destPhnNo != null && (destPhnNo.equals("R") || destPhnNo.equals("T")))
						{
							st = new StringTokenizer(aVal[2], delims);
							if(st.hasMoreElements())
							{
								spKey = (String)st.nextElement();
								if(spKey!=null) spKey =spKey.trim();
							}
							if(st.hasMoreElements())
							{
								pinNum = (String)st.nextElement();
								if(pinNum!=null) pinNum =pinNum.trim();
							}
							qry = "Select id from subscribers_cards@skydb_muci_link where username='"+pinNum+"' and active='Y' and ls='N' and rateplan_id="+maxPackId+"";
							System.out.println(qry);
							rs=stmt.executeQuery(qry);
							while(rs.next())
							{
								pinFound = true;
							}
							if(pinFound == false)
							{
								msg = "Salaam Pinless:Sorry the pin is invalid. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								qry = "Select count(*) from subscribers_cards@skydb_muci_link where username='"+pinNum+"' and active='Y' and (ls='S' OR ls='R') and rateplan_id="+maxPackId+"";
								System.out.println(qry);
								rs=stmt.executeQuery(qry);
								while(rs.next())
								{
									if(rs.getInt(1)>0)
									msg = "Salaam Pinless:Sorry the pin is used. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								}
								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
								System.out.println(qry);
								stmt.executeUpdate(qry);
								continue;

							}

							qry = "Select signup@skydb_muci_link('2*"+pinNum+"','"+aVal[1]+"') from dual";
							System.out.println("function qry::"+qry);
							try
							{
								rs=stmt.executeQuery(qry);
								while(rs!=null && rs.next())
								{
									theRet = rs.getString(1);
								}
								if(theRet != null && theRet.equals("1"))
								{
									qry = "Select NVL((Select tkleft from subscribers@skydb_muci_link where username='"+pinNum+"'),(Select tkleft from subscribers@skydb_muci_link where username=(select remarks from subscribers_cards@skydb_muci_link where username = '"+pinNum+"'))) from dual";
									System.out.println(qry);
									rs=stmt.executeQuery(qry);
									while(rs.next())
									{
										recvAmount = rs.getDouble(1);
									}
									msg = "Salaam Pinless:Registration/Recharge Request Successful. Your Balance is:"+ twoDigit.format(recvAmount)+" SGD";
									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);


									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;

								}
							}catch(Exception e)
							{
								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								msg = "Salaam Pinless:Registration/Recharge Request Failed. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
								System.out.println(qry);
								stmt.executeUpdate(qry);
								continue;
							}

						}
						/*************** Authenticate Customer *********************/
						qry = "Select sb_id from registered_phones@skydb_muci_link where rateplan_id="+maxPackId+" and phone_no='"+aVal[1]+"'";
						System.out.println(qry);
						rs = stmt.executeQuery(qry);
						while (rs.next())
						{
							custFound = true;
							cust_id = rs.getString(1);
						}

						if(!custFound) //cust not found
						{
							qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							msg = "Salaam Pinless:Athentication Failed, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
							qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
								  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							continue;

						}


					/************************ // setting Speeddial **********************************/
						if(destPhnNo != null && destPhnNo.equals("S"))
						{
							st = new StringTokenizer(aVal[2], delims);
							if(st.hasMoreElements())
							{
								spKey = (String)st.nextElement();
								if(spKey!=null) spKey =spKey.trim();
							}


							vt = new Vector();
							while(st.hasMoreElements())
							{
								sp = (String)st.nextElement();
								if(sp!=null) sp =sp.trim();

								vt.add(sp);
							}


							qryText =" and id not in (-1)";
							for(int hh = 0; hh<vt.size();hh++)
							{
								sp = (String)vt.get(hh);
								if(sp !=null && sp.length()>=8)
								{
									theAl_id="-1";
									qry = "Select NVL((Select min(id) from cc_accesslist@skydb_muci_link where id not in "+
									" (Select al_id from cc_speeddial@skydb_muci_link where sb_id = "+cust_id+")),-1) from dual";
									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while (rs.next())
									{
										theAl_id = rs.getString(1);
									}


									if(theAl_id != null && !theAl_id.equals("-1"))
									{
										qry = "Select speeddial from cc_accesslist@skydb_muci_link where id = "+theAl_id+"";
										System.out.println(qry);
										rs = stmt.executeQuery(qry);
										while (rs.next())
										{
											theMap_phone = rs.getString(1);
										}

										qry = "Insert into cc_speeddial@skydb_muci_link "+
										" (id, sb_id, mapphone, al_id, language, creationdate, rateplan_id) "+
										  " values (cc_speeddial_sq.nextval@skydb_muci_link,"+cust_id+",'"+sp+"',"+theAl_id+",'en', sysdate, "+maxPackId+")";
										System.out.println(qry);
										stmt.executeUpdate(qry);

										stmt.getConnection().commit();

										spText +="::"+theMap_phone+"->"+sp;
									}
								}
							}

							qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							msg = "Salaam Pinless:Speed dial settings:"+spText+"";
							qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
								  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
							System.out.println(qry);
							stmt.executeUpdate(qry);
							continue;

						}

						/************************ End setting Speeddial **********************************/

						/*********************** Balance *********************************/
						qry = "Select tkleft from subscribers@skydb_muci_link where id ="+cust_id+"";
						System.out.println(qry);
						rs = stmt.executeQuery(qry);
						while (rs.next())
						{
							recvAmount = rs.getDouble(1);
						}
						recvCurrency = "SGD";

						/*************** Handle Keyword Bal *****************************************/
						if(destPhnNo != null && destPhnNo.equals("BAL"))
						{
							qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							msg = "Salaam Pinless:Your Balance is "+twoDigit.format(recvAmount)+" "+recvCurrency+".";
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

								msg = "Salaam Pinless:Sorry Invalid destination number, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								continue;
							}
						}


						/************* Find KeyWord **************/
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

						if(keyFound)
						{
							try{
								amount = keyVal.substring(prefix.length());
								System.out.println("prefix::"+prefix+":amount:"+amount);
								val = Integer.parseInt(amount);
							}catch(Exception localException)
							{
								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								msg = "Salaam Pinless:Invalid Keword/Value, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
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

								msg = "Salaam Pinless:Invalid Keword/Value, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
								System.out.println(qry);
								stmt.executeUpdate(qry);
								continue;
						}




						if(theType != null && theType.equals("FLX"))// Flexi Block
						{


							/*********** Resolve Flexi Series *******************/
							qry = "Select seriesid from sms_ratechart@skydb_muci_link where country_id = "+country_id+" and seriesid in ( Select id from cardseries@skydb_muci_link  where rate_plan_id = "+airTimeRateplan_id+" and is_calling_flexi='Y' and flexi_country_id = "+country_id+")";
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

								qry = "Select seriesid from sms_ratechart@skydb_muci_link where country_id = "+country_id+" and operator_id="+operator_id+" and seriesid in ( Select id from cardseries@skydb_muci_link  where rate_plan_id = "+airTimeRateplan_id+" and is_calling_flexi='Y' and flexi_country_id = "+country_id+")";
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
									msg="Salaam Pinless:Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
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
									msg="Salaam Pinless:Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
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

								supplier_id="-1";
								if(aVal[3] != null && !aVal[3].equals("-1"))
								{
									supplier_id = aVal[3];
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

								if(supplier_id != null && supplier_id.equals("23"))
								{

									qry = "Select NVL((select operator_id from (Select operator_id from (select max(length(code)) as aa,operator_id from "+
									" telco_codes@skydb_muci_link where "+
									" instr(upper('"+destPhnNo+"'), code) = 1 group by operator_id order by aa desc)	where rownum <2)),-1) from dual";
									System.out.println(qry);
									rs = stmt.executeQuery(qry);

									while(rs.next())
									{
										india_op_id = rs.getString(1);
									}

									if(india_op_id != null && india_op_id.equals("-1"))
									{

										msg="Salaam Pinless:Sorry Operator not supported, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
										System.out.print("Sorry Operator not supported, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

										qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
											  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", "+modem_id+")";
										System.out.println(qry);
										stmt.executeUpdate(qry);

										qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+aVal[0]+"";
										System.out.println(qry);
										stmt.executeUpdate(qry);
										continue;

									}else
									{
										qry = "Select op_code from operator_codes@skydb_muci_link where id = "+india_op_id;
										System.out.println(qry);
										rs = stmt.executeQuery(qry);

										while(rs.next())
										{
											theOpCode = rs.getString(1);
										}
										qry = "Select egen_id.nextval@skydb_muci_link from dual";
										System.out.println(qry);
										rs = stmt.executeQuery(qry);
										while (rs.next())
										{
											clientTranId = rs.getString(1);
										}


									}
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
									msg="Salaam Pinless:Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
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

							if(countryCode != null && countryCode.equals("65"))
							{
								qry = " Select NVL((Select (sysdate - tr )*24*60 from( Select tr from ( Select topup_request_time tr from topup_request where dst_no='"+destPhnNo+"' and processed = 'Y' and amount= "+amount+" order by id desc) where rownum <2)),9999999) a, "+
								" NVL((Select (sysdate - tp )*24*60 from( Select tp from ( Select topup_process_time tp from topup_request where dst_no='"+destPhnNo+"'  and processed = 'Y' and amount= "+amount+" order by id desc) where rownum <2)),9999999) b from dual";

							}else
							{
								qry = " Select NVL((Select (sysdate - tr )*24*60 from( Select tr from ( Select topup_request_time tr from topup_request where dst_no='"+destPhnNo+"' and processed = 'Y' order by id desc) where rownum <2)),9999999) a, "+
								" NVL((Select (sysdate - tp )*24*60 from( Select tp from ( Select topup_process_time tp from topup_request where dst_no='"+destPhnNo+"'  and processed = 'Y' order by id desc) where rownum <2)),9999999) b from dual";
							}
							System.out.println(qry);
							rs = stmt.executeQuery(qry);
							while(rs.next())
							{
								timeDiff = minDiff - Math.min(rs.getDouble(1),rs.getDouble(2));
							}

							if(timeDiff > 0)
							{
								timeDiff = Math.ceil(timeDiff);
								msg = "Salaam Pinless:This request can not be processed now. Please try after "+timeDiff+" minute";
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
									msg="Salaam Pinless:Sorry Invalid Rate Configuration. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
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
									msg="Salaam Pinless:Sorry Invalid Amount To Recharge. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
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
									qry = "Select seriesid, "+amount+"*sale_denomination/purchase_denomination,NVL(service_charge/100,0)*("+amount+"*sale_denomination/purchase_denomination)+NVL(fixed_charge,0)  from sms_ratechart@skydb_muci_link where (min_value is not null and min_value >0) and (max_value is not null and max_value >0) and   "+amount+" between min_value and max_value and country_id="+country_id+" "+qryCond+"";
									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while(rs.next())
									{
										seriesId = rs.getString(1);
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
									msg="Salaam Pinless:Sorry Invalid Amount To Recharge. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
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
									qry = "Select seriesid,purchase_denomination,NVL(service_charge/100,0)*sale_denomination+NVL(fixed_charge,0) from sms_ratechart@skydb_muci_link where country_id="+country_id+" "+qryCond+" and sale_denomination="+Ramt;
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
										serviceCharge = rs.getString(3);
									}
									localAmount = Ramt;
									variableAmt = "N";
								}
							}
							if(variableAmt != null && variableAmt.length()<1)
							{
									msg="Salaam Pinless:Sorry Invalid Amount To Recharge. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
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

							/********************* Credit Limit Check **************************/
							System.out.println("::"+(recvAmount-Double.parseDouble(localAmount)-Double.parseDouble(serviceCharge))+"::"+recvAmount);
							if(recvAmount-Double.parseDouble(localAmount)- Double.parseDouble(serviceCharge)< 0)
							{
									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									msg = "Salaam Pinless:Sorry, You do not have enough balance. Your balance:"+twoDigit.format(recvAmount)+". For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+","+modem_id+")";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;
							}




							supplier_id="-1";
							if(aVal[3] != null && !aVal[3].equals("-1"))
							{
								supplier_id = aVal[3];
							}

							if(supplier_id == null || supplier_id.equals("-1"))
							{

								if(operator_id != null && !operator_id.equals("-1"))
								{
									qry = "Select NVL((Select NVL(supplier_id,-1) from flexi_value_wise_supplier@skydb_muci_link where operator_id="+operator_id+" and value="+amountToRecharge+"),-1) from dual";
									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while(rs.next())
									{
										supplier_id = rs.getString(1);
									}
									if(supplier_id == null || supplier_id.equals("-1"))
									{
										qry = "Select NVL(supplier_id,'-1') from flexi_operator@skydb_muci_link where  id = "+operator_id+"";
										System.out.println(qry);
										rs = stmt.executeQuery(qry);
										while(rs.next())
										{
											supplier_id = rs.getString(1);
										}
									}
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
									msg="Salaam Pinless:Sorry No Supplier. for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
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


							qry = " select USERNAME pinnumber from SUBSCRIBERS@skydb_muci_link where id="+cust_id+"";
							rs = stmt.executeQuery(qry);

							if(rs.next())
							{
								pinNum = rs.getString("pinnumber");
							}

							qry = "select flx_id.nextval from dual";
							System.out.println(qry);
							rs= stmt.executeQuery(qry);
							if(rs.next()) flx_id = rs.getString(1);

							areaName = "PINLESS"+"-FLX-"+flx_id;


							OracleCallableStatement ps = (OracleCallableStatement) stmt.getConnection().prepareCall( "BEGIN PROC_SMS_TOPUP_PINLESS@skydb_muci_link(?,?,?,?,?,?,?,?,?); END;" );
							ps.setString(1,pinNum);
							ps.setString(2,aVal[1]);
							ps.setString(3,destPhnNo);
							ps.setDouble(4,(Double.parseDouble(localAmount)+Double.parseDouble(serviceCharge)));
							ps.setString(5,areaName);
							ps.registerOutParameter(6,OracleTypes.VARCHAR );
							ps.setDouble(7,Double.parseDouble(amountToRecharge));
							ps.setString(8,countryCode);
							ps.setString(9,country_name);

							System.out.println("BEGIN PROC_SMS_TOPUP_PINLESS@skydb_muci_link('"+pinNum+"','"+aVal[1]+"','"+destPhnNo+"',"+amount+",'"+areaName+"',ret,"+amountToRecharge+","+countryCode+","+country_name+") END;/");

							ps.execute();
							ret = ps.getInt(6);

							if(ret == 0)
							{

								qry = "Update topup_request set supp_id="+supplier_id+",shop_comm="+smsComm+",series_id="+seriesId+", inbox_id= "+aVal[0]+",egen_id="+clientTranId+",prepost="+prepost+" where msgid='"+areaName+"'";
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


					/************************ Split amount from one21BD/eGen *************************/

								if(supplier_id != null && (supplier_id.equals("22") || supplier_id.equals("23")) )
								{
									sec_supplier_id ="";
									max_topup_val = "0";
									first_val = 0;
									second_val=0;

									qry = "Select NVL(secondary_supplier_id,-1) from flexi_supplier@skydb_muci_link where id = "+supplier_id+"";
									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while(rs.next())
									{
										sec_supplier_id = rs.getString(1);
									}

									qry = "Select NVL((Select max(value) from flexi_supplier_value@skydb_muci_link where supplier_id = "+supplier_id+" and "+amountToRecharge+" >= value),0) from dual";
									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while(rs.next())
									{
										max_topup_val = rs.getString(1);
									}

									first_val = Integer.parseInt(max_topup_val);
									second_val = Integer.parseInt(amountToRecharge) - Integer.parseInt(max_topup_val);
									if(first_val == 0)
									{
										supplier_id = sec_supplier_id;
										first_val = Integer.parseInt(amountToRecharge);
										second_val = 0;
										qry = "Update topup_request set supp_id="+supplier_id+" where msgid='"+areaName+"'";
										System.out.println(qry);
										stmt.executeUpdate(qry);
									}

									if (second_val > 0)
									{
										qry = "Insert into topup_request_secondary(id,msgid,src_ip,originator,prefix,dst_no,amount,orig_amount,history_id,"+
										" topup_request_time,supp_id,me_id,inbox_id,pin,shop_comm,series_id,telco,sb_id) "+
										" select id,msgid,src_ip,originator,prefix,dst_no,"+second_val+",orig_amount,history_id,topup_request_time + 30/(24*60),"+sec_supplier_id+","+
										" me_id,inbox_id,pin,shop_comm,series_id,telco,sb_id from topup_request where msgid= '"+areaName+"'";
										System.out.println(qry);
										stmt.executeUpdate(qry);

									}
								}
					/************************ Split amount from one21BD/eGen ENDS *************************/

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
											if(supplier_id != null && supplier_id.equals("23"))
											{
												urlStr += rs.getString(2)+"="+destPhnNo.substring(2);
											}else
											{
												urlStr += rs.getString(2)+"="+tmpDestPhone;
											}
										} else if(rs.getString(1) != null && rs.getString(1).equals("AMOUNT"))
										{
											if(theCountt >0) urlStr +="&";
											if(supplier_id != null && (supplier_id.equals("22") || supplier_id.equals("23")))
											{
												urlStr += rs.getString(2)+"="+first_val;
											}else
											{
												urlStr += rs.getString(2)+"="+amountToRecharge;
											}

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
										}else if(urlStr!=null && urlStr.indexOf("inapi.globaltopup.me")>=0)
										{
											urlStr += "&mtid="+areaName;
											urlStr += "&provider=NA";
											System.out.println(">>"+urlStr);
											responseStr = URLReader.readURL(new URL(urlStr)) ;
										}else if(urlStr!=null && urlStr.indexOf("egensolutions.in")>=0)
										{
											urlStr += "&operatorCode="+theOpCode;
											urlStr += "&clientTranId="+clientTranId;
											System.out.println(">>"+urlStr);
											responseStr = URLReader.readURL(new URL(urlStr)) ;
										}else if(urlStr!=null && urlStr.indexOf("hflexi")>=0)
										{
											System.out.println("hflexi Called");
										}else
										{
											responseStr = URLReader.readURL(new URL(urlStr)) ;
										}

										System.out.println(">>>>>>>>>>>"+responseStr);

										if(urlStr!=null && urlStr.indexOf("egensolutions.in")>=0)
										{
											retCode = responseStr.substring(responseStr.indexOf("<resultCode>")+12,responseStr.indexOf("</resultCode>"));
											retText = responseStr.substring(responseStr.indexOf("<resultValue>")+13,responseStr.indexOf("</resultValue>"));
											txnid = responseStr.substring(responseStr.indexOf("<operatorId>")+12,responseStr.indexOf("</operatorId>"));
											System.out.println("txnid::"+txnid);

											remarks = retText;
											System.out.println("remarks::"+remarks);


										}else if(urlStr!=null && urlStr.indexOf("api.eload.sg")>=0)
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
										}else if(urlStr!=null && urlStr.indexOf("hflexi")>=0)
										{
											txnid = "1";
											remarks = "SUCCESS";
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

										}else if(urlStr!=null && urlStr.indexOf("inapi.globaltopup.me")>=0)
										{
											StringTokenizer st2 = new StringTokenizer(responseStr, delims1);

											if(st2.hasMoreElements())
											{
												retCode = (String)st2.nextElement();
											}

											if(st2.hasMoreElements())
											{
												retText = (String)st2.nextElement();
											}
											if(st2.hasMoreElements())
											{
												txnid = (String)st2.nextElement();
											}
											if(st2.hasMoreElements())
											{
												supBal = (String)st2.nextElement();
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
										}else if(urlStr!=null && urlStr.indexOf("inapi.globaltopup.me")>=0)
										{
											if(retCode!=null && (retCode.equals("1") || retCode.equals("0")) )
											{
												tmpStr = "Y";
											}else
											{
												tmpStr = "N";

												qry = "Update topup_request_secondary set processed='"+tmpStr+"',transaction_id = '"+txnid+"',topup_process_time = sysdate, response='Master Topup Failed' where msgid = '"+areaName+"'";
												System.out.println(qry);
												stmt.executeUpdate(qry);

											}

										}else if(urlStr!=null && urlStr.indexOf("egensolutions.in")>=0)
										{
											if(retCode!=null && (retCode.equals("121") || retCode.equals("122") || retCode.equals("125")) )
											{
												tmpStr = "Y";
											}else
											{
												tmpStr = "N";
												qry = "Update topup_request_secondary set processed='"+tmpStr+"',transaction_id = '"+txnid+"',topup_process_time = sysdate, response='Master Topup Failed' where msgid = '"+areaName+"'";
												System.out.println(qry);
												stmt.executeUpdate(qry);

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
											if(urlStr!=null && (urlStr.indexOf("bdapi.globaltopup.me")>=0 || urlStr.indexOf("hflexi")>=0))
											{
												msg = "Recharge to "+destPhnNo+" successfully completed by "+amountToRecharge+" "+currencyName+". Transaction no-"+aVal[0]+"";
											}else
											{
												msg = "Recharge to "+destPhnNo+" successfully completed by "+amountToRecharge+" "+currencyName+". Transaction no-"+txnid+"";
											}
											if(country_id != null && country_id.equals("13"))
											{
												msg = "Recharge to "+telcoName+" number "+destPhnNo+" successfully completed by "+amountToRecharge+" "+currencyName+". Transaction no-"+txnid+"";
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

										qry = "Select tkleft from subscribers@skydb_muci_link where id ="+cust_id+"";
										System.out.println(qry);
										rs = stmt.executeQuery(qry);
										while (rs.next())
										{
											recvAmount = rs.getDouble(1);
										}

										msg = "Salaam Pinless:"+msg+" Balance: $"+twoDigit.format(recvAmount);
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
			System.out.print("ETopUpSMSPinless Job failed: " + localSQLException1.getMessage());
			try
			{
				if(stmt != null) stmt.getConnection().rollback();
			}catch(Exception e)
			{}
		} catch (Exception localException)
		{
			System.out.print("ETopUpSMSPinless Job failed: " + localException.getMessage());
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




	} // End Function eTop

} // End Class ETopUpSMSPinless