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

public class ETopUpSMS implements Job
{
	public void execute(JobExecutionContext paramJobExecutionContext) throws JobExecutionException
	{
		eTop();
	}

	public void eTop()
	{

		boolean bool = false;
		ConnectDB con = null;
		java.sql.Statement stmt = null;
		ResultSet rs = null;
		String qry = "";
		String id = "";
		String ids = "";
		String mobile = "";
		String msg = "";
		String selectedSupplier="-1";
		int count=0;
		String[] aVal = null;
		String delims = ",:.@&$#*^><!?_-/+;`'~[]{}()= ";
		String destPhnNo = "";
		String pin = "";
		String countryCode="";
		String operator_id ="";
		String country_id = "";
		double minDiff = 0.0;
		double timeDiff = 0.0;
		String amount = "";
		String Ramt = "";
		boolean operatorRate = false;
		int theCount=0;
		String variableAmt = "";
		String localAmount = "";
		String amountToRecharge="";
		boolean pinFound = false;
		boolean ignoreFound = false;
		String prepost ="0";
		String country_name ="";
		String code ="";
		String series_id = "";
		int retry_count = 0;
		String currencyName="";
		String prepostVal="";
		ArrayList aList = new ArrayList();

		String supplier_id="-1";
		String theOpCode ="";
		String india_op_id = "";
		String clientTranId = "-1";

		String sec_supplier_id ="";
		String max_topup_val = "";
		int first_val = 0;
		int second_val=0;

		String CC_NUMBER = "84455516";
		String p_sms = "SALAAM PINLESS! International Calling Card.BANGLADESH-$2=52 Mins,$5=130 Mins,$10= 260 Mins, INDIA-$2= 59 Mins,$5= 147 Mins,$10= 294 Mins. Speed Dial System";

		boolean send_p_sms = false;

		try
		{
			con = new ConnectDB();
			stmt = con.connect();


			qry = "Select count(*) from sms_inbox@skydb_muci_link where modem_id = 1 and status = 'N'";
			System.out.println(qry);
			rs = stmt.executeQuery(qry);
			while(rs.next())
			{
				count = rs.getInt(1);
			}

			if(count > 0)
			{

				qry ="Select id,mobile,REPLACE(REPLACE(msg, CHR(13), ' '), CHR(10), ' '),NVL(supplier_id,'-1') from sms_inbox@skydb_muci_link where modem_id = 1 and status = 'N'";
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

				qry ="Update sms_inbox@skydb_muci_link set status = 'P' where modem_id = 1 and status = 'N'";
				System.out.println(qry);
				stmt.executeUpdate(qry);

				if (aList!= null && aList.size() >0)
				{

					for(int h=0;h<aList.size();h++)
					{
						send_p_sms = false;
						pinFound = false;
						ignoreFound = false;
						destPhnNo = "";
						pin = "";
						msg = "";
						selectedSupplier="-1";
						prepostVal = "";
						prepost = "0";
						operator_id = "-1";
						theOpCode ="";
						india_op_id = "";
						clientTranId ="-1";
						supplier_id="-1";


						sec_supplier_id ="";
						max_topup_val = "";
						first_val = 0;
						second_val=0;

						aVal = (String[])aList.get(h);
						StringTokenizer st = new StringTokenizer(aVal[2], delims);
						if(st.hasMoreElements())
						{
							pin = (String)st.nextElement();
						}
						if(st.hasMoreElements())
						{
							destPhnNo = (String)st.nextElement();
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
						if(destPhnNo!=null) destPhnNo = destPhnNo.trim();
/***************************************************************************************************************************/
						qry ="Update sms_inbox@skydb_muci_link set destination = '"+destPhnNo+"',pin='"+pin+"',txn_processed='P' where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
						System.out.println(qry);
						stmt.executeUpdate(qry);

/********************* Check Ignore From and To Numbers **********************************/

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
							qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							msg = "Sorry Invalid destination number, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
							qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
								  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+",1)";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							continue;
						}

						qry = "Select tkleft, (select flexi_country_id from cardseries@skydb_muci_link where series = substr(substr(cardslno,instr(cardslno,'-')+1),0,instr(substr(cardslno,instr(cardslno,'-')+1),'-')-1)), (Select id from cardseries@skydb_muci_link where series = substr(substr(cardslno,instr(cardslno,'-')+1),0,instr(substr(cardslno,instr(cardslno,'-')+1),'-')-1)) from subscribers@skydb_muci_link where username='"+pin+"' and active='Y' and ls='N' and totaltk=tkleft";
						System.out.println(qry);
						rs = stmt.executeQuery(qry);

						while(rs.next())
						{
							country_id = rs.getString(2);
							Ramt = rs.getString(1);
							pinFound = true;
							series_id = rs.getString(3);
						}



						if(pinFound == false)
						{

							qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							retry_count=0;
							msg = "Sorry your pin is not valid for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";

							qry = "Select count(*) from subscribers@skydb_muci_link where username = '"+pin+"' and ls='Y'";
							System.out.println(qry);
							rs = stmt.executeQuery(qry);
							while(rs.next())
							{
								if(rs.getInt(1) >0)
								{
									msg = "Sorry this pin is already used. For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								}

							}

							qry = "Select NVL(retry_count,0) from false_retry@skydb_muci_link where retry_pin='"+pin+"' and callerid='"+aVal[1]+"'";
							System.out.println(qry);
							rs = stmt.executeQuery(qry);
							while(rs.next())
							{
								retry_count = rs.getInt(1);

							}
							if(retry_count <2)
							{
								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+",1)";
								System.out.println(qry);
								stmt.executeUpdate(qry);
							}


							if(retry_count>0)
							{
								qry ="Update false_retry@skydb_muci_link set retry_count = retry_count+1 where retry_pin='"+pin+"' and callerid='"+aVal[1]+"'";
								System.out.println(qry);
								stmt.executeUpdate(qry);

							}else
							{
								qry ="Insert into false_retry@skydb_muci_link(callerid, retry_count,retry_time,retry_pin) values "+
									" ('"+aVal[1]+"',1,sysdate,'"+pin+"')";
								System.out.println(qry);
								stmt.executeUpdate(qry);
							}

							continue;
						}

						qry = "Select name,country_code,(Select remarks from item@oms_link where id = currency_id) from flexi_country@skydb_muci_link where id = "+country_id+"";
						System.out.println(qry);
						rs = stmt.executeQuery(qry);

						while(rs.next())
						{
							country_name = rs.getString(1);
							countryCode = rs.getString(2);
							currencyName = rs.getString(3);
						}

						if(destPhnNo ==null)
						{
							System.out.println("destPhnNo:"+destPhnNo+":destPhnNo.length():"+destPhnNo.length());
							msg="Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
							System.out.print("Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

							qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
								  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+",1)";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
							System.out.println(qry);
							stmt.executeUpdate(qry);
							continue;

						}
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
								msg="Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								System.out.print("Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+",1)";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
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
								qry ="Update sms_inbox@skydb_muci_link set destination = '"+destPhnNo+"',pin='"+pin+"',txn_processed='P' where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);
							}

							if(destPhnNo!=null && destPhnNo.length() != 12)
							{
								msg="Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								System.out.print("Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+",1)";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);
								continue;
							}

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

								msg="AAARECHARGE:Sorry Operator not supported, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								System.out.print("Sorry Operator not supported, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", 1)";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);
								continue;

							}else
							{
								qry = "Select NVL(flexi_operator_id,'-1') from operator_codes@skydb_muci_link where id = "+india_op_id;
								System.out.println(qry);
								rs = stmt.executeQuery(qry);

								while(rs.next())
								{
									operator_id = rs.getString(1);
								}

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
									qry = "Select NVL(supplier_id,'-1') from flexi_operator@skydb_muci_link where  id = "+operator_id+"";
									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while(rs.next())
									{
										supplier_id = rs.getString(1);
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

/******************************** Hard Coded Supplier ID ***************************************************************/

							//supplier_id = "8";

/******************************** Hard Coded Supplier ID ENDS ***************************************************************/
							/******************************** For EGEN ****************************************/
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

									msg="AAARECHARGE:Sorry Operator not supported, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry Operator not supported, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", 1)";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
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
							/******************************** For EGEN Ends ****************************************/
							/******************************** For GET API ****************************************/
							if(supplier_id != null && supplier_id.equals("24"))
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

									msg="AAARECHARGE:Sorry Operator not supported, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									System.out.print("Sorry Operator not supported, for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+", 1)";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);
									continue;

								}else
								{
									qry = "Select op_code_getapi from operator_codes@skydb_muci_link where id = "+india_op_id;
									System.out.println(qry);
									rs = stmt.executeQuery(qry);

									while(rs.next())
									{
										theOpCode = rs.getString(1);
									}
									qry = "Select getapi_id.nextval@skydb_muci_link from dual";
									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while (rs.next())
									{
										clientTranId = rs.getString(1);
									}


								}
							}
							/******************************** For GET API Ends ****************************************/



						}

						qry = "Select NVL(time_diff,0) from flexi_country@skydb_muci_link where id ="+country_id+" ";
						rs = stmt.executeQuery(qry);
						while(rs.next())
						{
							minDiff = rs.getDouble(1);
						}

						/********** CHECK MINIMUM TIMEDIFF *****************/
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
							msg = "This request can not be processed now. Please try after "+timeDiff+" minute";
							System.out.print("<center><font color=red size=4><b>This request can not be processed now. Please try after "+timeDiff+" minute</b></font>");

							qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
								  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+",1)";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
							System.out.println(qry);
							stmt.executeUpdate(qry);
							continue;
						}

				/********** CHECK AMOUNT - SERIES - COMMISSION ******************************/

						if(Ramt != null && Ramt.length()>0)
						{
							qry = "Select count(*) from sms_ratechart@skydb_muci_link where seriesid="+series_id+" and country_id="+country_id+" and sale_denomination="+Ramt+"";
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
								theCount = rs.getInt(1);
							}

							if(theCount == 1)
							{
								qry = "Select seriesid,purchase_denomination from sms_ratechart@skydb_muci_link where seriesid="+series_id+" and country_id="+country_id+" and sale_denomination="+Ramt;
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
									series_id = rs.getString(1);
									amountToRecharge = rs.getString(2);
								}
								localAmount = Ramt;
								variableAmt = "N";

							} else if(theCount > 1)
							{
								System.out.println("<font color=\"RED\" size=4>Invalid Tariff for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]</font>");
								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								msg = "Invalid Tariff for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+",1)";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								continue;
							} else if(theCount == 0)
							{

								qry = "Select count(*) from sms_ratechart@skydb_muci_link where seriesid in (Select id from cardseries@skydb_muci_link where is_default = 'Y') and country_id="+country_id+" and sale_denomination="+Ramt+"";
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
									theCount = rs.getInt(1);
								}
								if(theCount == 0)
								{
									System.out.println("<font color=\"RED\" size=4>Invalid Tariff for assistance call Customer Support "+CC_NUMBER+" [11am-11pm].</font>");
									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									msg = "Invalid Tariff for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+",1)";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									continue;
								}
								if(theCount > 1)
								{
									System.out.println("<font color=\"RED\" size=4>Invalid Tariff for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]</font>");
									qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									msg = "Invalid Tariff for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
									qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
										  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+",1)";
									System.out.println(qry);
									stmt.executeUpdate(qry);

									continue;
								}

								if(theCount == 1)
								{
									qry = "Select seriesid,purchase_denomination from sms_ratechart@skydb_muci_link where seriesid in (Select id from cardseries@skydb_muci_link where is_default = 'Y') and  country_id="+country_id+" and sale_denomination="+Ramt;
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
										series_id = rs.getString(1);
										amountToRecharge = rs.getString(2);
									}
									localAmount = Ramt;
									variableAmt = "N";
								}
							}
							System.out.println("theCount::"+theCount+"::series_id::"+series_id+"::localAmount::"+localAmount+"::amountToRecharge::"+amountToRecharge+"::variableAmt::"+variableAmt);

						}
						if(variableAmt != null && variableAmt.length()<1)
						{
							System.out.println("<font color=\"RED\" size=4>Invalid Tariff for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]</font>");
								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								msg = "Invalid Tariff for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+",1)";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								continue;
						}
						if(series_id != null && series_id.length()<1)
						{
							System.out.println("<font color=\"RED\" size=4>Invalid Tariff for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]</font>");
								qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								msg = "Invalid Tariff for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
									  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+",1)";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								continue;
						}


						String flx_id="";
						qry = "select flx_id.nextval from dual";
						System.out.println(qry);
						rs= stmt.executeQuery(qry);
						if(rs.next()) flx_id = rs.getString(1);

						String areaName="";

						areaName = "SMS-FLX-"+flx_id;

						int ret = -2;
						String succ ="";
						OracleCallableStatement ps = (OracleCallableStatement) stmt.getConnection().prepareCall( "BEGIN PROC_SMS_TOPUP_PIN@skydb_muci_link(?,?,?,?,?,?,?,?,?); END;" );
						ps.setString(1,pin);
						ps.setString(2,aVal[1]);
						ps.setString(3,destPhnNo);
						ps.setDouble(4,Double.parseDouble(localAmount));
						ps.setString(5,areaName);
						ps.registerOutParameter(6,OracleTypes.VARCHAR );
						ps.setDouble(7,Double.parseDouble(amountToRecharge));
						ps.setString(8,code);
						ps.setString(9,country_name);

						System.out.println("BEGIN PROC_SMS_TOPUP@skydb_muci_link('"+pin+"','+phone_no+','"+destPhnNo+"',"+amount+",'"+areaName+"',ret,"+amountToRecharge+","+code+","+country_name+") END;/");

						ps.execute();
						ret = ps.getInt(6);
						System.out.println(">>>>"+ret);
						String tmpDestPhone="";
						String txnid = "";
						String remarks="";
						String tmpStr="";
						String theSupp="";
						String urlStr="";
						int theCountt=0;
						int serviceCount=0;

						String delims1 = "|";
						String delims2 = ",";
						String retCode ="";
						String retText="";
						String supBal="";
						if(ret == 0)
						{

							if(destPhnNo!=null && destPhnNo.indexOf("880") ==0)
							{
								tmpDestPhone= destPhnNo.substring(2);
							}else
							{
								tmpDestPhone = destPhnNo;
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
									qry = "Select NVL(supplier_id,'-1') from flexi_operator@skydb_muci_link where  id = "+operator_id+"";
									System.out.println(qry);
									rs = stmt.executeQuery(qry);
									while(rs.next())
									{
										supplier_id = rs.getString(1);
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

/******************************** Hard Coded Supplier ID ***************************************************************/
							if(country_id != null && country_id.equals("8"))
							{
								//supplier_id = "8";
							}

/******************************** Hard Coded Supplier ID ENDS ***************************************************************/


					/************************ Split amount from one21BD/eGen/getapi *************************/

							if(supplier_id != null && (supplier_id.equals("22") || supplier_id.equals("23") || supplier_id.equals("24")) )
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
									" topup_request_time,supp_id,me_id,inbox_id,pin,shop_comm,series_id,telco) "+
									" select id,msgid,src_ip,originator,prefix,dst_no,"+second_val+",orig_amount,history_id,topup_request_time + 30/(24*60),"+sec_supplier_id+","+
									" me_id,inbox_id,pin,shop_comm,series_id,telco from topup_request where msgid= '"+areaName+"'";
									System.out.println(qry);
									stmt.executeUpdate(qry);

								}
							}
					/************************ Split amount from one21BD/eGen ENDS *************************/




							qry = "Update topup_request set supp_id="+supplier_id+",egen_id="+clientTranId+",prepost="+prepost+" where msgid='"+areaName+"'";
							System.out.println(qry);
							stmt.executeUpdate(qry);


							qry = "Select url from flexi_supplier@skydb_muci_link where id="+supplier_id+"";
							System.out.println(qry);
							rs = stmt.executeQuery(qry);
							while(rs.next())
							{
								urlStr = rs.getString(1)+"?";
							}


							qry = "Select b.param_name,a.param_name,a.value,a.param_def_id from flexi_supplier_param@skydb_muci_link a, flexi_param_def@skydb_muci_link b where a.param_def_id=b.param_id and a.flexi_supplier_id="+supplier_id;
							System.out.println(qry);
							rs = stmt.executeQuery(qry);
							while(rs.next())
							{
								theSupp = rs.getString(1);

								if(rs.getString(1) != null && (rs.getString(1).equals("LOGIN") || rs.getString(1).equals("PASS")))
								{
									if(theCountt >0) urlStr +="&";
									urlStr += rs.getString(2)+"="+rs.getString(3);
								} else if(rs.getString(1) != null && rs.getString(1).equals("MOBILE"))
								{
									if(theCountt >0) urlStr +="&";
									if(supplier_id != null && (supplier_id.equals("23") || supplier_id.equals("24")))
									{
										urlStr += rs.getString(2)+"="+destPhnNo.substring(2);
									}else
									{
										urlStr += rs.getString(2)+"="+tmpDestPhone;
									}
								} else if(rs.getString(1) != null && rs.getString(1).equals("AMOUNT"))
								{
									if(theCountt >0) urlStr +="&";
									if(supplier_id != null && (supplier_id.equals("22") || supplier_id.equals("23") || supplier_id.equals("24")))
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
							}

							System.out.println(">>"+urlStr);

							String responseStr = "";

							if(urlStr!=null && urlStr.indexOf("bdapi.globaltopup.me")>=0)
							{
								urlStr += "&mtid="+areaName;
								System.out.println(">>"+urlStr);
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
							}else if(urlStr!=null && urlStr.indexOf("www.getapi.in")>=0)
							{
								urlStr += "&keyword="+theOpCode;
								urlStr += "&utid="+clientTranId;
								System.out.println(">>"+urlStr);
								responseStr = URLReader.readURL(new URL(urlStr)) ;
							}else if(urlStr!=null && urlStr.indexOf("hflexi")>=0)
							{
								System.out.println("hflexi Called");
							}else
							{
								try
								{
									responseStr = URLReader.readURL(new URL(urlStr)) ;
								}catch(Exception e)
								{
									System.out.println("HTTP API:URL Read Error"+e.getLocalizedMessage());
									continue;
								}
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
							}
							else if(urlStr!=null && urlStr.indexOf("topupcard.sg")>=0)
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

							}else if(urlStr!=null && urlStr.indexOf("www.getapi.in")>=0)
							{
								StringTokenizer st3 = new StringTokenizer(responseStr, delims2);

								if(st3.hasMoreElements())
								{
									//retCode = (String)st3.nextElement();
									txnid = (String)st3.nextElement();
								}

								if(st3.hasMoreElements())
								{
									retText = (String)st3.nextElement();
								}
								if(st3.hasMoreElements())
								{
									retText = (String)st3.nextElement();
								}

								if(st3.hasMoreElements())
								{
									retText = (String)st3.nextElement();
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

							}else if(urlStr!=null && urlStr.indexOf("www.getapi.in")>=0)
							{
								if(remarks!=null && remarks.indexOf("SUCCESS")>=0)
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
								send_p_sms=true;
								if(urlStr!=null && (urlStr.indexOf("bdapi.globaltopup.me")>=0 || urlStr.indexOf("hflexi")>=0))
								{
									msg = "Recharge to "+destPhnNo+" successfully completed by "+amountToRecharge+" "+currencyName+". Transaction no-"+aVal[0]+"";
								}else
								{
									msg = "Recharge to "+destPhnNo+" successfully completed by "+amountToRecharge+" "+currencyName+". Transaction no-"+txnid+"";
								}
							}else if (tmpStr != null && tmpStr.equals("N"))
							{
								qry = "update subscribers@skydb_muci_link set tkleft = totaltk, ls = 'N' where username = '"+pin+"'";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								qry = "update subscribers_cards@skydb_muci_link set ls='N' where username = '"+pin+"'";
								System.out.println(qry);
								stmt.executeUpdate(qry);

								if(remarks!=null && remarks.indexOf("Invalid phonenumber")>=0)
								{
									msg="Sorry Invalid mobile number for assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
								}else if(msg!=null && msg.length()<1)
								{
									msg = "Mobile "+tmpDestPhone+" RECHARGE FAILED FOR Amount"+amountToRecharge+"";
								}
							}


							qry = "Update topup_request set processed='"+tmpStr+"',transaction_id = '"+txnid+"',topup_process_time = sysdate, response='"+remarks+"', inbox_id= "+aVal[0]+",pin="+pin+" where msgid = '"+areaName+"'";

							System.out.println(qry);
							stmt.executeUpdate(qry);

							qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+msg+"',"+aVal[0]+",1)";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							qry ="Update sms_inbox@skydb_muci_link set txn_processed='"+tmpStr+"' where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
							System.out.println(qry);
							stmt.executeUpdate(qry);

							/***
							if(send_p_sms == true)
							{
								qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+aVal[1]+"','"+p_sms+"',"+aVal[0]+",1)";
								System.out.println(qry);
								stmt.executeUpdate(qry);

							}
							******/

						}
/**************************************************************************************************************************/
						System.out.println("pin::"+pin+"::destPhnNo::"+destPhnNo);
						qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = 1 and status = 'P' and id="+aVal[0]+"";
						System.out.println(qry);
						stmt.executeUpdate(qry);
						stmt.getConnection().commit();
					}

				}

				stmt.getConnection().commit();
			}else
			{
				System.out.println("No sms found");
			}
			stmt.getConnection().commit();

		} catch (SQLException localSQLException1)
		{
			System.out.print("eTopupSMS Job failed: " + localSQLException1.getMessage());
			try
			{
				if(stmt != null) stmt.getConnection().rollback();
			}catch(Exception e)
			{}
		} catch (Exception localException)
		{
			System.out.print("eTopupSMS Job failed: " + localException.getMessage());
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