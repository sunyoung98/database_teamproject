	<%@page import="javax.security.auth.callback.ConfirmationCallback"%>
	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ page import="java.io.PrintWriter"%>
	<%@ page import="java.util.ArrayList"%>
	<%@ page import="java.sql.Connection"%>
	<%@ page import="java.sql.ResultSet"%>
	<%@ page import="java.sql.Statement"%>
	<%@ page import="bbs.Bbs"%>
	<%@ page import="bbs.BbsDAO"%>
	
	<!DOCTYPE html>
	<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- 뷰포트 -->
	<meta name="viewport" content="width=device-width" initial-scale="1">
	<!-- 스타일시트 참조  -->
	<link rel="stylesheet" href="css/bootstrap.css">
	<title>MINI U-Campus</title>
	<style type="text/css">
		a, a:hover {
			color: #000000;
			text-decoration: none;
		}
	</style>
	</head>
	<body>
		<%
			String userID = null;
			if (session.getAttribute("userID") != null) {
				userID = (String) session.getAttribute("userID");
			}
			int selectedCNum=0;
			if (session.getAttribute("sCNum") != null) {
				selectedCNum = (Integer) session.getAttribute("sCNum");
			}
		%>
		<nav class="navbar navbar-default">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="bs-example-navbar-collapse-1"
					aria-expaned="false">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="bbs.jsp">MINI U-Campus 게시판</a>
			</div>
			<div class="collapse navbar-collapse"
				id="#bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li><a href="bbs.jsp">게시판</a></li>
					<li class="active"><a href="mail.jsp">Mail</a></li>
					<li><a href="sms.jsp">SMS</a></li>
				</ul>
				<%
					if (userID == null) {
				%>
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">접속하기<span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="login.jsp">로그인</a></li>
							<li><a href="join.jsp">회원가입</a></li>
						</ul></li>
				</ul>
				<%
					} else {
				%>
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">회원관리<span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="logoutAction.jsp">로그아웃</a></li>
						</ul></li>
				</ul>
				<%
					}
				%>
			</div>
		</nav>
		<div>
		<form class= "qb" method="post" action="mailAction.jsp">
				<select name="subjects"  style=" margin-left:85px;margin-bottom: 10px; width:180px;" >
					<%
						
						BbsDAO s_name = new BbsDAO();
						ArrayList<Bbs> sname = s_name.getsubjectName(selectedCNum);
						
					%>
					<option style="background-color: #FFFFCC">
					<%=sname.get(0).getCName()+"("+sname.get(0).getCTime()+")"%></option>
					<%
						
						BbsDAO pro_subjects = new BbsDAO();
						ArrayList<Bbs> p = pro_subjects.getProSubjects(userID);
						for (int i = 0; i < p.size(); i++) {
					%>
					<!-- "${p.get(i).getCNum()}"로 value값 바꿔야해 -->
					<option value="<%= p.get(i).getCNum()%>">
					<%=p.get(i).getCName()+"("+p.get(i).getCTime()+")"%></option>
					<% 
						} 
					%>
				</select>
				<input type="submit" value="이동"/> 
				</form>
				<table class="table table-striped"

					style="text-align: center; border: 1px solid #dddddd">
				<form method="post" action="email_servlet/EmailController">
					<thead>

						<tr>
							
							<th style="background-color: #eeeeee; text-align: center;">아이디</th>
							<th style="background-color: #eeeeee; text-align: center;">전공</th>
							<th style="background-color: #eeeeee; text-align: center;">학년</th>
							<th style="background-color: #eeeeee; text-align: center;">성별</th>
							<th style="background-color: #eeeeee; text-align: center;">외국인여부</th>
							<th style="background-color: #eeeeee; text-align: center;">이메일주소</th>
							<th style="background-color: #eeeeee; text-align: center;">이메일 발송</th>

						</tr>

					</thead>

					<tbody>
						<%

							BbsDAO students = new BbsDAO();
							
							ArrayList<Bbs> ps=students.getstudentArray(userID, selectedCNum);
							
							for (int i = 0; i < ps.size(); i++) {

						%>
						<tr>
							
							<td><%=ps.get(i).getsId()%></td>
							<td><%=ps.get(i).getMajor()%></td>
							<td><%=ps.get(i).getGrade()%></td>
							<td><%=ps.get(i).getGender()%></td>
							<td><%=ps.get(i).getForeign()%></td>
							<td><%=ps.get(i).getEmail()%></td>
							<td><input name="email" type="checkbox" value="<%=ps.get(i).getEmail()%>"/></td>
						</tr>

						<%

							}

						%>

					</tbody>

				</table>
		</div>
		<div class="container">
			<div class="row">
					<table>
						<tr>

							<td><input type="text" class="form-control" placeholder="subject" name="subject" maxlength="50"/></td>

						</tr>
						<tr>

							<td><textarea class="form-control" placeholder="message" name="message" maxlength="2048" style="height: 350px;"></textarea></td>

						</tr>
					</table>
					<input type="submit" name="btnSendMail" value="Send" />
				</form>

			</div>
		</div>
		<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
		<script src="js/bootstrap.js"></script>
		<script type="text/javascript">
</script>
	</body>
	</html>