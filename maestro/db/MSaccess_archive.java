package com.maestro.db;

import java.sql.*;

public class MSaccess_archive {
    public static void main(String[] args) {

        try {

            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

            String accessFileName = "C:/Program Files (x86)/ZKTeco/att2000.mdb";

            String connURL="jdbc:odbc:DRIVER={Microsoft Access Driver (*.mdb, *.accdb)};DBQ="+accessFileName+";";

            Connection con = DriverManager.getConnection(connURL, "admin","");

            Statement stmt = con.createStatement();

            stmt.execute("select * from CHECKINOUT"); // execute query in table student

            ResultSet rs = stmt.getResultSet(); // get any Result that came from our query

            if (rs != null)
             while ( rs.next() ){

                System.out.println("ID: " + rs.getString("userid") + " ID: "+rs.getString("checktime"));
                }

                stmt.close();
                con.close();
            }
            catch (Exception err) {
                System.out.println("ERROR: " + err);
            }
    }

}