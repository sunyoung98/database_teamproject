	
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
			String[] LCNum=request.getParameterValues("enroll");
			int selectedCnum=0;
			String s="";
			int num=0;
			String sub="";
			String str="";
			for(String n:LCNum){
				if(n!=null){
					sub=n.substring(0, 1);
					if(Integer.parseInt(sub)==1){
						num=1;
						str=n.substring(2);
						selectedCnum=Integer.parseInt(str);	
						session.setAttribute("eCNum", selectedCnum); 
						session.setAttribute("dCNum", 0); 
					}
					else if(Integer.parseInt(sub)==2){
						num=2;
						str=n.substring(2);
						selectedCnum=Integer.parseInt(str);	
						session.setAttribute("dCNum", selectedCnum); 
						session.setAttribute("eCNum",0); 
						
					}
				}
			}
			if(num==1){
				PrintWriter script = response.getWriter();
		        script.println("<script>");
		        script.println("alert('수강신청이 완료되었습니다')");
		        script.println("location.href = 'enrollment.jsp'");
		        script.println("</script>");	
			}
			else if(num==2){
				PrintWriter script = response.getWriter();
		        script.println("<script>");
		        script.println("alert('수강삭제가 완료되었습니다')");
		        script.println("location.href = 'enrollment.jsp'");
		        script.println("</script>");	
			}
			
			
		%>
	</body>

	</html>