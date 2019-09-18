	<%@page import="javax.security.auth.callback.ConfirmationCallback"%>
	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ page import="java.io.PrintWriter"%>
	<%@ page import="bbs.BbsDAO"%>
	<%@ page import="bbs.Bbs"%>
	<%@ page import="bbs.SendSMS"%>
	<%@ page import="java.util.ArrayList"%>
	<!DOCTYPE html>
	<html>
	<head>
	<body>
		<%
			PrintWriter script = response.getWriter();
		
			request.setCharacterEncoding("UTF-8");
			String content=request.getParameter("tbMailContent");
			System.out.println(content);
			String[] phones=request.getParameterValues("phones");
			
			try{
				SendSMS sms=new SendSMS(phones,content);

			}catch(Exception e){
				e.printStackTrace();
			}

			script.println("<script>");
			script.println("location.href = 'sms.jsp'");
			script.println("</script>");
		%>
	</body>
	</html>