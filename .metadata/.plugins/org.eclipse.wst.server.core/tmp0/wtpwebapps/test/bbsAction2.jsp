	
	<%@page import="javax.security.auth.callback.ConfirmationCallback"%>

	<%@ page language="java" contentType="text/html; charset=UTF-8"

		pageEncoding="UTF-8"%>

	<%@ page import="java.io.PrintWriter"%>

	<%@ page import="bbs.BbsDAO"%>

	<%@ page import="bbs.Bbs"%>

	<%@ page import="java.util.ArrayList"%>
	
	

	<!DOCTYPE html>

	<html>

	<head>
	<body>
	
		<%
		PrintWriter script = response.getWriter();
			String[] sCNum=request.getParameterValues("subjects");
			int selectedCnum=0;
			
			for(String n:sCNum){
				try{
					selectedCnum=Integer.parseInt(n);
				}catch(Exception e){

			        script.println("<script>");
			        script.println("history.back()");
			        script.println("</script>");
				} 
			}
			
			session.setAttribute("sCNum", selectedCnum);
	        script.println("<script>");
	        script.println("location.href = 'bbs_stu.jsp'");
	        script.println("</script>");
		%>																																																												
	</body>

	</html>