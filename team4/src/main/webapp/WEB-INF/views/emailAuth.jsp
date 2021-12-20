<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%-- href="${pageContext.request.contextPath}/resources/css/login.css" --%>
<link href="resources/css/login.css" type="text/css" rel="stylesheet" />
<script src="resources/js/schedule.js"></script>

<!-- <script type="application/javascript">
  function getIP(json) {
    document.write("My public IP address is: ", json.ip);
  }
</script> -->
<script>
 	
	function mailAuth() {
		
		const mbId = document.getElementsByName("mbId")[0];
		const teCode = document.getElementsByName("teCode")[0];
		let form = makeForm("mailAccept","post");
		form.appendChild(mbId);
		form.appendChild(teCode);
		document.body.appendChild(form);
		form.submit();
	}
</script>


<title>LogIn Page</title>
</head>

<%-- <body onLoad = "callMessage('${message}')"> --%>

<body >
	<div id="accesszone">
		
		<div class="title">TAK BUS 회원 인증 페이지</div>
		<div class="sTitle">schedule services</div>
		<div>
			<div>
				<input type="text" name="mbId" class="box" placeholder="아이디" />
			</div>
		</div>
		<div>
			<div>
				<input type="password" name="aCode" class="box" placeholder="비밀번호" />
			</div>
		</div>
		<div>
			<input type="button" class="submit" value="인증을 위한 로그인" onClick="mailAuth()">
			<input type="hidden" name="teCode" value="${param.teCode}"/>
		</div>
		<div style="text-align: center; color: #bbbbbb" class="found">
			아이디 찾기 | 비밀번호 찾기 | <a href="JoinForm" style="text-decoration: none; color: #bbbbbb">회원가입</a>
		</div>
		<!-- <input type="date" name="day"> -->
	</div>


</body>
</html>