<%@ page language="java" import="javax.servlet.jsp.* ,
		java.sql.*, javax.servlet.http.*"
%>

<%
        session.invalidate();
        out.println("<b><center>Your session is closed</b></center>");

        out.print("<script>parent.location.replace(\"../index.html\")</script>");        
%>
