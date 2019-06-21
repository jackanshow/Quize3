package com.sever;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Clock;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class randomQ
 */
@WebServlet("/randomQ")
public class randomQ extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		double times=Double.parseDouble(request.getParameter("times"));
		double range1=Double.parseDouble(request.getParameter("range1"));
		double range2=Double.parseDouble(request.getParameter("range2"));
		
		Connection conn = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      conn = DriverManager.getConnection("jdbc:sqlite::resource:db/cloudDB.db");
	      //conn = DriverManager.getConnection("jdbc:sqlite:"+"resource/db/xxwDB.db");
	      conn.setAutoCommit(false);
	      System.out.println("Opened database successfully");
	      
	      long timeBegin = Clock.systemDefaultZone().millis();
	      stmt = conn.createStatement();
	      String sql = "SELECT * FROM QUIZE3";
	      
	      
		   // 设置响应内容类型
	   	      response.setContentType("text/html");
	   		// 实际的逻辑是在这里
	   	      PrintWriter out = response.getWriter();
	   	      
	   	      out.println("<table border=\"1\">");
	   	      out.println("<tr><th>"+"LATITUDE"+"</th><th>"+"LONGITUDE"+
		        			 "<th>"+"TIME"+"</th>"
		        			 +"</tr>");
	   	      for(int i=0;i<times;i++) {
	   	    	  double min=Math.random()*(range2-range1)+range1;
	   	    	  double max=Math.random()*(range2-range1)+range1;
	   	    	  Max(max,min);
	   	    	out.println("<tr><th>"+i+"</th><th>"+" "+
	        			 "<th>"+" "+"</th>"
	        			 +"</tr>");
	   	    	ResultSet rs = stmt.executeQuery(sql);
	   	    	while ( rs.next() ) {
	   	         
	   	    		double depSql = Double.parseDouble("".equals(rs.getString("DEPTHERROR"))?"0.0":rs.getString("DEPTHERROR"));
	  	    	  
	  	    	  if(depSql<=max&&depSql>=min) {
	  	    		  out.println("<tr><td>"+rs.getString("LATITUDE")+"</td><td>"+rs.getString("LONGITUDE")+
	  		        			 "<td>"+rs.getString("TIME")+"</td>"+"</tr>");
	  	    	  }

	  	    	  
	  	    	rs.close();
	   	    	}
	   	      }
	      
	      out.println("</table>"); 	    
	      
	      
	      stmt.close();
	      conn.close();
	      long timeEnd = Clock.systemDefaultZone().millis();
	      out.println("<p>"+(timeEnd-timeBegin)+" milliseconds time expended to perform these queries.</p>");	     
	      out.close();
	    } 
	    catch(SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        }
	    catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    finally{
            // 最后是用于关闭资源的块
            try{
                if(stmt!=null)
                stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
	    System.out.println("Operation done successfully");
		
	}
	
	public void Max(double max,double min) {
		double temp = 0;
		if (max<min) {
			temp=max;
			max=min;
			min=temp;
		}
	}

}
