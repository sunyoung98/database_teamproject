	
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
			int eCNum = 0;

			if (session.getAttribute("eCNum") != null) {

				eCNum = (Integer) session.getAttribute("eCNum");

			}
			int dCNum = 0;

			if (session.getAttribute("dCNum") != null) {

				dCNum = (Integer) session.getAttribute("dCNum");

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

				<a class="navbar-brand" href="bbs_stu.jsp">MINI U-Campus 게시판</a>

			</div>

			<div class="collapse navbar-collapse"

				id="#bs-example-navbar-collapse-1">

				<ul class="nav navbar-nav">

					<li><a href="index.jsp">메인</a></li>

					<li><a href="bbs_stu.jsp">게시판</a></li>
					<li  class="active"><a href="enrollment.jsp">수강신청</a></li>

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
		<div class="container">
			
			<div class="row">
				
				
				<table class="table table-striped"
					
					style="text-align: center; border: 1px solid #dddddd">
					
					<thead>

						<tr>

							<th style="background-color: #eeeeee; text-align: center;">학기</th>
							<th style="background-color: #eeeeee; text-align: center;">과목 번호</th>
							<th style="background-color: #eeeeee; text-align: center;">과목 이름</th>
							<th style="background-color: #eeeeee; text-align: center;">교수 이름</th>
							<th style="background-color: #eeeeee; text-align: center;">과목 시간</th>
							<th style="background-color: #eeeeee; text-align: center;">강의실</th>
							<th style="background-color: #eeeeee; text-align: center;">수강 신청/삭제</th>

						</tr>

					</thead>
					<tbody >
						
						<%
							BbsDAO stu_subjects = new BbsDAO();
							ArrayList<Bbs> p = stu_subjects.enrollSubject();
							for (int i = 0; i < p.size(); i++) {

						%>
						<tr>
							
							<td >1학기</td>
							<td><%=p.get(i).getCNum() %></td>
							<td><%=p.get(i).getCName()%></td>
							<td ><%=p.get(i).getPname()%></td>
							<td><%=p.get(i).getCTime()%></td>	
							<td><%=p.get(i).getCRoom()%></td>
							
							<td>
								<form class="qb" method="post" action="enrollAction.jsp">
								<select name="enroll" >
									<option>선택 없음</option>
									<option value="<%="1_"+p.get(i).getCNum()%>"><%="수강 신청" %></option>
									<option value="<%="2_"+p.get(i).getCNum()%>"><%="수강 삭제" %></option>
								</select>
								<input type="submit" value="신청"/>
								</form>	
							</td>
						</tr>
						<%
							}
							
						%>
						<%
							if(eCNum!=0){
								stu_subjects.enrollSubject(userID, eCNum);	
							}
							if(dCNum!=0){
								stu_subjects.delectSubject(userID, dCNum);
							}
						%>
						
					</tbody>
					
				</table>
				
				
			</div>

		</div>
		
		
	
		<!-- 애니매이션 담당 JQUERY -->

		<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

		<!-- 부트스트랩 JS  -->

		<script src="js/bootstrap.js"></script>

	

	</body>

	</html>