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
import java.util.*;
import java.net.*;
import java.io.*;

public class ETopUp
  implements Job
{
  public void execute(JobExecutionContext paramJobExecutionContext)
    throws JobExecutionException
  {
    eTop();
  }

  public void eTop()
  {
    boolean bool = false;
	ConnectDB con = null;
	ConnectDB con1 = null;
    java.sql.Statement stmt = null;
    java.sql.Statement stmt1 = null;
    ResultSet rs = null;
    ResultSet rs1 = null;
    String qry = "";
    String loginId = "";
    String responseStr = "";
    String responseText = "";
	String delims = "|";
	String delims2 = ",";
	String retCode ="";
	String retText ="";
	String ret_errcode="";
	String pin="";

	String urlStr="";
	int theCountt = 0;
	String txnid ="";
	String india_op_id ="";
	String theOpCode="";
	String clientTranId = "";
	String remarks = "";
	String tmpStr ="";

	String[] aVal = null;
	ArrayList aList = new ArrayList();
	String msgids = "";

    try
    {
		con = new ConnectDB();
		con1 = new ConnectDB();
		stmt = con.connect();
		stmt1 = con1.connect();


/************************* ELOAD.SG *******************************/
/*
		String xmldata = "";
		qry ="Select msgid from topup_request where processed = 'Y' and checked is null and supp_id=21 and topup_request_time >= sysdate - 2";
		System.out.println(qry);
		rs = stmt.executeQuery(qry);
		while(rs.next())
		{
			responseStr = "";
			retText = "";
			retCode ="";
			ret_errcode="";

			xmldata = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""+
			" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
			"<soap:Body>"+
			"<CheckTransactionStatus xmlns=\"eloadsg\">"+
			"<sClientUserName>6591026822</sClientUserName>"+
			"<sClientPassword>59889864</sClientPassword>"+
			"<sClientTxID>"+rs.getString(1)+"</sClientTxID>"+
			"<sTransactionStatus></sTransactionStatus>"+
			"<sTransactionErrorCode></sTransactionErrorCode>"+
			"<sMTMsg></sMTMsg>"+
			"<sResponseID></sResponseID>"+
			"<sResponseStatus></sResponseStatus>"+
			"</CheckTransactionStatus>"+
			"</soap:Body>"+
			"</soap:Envelope>";

			URL url = new URL("http://api2.eload.sg/connect.asmx?WSDL");
			//URL url = new URL("http://180.210.201.43/connect.asmx?WSDL");

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Content-Length", String.valueOf(xmldata.length()));
			connection.setRequestProperty("Content-Type", "text/xml");
			connection.setRequestProperty("Connection", "Close");
			connection.setRequestProperty("SoapAction", "eloadsg/CheckTransactionStatus");
			connection.setDoOutput(true);

			PrintWriter pw = new PrintWriter(connection.getOutputStream());
			pw.write(xmldata);
			pw.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while((line = rd.readLine()) != null)
			{
				responseStr +=line;
			//	System.out.println(line);
			}
			System.out.println(""+rs.getString(1)+""+responseStr);
			if(responseStr != null)
			{
				if(responseStr.indexOf("<sTransactionStatus>") >=0 && responseStr.indexOf("</sTransactionStatus>")>=0)
					retCode = responseStr.substring(responseStr.indexOf("<sTransactionStatus>")+20,responseStr.indexOf("</sTransactionStatus>"));
				if(responseStr.indexOf("<sMTMsg>") >=0 && responseStr.indexOf("</sMTMsg>")>=0)
					retText = responseStr.substring(responseStr.indexOf("<sMTMsg>")+8,responseStr.indexOf("</sMTMsg>"));
				//ret_errcode = responseStr.substring(responseStr.indexOf("<sTransactionErrorCode>")+23,responseStr.indexOf("</sTransactionErrorCode>"));

				if(retCode!= null && (retCode.indexOf("SUCCESS")>=0 || retCode.indexOf("PROCESSED") >=0))
				{
					qry = "Update topup_request set checked='Y',response = '"+retText+"' where msgid='"+rs.getString(1)+"'";
					System.out.println(qry);
					stmt1.executeUpdate(qry);
				}else if (retCode != null && retCode.indexOf("REFUNDED") >=0)
				{
					pin = "-1";
					qry = "Select NVL((Select pin from sms_inbox@skydb_muci_link where id = (Select inbox_id from topup_request where msgid='"+rs.getString(1)+"' )),-1) from dual";
					rs1 = stmt1.executeQuery(qry);
					while(rs1.next())
					{
						pin = rs1.getString(1);
					}
					if(pin != null && !pin.equals("-1"))
					{
						qry = "update subscribers@skydb_muci_link set tkleft = totaltk, ls = 'N' where username = '"+pin+"'";
						System.out.println(qry);
						stmt1.executeUpdate(qry);

						qry = "update subscribers_cards@skydb_muci_link set ls='N' where username = '"+pin+"'";
						System.out.println(qry);
						stmt1.executeUpdate(qry);

						qry = "update sms_inbox@skydb_muci_link set txn_processed='N' where id = (Select inbox_id from topup_request where msgid='"+rs.getString(1)+"' )";
						System.out.println(qry);
						stmt1.executeUpdate(qry);

					}
					qry = "Update topup_request set processed='N',response = '"+retText+"',checked='Y' where msgid='"+rs.getString(1)+"'";
					System.out.println(qry);
					stmt1.executeUpdate(qry);
				}
			}

		}
*/
/******************* hflexi *************************/

		urlStr = "http://119.81.14.114:8080/users/flx_check.jsp?";
		qry = "Select b.param_name,a.param_name,a.value,a.param_def_id from flexi_supplier_param@skydb_muci_link a, flexi_param_def@skydb_muci_link b where a.param_def_id=b.param_id and a.flexi_supplier_id=26";
		System.out.println(qry);
		rs = stmt.executeQuery(qry);
		while(rs.next())
		{
			if(rs.getString(1) != null && (rs.getString(1).equals("LOGIN") || rs.getString(1).equals("PASS")))
			{
				if(theCountt >0) urlStr +="&";
				urlStr += rs.getString(2)+"="+rs.getString(3);
			}
			theCountt++;
		}
		qry ="Select * from (Select id, transaction_id from topup_request where processed = 'Y' and checked is null and supp_id=26 and transaction_id <> '-1' and topup_request_time >= sysdate - 2 and topup_request_time < sysdate - 1/(24*60) order by id desc) where rownum <101";

		System.out.println(qry);
		rs = stmt.executeQuery(qry);
		while(rs.next())
		{
			retCode ="";
			responseStr = rs.getString(2);
			if(responseStr!=null) responseStr = responseStr.trim();
			System.out.println(""+urlStr+"&trid="+responseStr+"");
			responseText = URLReader.readURL(new URL(""+urlStr+"&trid="+responseStr+""));
			//System.out.println(""+responseText);
			if(responseText!= null && responseText.indexOf("<TRID>") >=0)
			{
				retCode =responseText.substring(responseText.indexOf("<FLXID>")+7,responseText.indexOf("</FLXID>"));
			}

			if(retCode != null && !retCode.equals("-1") && !retCode.equals("") && retCode.length()>3)
			{
				qry = "Update topup_request set checked='Y',actual_txn_id='"+retCode+"' where id="+rs.getString(1)+"";
				System.out.println(qry);
				stmt1.executeUpdate(qry);
			}
		}
		stmt1.getConnection().commit();
		stmt.getConnection().commit();


/********************** GlobalEZ INDIA ****************************/
/*
		urlStr = "http://www.topupcard.sg/api20/topup.asmx/CheckStatus?";

		qry = "Select b.param_name,a.param_name,a.value,a.param_def_id from flexi_supplier_param@skydb_muci_link a, flexi_param_def@skydb_muci_link b where a.param_def_id=b.param_id and a.flexi_supplier_id=8";
		System.out.println(qry);
		rs = stmt.executeQuery(qry);
		while(rs.next())
		{
			if(rs.getString(1) != null && (rs.getString(1).equals("LOGIN") || rs.getString(1).equals("PASS")))
			{
				if(theCountt >0) urlStr +="&";
				urlStr += rs.getString(2)+"="+rs.getString(3);
			}
			theCountt++;
		}

		//responseStr = URLReader.readURL(new URL(urlStr)) ;

		stmt.getConnection().commit();

		qry ="Select id, transaction_id from topup_request where checked is null and supp_id=8 and transaction_id <> '-100' and topup_request_time >= sysdate - 5";
		System.out.println(qry);
		rs = stmt.executeQuery(qry);

		while(rs.next())
		{
			retText = "";
			retCode ="";
			responseStr = rs.getString(2);
			if(responseStr!=null) responseStr = responseStr.trim();
			System.out.println(""+urlStr+"&OrderID="+responseStr+"");
			responseText = URLReader.readURL(new URL(""+urlStr+"&OrderID="+responseStr+""));
			//System.out.println(""+responseText);
			if(responseText!= null && responseText.indexOf("<StatusCode>") >=0)
			{
				retCode =responseText.substring(responseText.indexOf("<StatusCode>")+12,responseText.indexOf("</StatusCode>"));
			}
			if(retCode != null && !retCode.equals("Q") && (responseText!= null && responseText.indexOf("<Remarks>") >=0))
			{
				retText =responseText.substring(responseText.indexOf("<Remarks>")+9,responseText.indexOf("</Remarks>"));
			}
			System.out.println("id::"+rs.getString(1)+":retCode:"+retCode+":retText:"+retText);
			if (retCode != null && retCode.equals("D"))
			{
				qry = "Update topup_request set checked='Y',response = '"+retText+"' where id="+rs.getString(1)+"";
				System.out.println(qry);
				stmt1.executeUpdate(qry);
			}else if (retCode != null && retCode.equals("F"))
			{
				pin = "-1";
				qry = "Select NVL((Select pin from sms_inbox@skydb_muci_link where id = (Select inbox_id from topup_request where id="+rs.getString(1)+" )),-1) from dual";
				rs1 = stmt1.executeQuery(qry);
				while(rs1.next())
				{
					pin = rs1.getString(1);
				}
				if(pin != null && !pin.equals("-1"))
				{
					qry = "update subscribers@skydb_muci_link set tkleft = totaltk, ls = 'N' where username = '"+pin+"'";
					System.out.println(qry);
					stmt1.executeUpdate(qry);

					qry = "update subscribers_cards@skydb_muci_link set ls='N' where username = '"+pin+"'";
					System.out.println(qry);
					stmt1.executeUpdate(qry);

					qry = "update sms_inbox@skydb_muci_link set txn_processed='N' where id = (Select inbox_id from topup_request where id="+rs.getString(1)+" )";
					System.out.println(qry);
					stmt1.executeUpdate(qry);

				}
				qry = "Update topup_request set processed='N',response = '"+retText+"',checked='Y' where id="+rs.getString(1)+"";
				System.out.println(qry);
				stmt1.executeUpdate(qry);
			}
			stmt1.getConnection().commit();
		}
		stmt1.getConnection().commit();
		stmt.getConnection().commit();
*/
/********************** ONE21 ****************************/
/*
		qry = "Select a.value from flexi_supplier_param@skydb_muci_link a, flexi_param_def@skydb_muci_link b where a.param_def_id=b.param_id and a.flexi_supplier_id= 11 and b.param_name='LOGIN'";
		System.out.println(qry);
		rs = stmt.executeQuery(qry);
		while(rs.next())
		{
			loginId = rs.getString(1);
		}

		stmt.getConnection().commit();

		qry ="Select id, transaction_id,msgid from topup_request where checked is null and supp_id=11 and topup_request_time >= sysdate - 10";
		System.out.println(qry);
		rs = stmt.executeQuery(qry);
		while(rs.next())
		{
			responseStr = rs.getString(2);
			if(responseStr!=null) responseStr = responseStr.trim();
			System.out.println("::http://bdapi.globaltopup.me/rapi/getstatus.aspx?id="+loginId+"&tid="+responseStr+"&mtid="+rs.getString(3)+"");
			responseText = URLReader.readURL(new URL("http://bdapi.globaltopup.me/rapi/getstatus.aspx?id="+loginId+"&tid="+responseStr+"&mtid="+rs.getString(3)+""));


			StringTokenizer st = new StringTokenizer(responseText, delims);
			retText = "";
			retCode ="";
			if(st.hasMoreElements())
			{
				retCode = (String)st.nextElement();
			}
			if(st.hasMoreElements())
			{
				retText = (String)st.nextElement();
			}

			if (retCode != null && retCode.equals("0"))
			{
				qry = "Update topup_request set checked='Y',response = '"+retText+"' where id="+rs.getString(1)+"";
				System.out.println(qry);
				stmt1.executeUpdate(qry);
			}else if (retCode != null && !retCode.equals("1"))
			{
				pin = "-1";
				qry = "Select NVL((Select pin from sms_inbox@skydb_muci_link where id = (Select inbox_id from topup_request where id="+rs.getString(1)+" )),-1) from dual";
				rs1 = stmt1.executeQuery(qry);
				while(rs1.next())
				{
					pin = rs1.getString(1);
				}
				if(pin != null && !pin.equals("-1"))
				{
					qry = "update subscribers@skydb_muci_link set tkleft = totaltk, ls = 'N' where username = '"+pin+"'";
					System.out.println(qry);
					stmt1.executeUpdate(qry);

					qry = "update subscribers_cards@skydb_muci_link set ls='N' where username = '"+pin+"'";
					System.out.println(qry);
					stmt1.executeUpdate(qry);

					qry = "update sms_inbox@skydb_muci_link set txn_processed='N' where id = (Select inbox_id from topup_request where id="+rs.getString(1)+" )";
					System.out.println(qry);
					stmt1.executeUpdate(qry);

				}
				qry = "Update topup_request set processed='N',response = '"+retText+"',checked='Y' where id="+rs.getString(1)+"";
				System.out.println(qry);
				stmt1.executeUpdate(qry);
			}

		}
		stmt1.getConnection().commit();
		stmt.getConnection().commit();

*/
/**************************** Secondary Topup *****************************************/
/*
		msgids = "";
		qry = "Select msgid, dst_no,amount,supp_id from topup_request_secondary where processed is null and sysdate > topup_request_time";
		System.out.println(qry);
		rs = stmt.executeQuery(qry);
		while(rs.next())
		{
			aVal = new String[]{rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)};
			aList.add(aVal);
			if(msgids != null && msgids.length() > 0) msgids +=",";
			msgids += "'"+rs.getString(1)+"'";
		}
		if(msgids != null && msgids.length() > 0)
		{
			qry = "Update topup_request_secondary set processed = 'P' where msgid in ("+msgids+")";
			System.out.println(qry);
			stmt.executeUpdate(qry);
		}

		if (aList!= null && aList.size() >0)
		{
			for(int h=0;h<aList.size();h++)
			{
				aVal = (String[])aList.get(h);
				theCountt = 0;
				qry = "Select url from flexi_supplier@skydb_muci_link where id="+aVal[3]+"";
				System.out.println(qry);
				rs = stmt.executeQuery(qry);
				while(rs.next())
				{
					urlStr = rs.getString(1)+"?";
				}

				qry = "Select b.param_name,a.param_name,a.value,a.param_def_id from flexi_supplier_param@skydb_muci_link a, flexi_param_def@skydb_muci_link b where a.param_def_id=b.param_id and a.flexi_supplier_id="+aVal[3]+"";
				System.out.println(qry);
				rs = stmt.executeQuery(qry);
				while(rs.next())
				{

					if(rs.getString(1) != null && (rs.getString(1).equals("LOGIN") || rs.getString(1).equals("PASS")))
					{
						if(theCountt >0) urlStr +="&";
						urlStr += rs.getString(2)+"="+rs.getString(3);
					} else if(rs.getString(1) != null && rs.getString(1).equals("MOBILE"))
					{
						if(theCountt >0) urlStr +="&";
						if(aVal[3] != null && (aVal[3].equals("23") || aVal[3].equals("24")))
						{
							urlStr += rs.getString(2)+"="+aVal[1].substring(2);
						}else
						{
							urlStr += rs.getString(2)+"="+aVal[1];
						}

					} else if(rs.getString(1) != null && rs.getString(1).equals("AMOUNT"))
					{
						if(theCountt >0) urlStr +="&";
						urlStr += rs.getString(2)+"="+aVal[2];
					} else if(rs.getString(1) != null && rs.getString(1).equals("EXTRA"))
					{
						if(rs.getString(2) != null && !rs.getString(2).equals("null"))
						{
							if(theCountt >0) urlStr +="&";
							urlStr += rs.getString(2)+"="+rs.getString(3);
						}
					}

					theCountt++;
				}


				if(urlStr!=null && urlStr.indexOf("inapi.globaltopup.me")>=0)
				{
					qry = "Select egen_id.nextval@skydb_muci_link from dual";
					System.out.println(qry);
					rs = stmt.executeQuery(qry);
					while (rs.next())
					{
						clientTranId = rs.getString(1);
					}

					qry = "Update topup_request_secondary set egen_id = "+clientTranId+" where msgid='"+aVal[0]+"'";
					System.out.println(qry);
					rs = stmt.executeQuery(qry);

					urlStr += "&mtid="+clientTranId;
					urlStr += "&provider=NA";
					System.out.println(">>"+urlStr);
					responseStr = URLReader.readURL(new URL(urlStr)) ;
				}else if(urlStr!=null && urlStr.indexOf("egensolutions.in")>=0)
				{


					qry = "Select NVL((select operator_id from (Select operator_id from (select max(length(code)) as aa,operator_id from "+
					" telco_codes@skydb_muci_link where "+
					" instr(upper('"+aVal[1]+"'), code) = 1 group by operator_id order by aa desc)	where rownum <2)),-1) from dual";
					System.out.println(qry);
					rs = stmt.executeQuery(qry);

					while(rs.next())
					{
						india_op_id = rs.getString(1);
					}

					if(india_op_id != null && india_op_id.equals("-1"))
					{
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

					qry = "Update topup_request_secondary set egen_id = "+clientTranId+" where msgid='"+aVal[0]+"'";
					System.out.println(qry);
					rs = stmt.executeQuery(qry);

					urlStr += "&operatorCode="+theOpCode;
					urlStr += "&clientTranId="+clientTranId;
					System.out.println(">>"+urlStr);
					responseStr = URLReader.readURL(new URL(urlStr)) ;
				}

				else if(urlStr!=null && urlStr.indexOf("www.getapi.in")>=0)
				{


					qry = "Select NVL((select operator_id from (Select operator_id from (select max(length(code)) as aa,operator_id from "+
					" telco_codes@skydb_muci_link where "+
					" instr(upper('"+aVal[1]+"'), code) = 1 group by operator_id order by aa desc)	where rownum <2)),-1) from dual";
					System.out.println(qry);
					rs = stmt.executeQuery(qry);

					while(rs.next())
					{
						india_op_id = rs.getString(1);
					}

					if(india_op_id != null && india_op_id.equals("-1"))
					{
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

					qry = "Update topup_request_secondary set egen_id = "+clientTranId+" where msgid='"+aVal[0]+"'";
					System.out.println(qry);
					rs = stmt.executeQuery(qry);

					urlStr += "&keyword="+theOpCode;
					urlStr += "&utid="+clientTranId;
					System.out.println(">>"+urlStr);
					responseStr = URLReader.readURL(new URL(urlStr)) ;
				}

				if(urlStr!=null && urlStr.indexOf("egensolutions.in")>=0)
				{
					retCode = responseStr.substring(responseStr.indexOf("<resultCode>")+12,responseStr.indexOf("</resultCode>"));
					retText = responseStr.substring(responseStr.indexOf("<resultValue>")+13,responseStr.indexOf("</resultValue>"));
					txnid = responseStr.substring(responseStr.indexOf("<operatorId>")+12,responseStr.indexOf("</operatorId>"));
					System.out.println("txnid::"+txnid);

					remarks = retText;
					System.out.println("remarks::"+remarks);


				}else if(urlStr!=null && urlStr.indexOf("inapi.globaltopup.me")>=0)
				{
					StringTokenizer st = new StringTokenizer(responseStr, delims);

					if(st.hasMoreElements())
					{
						retCode = (String)st.nextElement();
					}

					if(st.hasMoreElements())
					{
						retText = (String)st.nextElement();
					}
					if(st.hasMoreElements())
					{
						txnid = (String)st.nextElement();
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

				}

				if(urlStr!=null && urlStr.indexOf("inapi.globaltopup.me")>=0)
				{
					if(retCode!=null && (retCode.equals("1") || retCode.equals("0")) )
					{
						tmpStr = "Y";
					}else
					{
						tmpStr = "N";
					}

				}else if(urlStr!=null && urlStr.indexOf("www.getapi.in")>=0)
				{
					if(remarks!=null && remarks.indexOf("SUCCESS")>=0)
					{
						tmpStr = "Y";
					}else
					{
						tmpStr = "N";
					}

				}else if(urlStr!=null && urlStr.indexOf("egensolutions.in")>=0)
				{
					if(retCode!=null && (retCode.equals("121") || retCode.equals("122") || retCode.equals("125")) )
					{
						tmpStr = "Y";
					}else
					{
						tmpStr = "N";
					}

				}

				qry = "Update topup_request_secondary set processed='"+tmpStr+"',transaction_id = '"+txnid+"',topup_process_time = sysdate, response='"+remarks+"' where msgid = '"+aVal[0]+"'";

				System.out.println(qry);
				stmt.executeUpdate(qry);



			}
		}

*/











    }
    catch (SQLException localSQLException1)
    {
		try
		{
		  if(stmt1 != null) stmt1.getConnection().rollback();
		  if(stmt != null) stmt.getConnection().rollback();
		}catch(Exception e)
		{}
      System.out.print("eTopup Job failed: " + localSQLException1.getMessage());
    }
    catch (Exception localException)
    {
		try
		{
		  if(stmt1 != null) stmt1.getConnection().rollback();
		  if(stmt != null) stmt.getConnection().rollback();
		}catch(Exception e)
		{}
      System.out.print("eTopup Job failed: " + localException.getMessage());
    }
    finally
    {
      try
      {
        if (rs != null) rs.close();
        if (rs1 != null) rs1.close();
		if(stmt != null) stmt.close();
		if(stmt1 != null) stmt1.close();
		if(con !=null)	con.close();
		if(con1 !=null)	con1.close();
      }
      catch (SQLException localSQLException2)
      {
      }
    }
  }
}