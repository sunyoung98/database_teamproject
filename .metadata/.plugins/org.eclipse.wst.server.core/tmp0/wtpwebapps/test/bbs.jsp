	
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

			//로긴한사람이라면	 userID라는 변수에 해당 아이디가 담기고 그렇지 않으면 null값

			String userID = null;

			if (session.getAttribute("userID") != null) {

				userID = (String) session.getAttribute("userID");

			}

			int pageNumber = 1; //기본 페이지 넘버
	

			//페이지넘버값이 있을때

			if (request.getParameter("pageNumber") != null) {

				pageNumber = Integer.parseInt(request.getParameter("pageNumber"));

			}
			
			int selectedCNum=0;
			if (session.getAttribute("sCNum") != null) {

				selectedCNum = (Integer) session.getAttribute("sCNum");

			}


		%>

	

	

		<!-- 네비게이션  -->

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
					<li class="active"><a href="bbs.jsp">게시판</a></li>
					<li><a href="mail.jsp">Mail</a></li>
					<li><a href="sms.jsp">SMS</a></li>
	
				</ul>

	

	

				<%

					//라긴안된경우

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

		<!-- 게시판 -->
		<div>
			
			<form class= "qb" method="post" action="bbsAction.jsp">
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
		</div>
		
		
		
		<div class="container">
			

			<div class="row">

				<table class="table table-striped"

					style="text-align: center; border: 1px solid #dddddd">

					<thead>

						<tr>

							<th style="background-color: #eeeeee; text-align: center;">번호</th>

							<th style="background-color: #eeeeee; text-align: center;">제목</th>

							<th style="background-color: #eeeeee; text-align: center;">작성자</th>

							<th style="background-color: #eeeeee; text-align: center;">작성일</th>

						</tr>

					</thead>

					<tbody>

						<%

							BbsDAO bbsDAO = new BbsDAO();
							
							ArrayList<Bbs> list = bbsDAO.getList(pageNumber,selectedCNum);
							
							for (int i = 0; i < list.size(); i++) {

						%>
						<tr>
							
							
							<td><%=list.get(i).getBbsID()%></td>

							<td><a href="view.jsp?bbsID=<%=list.get(i).getBbsID()%>"><%=list.get(i).getBbsTitle()%></a></td>

							<td><%=list.get(i).getUserID()%></td>

							<td><%=list.get(i).getBbsDate().substring(0, 11) + list.get(i).getBbsDate().substring(11, 13) + "시"

							+ list.get(i).getBbsDate().substring(14, 16) + "분"%></td>

						</tr>

	

						<%

							}

						%>

					</tbody>

				</table>
				 <!-- 회원만넘어가도록 -->

				    <%

				       if(selectedCNum!=0){

				    %>

				    <a href="write.jsp" class="btn btn-primary pull-right">글쓰기</a>

				    <%

				       }

				    %>
			</div>

		</div>
		<!-- 애니매이션 담당 JQUERY -->
		<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
		<!-- 부트스트랩 JS  -->
		<script src="js/bootstrap.js"></script>

	

	</body>
