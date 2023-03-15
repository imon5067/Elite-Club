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


public class APIACC
{

	private String phoneNo="";
	private String topup_amount="";
	private String topup_type="";
	private String theOriginator ="";
	private String theMe_id ="";

	public APIACC()
	{

	}

	public APIACC(String aOriginator, String aMe_id, String aPhone_no, String aTopup_amount,String aTopup_type)
	{
		this.theOriginator = aOriginator;
		this.theMe_id = aMe_id;
		this.phoneNo = aPhone_no;
		this.topup_amount = aTopup_amount;
		this.topup_type = aTopup_type;
	}

	public String process()
	{

		//Oracle Variables
		ConnectDB con = null;
		java.sql.Statement stmt = null;
		ResultSet rs = null;
		String qry = "";

		String modem_id = "101";
		// List
		DecimalFormat twoDigit = new DecimalFormat("#,##,##0.00");
		// SMS Variables
		String mobile = "";
		String msg = "";
		String destPhnNo = this.phoneNo;
		String amount = this.topup_amount;
		String Ramt = "";
		String serviceCharge = "";

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
		String me_id=this.theMe_id;
		String originator = this.theOriginator;
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
		String bKashSeriesId = "872814";
		String CC_NUMBER = "84455516";

		String pack_prefix = "";
		String firstName = "";
		String lastName = "";
		String[] temp = null ;
		String rpId = "";
		String succ ="";
		String pinNum="";
		String phone_no= "";
		String flx_id="";
		String areaName="";
		String p_area_id = "11";
		int ret = -2;

		String tmpDestPhone="";

		double recvAmount = 0.0;
		String recvCurrency ="";
		double crLimit=0.0;
		String ttype = this.topup_type;
		String theInboxId ="";

		try
		{
			con = new ConnectDB();
			stmt = con.connect();

/**************************** package/uc_user_id **********************/
			qry ="Select uc_user_id from emp where me_id="+me_id+"";
			System.out.println(qry);
			rs= stmt.executeQuery(qry);
			while(rs.next())
			{
				ucUserId = rs.getString(1);
			}

			if(ucUserId!=null && ucUserId.equals("9"))
			{
				airTimeRateplan_id = "872818";
				maxPackId = "872816";
				p_area_id = "2";
				bKashSeriesId = "872820";

			}
			if(ucUserId!=null && ucUserId.equals("10"))
			{
				airTimeRateplan_id = "887306";
				maxPackId = "880164";
				p_area_id = "3";
				bKashSeriesId ="894652";

			}

			/************* Change Status of Grabbed SMS ****************/
			//qry ="Update sms_inbox@skydb_muci_link set status = 'P' where modem_id = "+modem_id+" and status = 'N'";

			qry = "Select sms_inbox_sq.nextval@skydb_muci_link from dual";
			System.out.println(qry);
			rs= stmt.executeQuery(qry);
			while(rs.next())
			{
				theInboxId = rs.getString(1);
			}

			if(ttype != null && ttype.equals("0"))
			{
				qry = "Insert into mastertelusa.sms_inbox@skydb_muci_link (id, entry_date, mobile,msg,destination, status,modem_id,me_id) values " +
				" ( "+theInboxId+", sysdate, '"+originator+"','"+destPhnNo+".BK"+amount+"','"+destPhnNo+"','P',"+modem_id+","+me_id+")";
			}else if(ttype != null && ttype.equals("1"))
			{
				qry = "Insert into mastertelusa.sms_inbox@skydb_muci_link (id, entry_date, mobile,msg,destination, status,modem_id,me_id) values " +
				" ( "+theInboxId+", sysdate, '"+originator+"','"+destPhnNo+".AK"+amount+"','"+destPhnNo+"','P',"+modem_id+","+me_id+")";
			}else
			{
				msg="AAARECHARGE:Sorry Invalid bKash Request Type. For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
				System.out.print("Sorry Invalid mobile number invalid. For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

				qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
					  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+originator+"','"+msg+"',"+theInboxId+","+modem_id+")";
				System.out.println(qry);
				stmt.executeUpdate(qry);

				qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+theInboxId+"";
				System.out.println(qry);
				stmt.executeUpdate(qry);
				return msg;

			}
			System.out.println(qry);
			stmt.executeUpdate(qry);

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


			seriesId = ""+bKashSeriesId;
			operatorRate = false;
			qryCond += " And seriesid="+seriesId+" ";

			/***********  Country Name, Currency, Code Resolve ************/
			country_id = "6";
			qry = "Select name,country_code,(Select remarks from item@oms_link where id = currency_id) from flexi_country@skydb_muci_link where id = "+country_id+"";
			System.out.println(qry);
			rs = stmt.executeQuery(qry);

			while(rs.next())
			{
				country_name = rs.getString(1);
				countryCode = rs.getString(2);
				currencyName = rs.getString(3);
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
					msg="AAARECHARGE:Sorry Invalid mobile number. For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
					System.out.print("Sorry Invalid mobile number invalid. For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

					qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
						  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+originator+"','"+msg+"',"+theInboxId+","+modem_id+")";
					System.out.println(qry);
					stmt.executeUpdate(qry);

					qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+theInboxId+"";
					System.out.println(qry);
					stmt.executeUpdate(qry);
					return msg;
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
					msg="AAARECHARGE:Sorry Invalid Rate Configuration. For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
					System.out.print("Sorry Invalid Rate !! For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

					qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
						  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+originator+"','"+msg+"',"+theInboxId+", "+modem_id+")";
					System.out.println(qry);
					stmt.executeUpdate(qry);

					qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+theInboxId+"";
					System.out.println(qry);
					stmt.executeUpdate(qry);
					return msg;
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
					msg="AAARECHARGE:Sorry Invalid Amount To Recharge. For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
					System.out.print("Sorry Amount To Recharge !! For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

					qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
						  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+originator+"','"+msg+"',"+theInboxId+", "+modem_id+")";
					System.out.println(qry);
					stmt.executeUpdate(qry);

					qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+theInboxId+"";
					System.out.println(qry);
					stmt.executeUpdate(qry);
					return msg;
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
					msg="AAARECHARGE:Sorry Invalid Amount To Recharge. For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
					System.out.print("Sorry Amount To Recharge !! For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

					qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
						  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+originator+"','"+msg+"',"+theInboxId+", "+modem_id+")";
					System.out.println(qry);
					stmt.executeUpdate(qry);

					qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+theInboxId+"";
					System.out.println(qry);
					stmt.executeUpdate(qry);
					return msg;
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
					msg="AAARECHARGE:orry Invalid Amount To Recharge. For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
					System.out.print("Sorry Amount To Recharge !! For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

					qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
						  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+originator+"','"+msg+"',"+theInboxId+", "+modem_id+")";
					System.out.println(qry);
					stmt.executeUpdate(qry);

					qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+theInboxId+"";
					System.out.println(qry);
					stmt.executeUpdate(qry);
					return msg;
			}

			if(ucUserId!=null && ucUserId.equals("9"))
			{

				if(ttype!=null && ttype.equals("0"))
				{
					serviceCharge = "0";
				}
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



			/********************* Credit Limit Check **************************/
			System.out.println("::"+recvAmount*(-1)+Double.parseDouble(localAmount)*(1-Double.parseDouble(smsComm)/100)+"::"+crLimit);
			if(recvAmount*(-1)+Double.parseDouble(localAmount)*(1-Double.parseDouble(smsComm)/100)+ Double.parseDouble(serviceCharge)> crLimit)
			{
					qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id = "+modem_id+" and status = 'P' and id="+theInboxId+"";
					System.out.println(qry);
					stmt.executeUpdate(qry);

					msg = "AAARECHARGE:Sorry, You do not have enough balance. Your balance:"+twoDigit.format(recvAmount)+". For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
					qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
						  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+originator+"','"+msg+"',"+theInboxId+","+modem_id+")";
					System.out.println(qry);
					stmt.executeUpdate(qry);
					return msg;
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

			if(succ.indexOf("Successful")<0)
			{

				//throw new Exception("<font color=\"RED\" size=4>Error: "+succ+"</font>" );
				msg="AAARECHARGE:Sorry Recharge Failed. For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]";
				System.out.print("Recharge Failed From Procedure !! For assistance call Customer Support "+CC_NUMBER+" [11am-11pm]");

				qry = " Insert into sms_outbox@skydb_muci_link (id, entry_date,mobile,msg,inbox_id,modem_id) values "+
					  " (sms_outbox_sq.nextval@skydb_muci_link,sysdate, '"+originator+"','"+msg+"',"+theInboxId+", "+modem_id+")";
				System.out.println(qry);
				stmt.executeUpdate(qry);

				qry ="Update sms_inbox@skydb_muci_link set status = 'Y',process_time=sysdate where modem_id =  "+modem_id+" and status = 'P' and id="+theInboxId+"";
				System.out.println(qry);
				stmt.executeUpdate(qry);
				return msg;

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
			if(ucUserId!=null && ucUserId.equals("9"))
			{
				add_m = "0";
			}

		if(ret == 0)
		{
				qry = "Update topup_request set me_id ="+me_id+", add_amount=amount*"+add_m+",topup_type="+ttype+",shop_comm="+smsComm+",series_id="+seriesId+", inbox_id= "+theInboxId+" where msgid='"+areaName+"'";
				System.out.println(qry);
				stmt.executeUpdate(qry);



		}

		stmt.getConnection().commit();
		return areaName;

		}catch (SQLException localSQLException1)
		{
			System.out.print("APIACC failed: " + localSQLException1.getMessage());
			try
			{
				if(stmt != null) stmt.getConnection().rollback();
			}catch(Exception e)
			{}
		} catch (Exception localException)
		{
			System.out.print("APIACC failed: " + localException.getMessage());
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
		return areaName;

	} // end function Porcess

}// End Class

