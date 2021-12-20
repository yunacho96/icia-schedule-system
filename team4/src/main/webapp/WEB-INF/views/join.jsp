<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="${pageContext.request.contextPath}/resources/css/join.css"
	type="text/css" rel="stylesheet" />
<script src="resources/js/schedule.js"></script>
<title>Join Page</title>
<script>
/* 아이디 중복 검사 */
function dupCheck(obj){
	let uCode = document.getElementsByName("uCode")[0];
	let msg = document.getElementById("message");
	
	if(obj.value != "재입력"){
		// 아이디 유효성 검사
		if(!isValidateCheck(1, uCode.value)){
			uCode.value = "";
			uCode.focus();
			msg.innerText="아이디는 최소8~12자 이내로 입력해주세요.";
			return;
		}else{
			msg.innerText="";
		}
		/* AJAX 로 호출할 것 . form이 아니다. */
		postAjax("IsDup","uCode="+uCode.value, "afterDupCheck","form");
		
	}else{
		uCode.value = "";
		uCode.readOnly = false;
		uCode.focus();
		obj.value = "중복검사";	
	}
}
/* 도착하는 데이터 : {"message":"true"}*/
function afterDupCheck(jsonData){
	 //alert(jsonData);
	 let btn = document.getElementById("dubBtn");
	 let uCode = document.getElementsByName("uCode")[0];
	 let msg = document.getElementById("message");
	 
	 //사용 가능 
	 if(jsonData == true){ 
		 /* alert("사용 가능함 "); */
		 uCode.setAttribute("readOnly",true);
		 btn.setAttribute("value","재입력");
		 msg.innerText= "사용가능한 아이디";
	 }else{
		/* alert("사용 불가능함 "); */
		uCode.value="";
		uCode.focus(); // 이벤트는 setAttribute 주는거 아니다 
		msg.innerText= "사용불가능한 아이디";
	 }
}
function pwdCheck(obj){
	let msg = document.getElementById("pwdMsg");
	
	if(charCount(obj.value, 8, 20)){
		if(!isValidateCheck(2,obj.value)){
			msg.innerText="비밀번호는 영문대문자,소문자,특수문자,숫자 중 3가지 이상을 입력하세요."; 
			obj.focus();
		}else{
			msg.innerText="";
		}
	}else{
		msg.innerText="비밀번호는 8~20자 이내로 입력하세요.";
	}
	
}
function pwdDoubleCheck(){
	let msg = document.getElementById("pwdMsg2");
	let aCode = document.getElementsByName("aCode");
	if(aCode[0].value != aCode[1].value){
		msg.innerText="패스워드가 일치하지 않아요";
		aCode[1].value="";
		aCode[1].focus();
	}else{
		msg.innerText="";
	}
}
function nameCheck(obj){
	let nameCheck = document.getElementById("nameCheck");
	if(charCount(obj.value,2,5)){
		if(!korCheck(obj.value)){
			nameCheck.innerText="알파벳,숫자,공백없이 한글로 입력해주세요.";
			obj.value="";
		}else{
			nameCheck.innerText="";
		}
	}else{
		nameCheck.innerText="이름은 최소 2~5자 이내로 써주세요";
		obj.focus();
	}
}

function uploadFile(){
	let file1 = document.getElementsByName("mpFile")[0];
	let form = makeForm("upload","post");
	form.enctype="multipart/form-data";
	form.appendChild(file1);
	document.body.appendChild(form);
	form.submit();
}

</script>
</head>
<%-- <body onLoad = "callMessage('${message}')"> --%>

<body>
	<div id="accesszone">
		<div class="title">Join TAK</div>

		<div> 내 프로필 사진을 등록해보세요.  
			<input type="file" name="mpFile" />
			<button id="upload" onClick="uploadFile()">업로드</button>
		</div>
		 
		<div>
			<div id="uCode" class="item">아이디</div>
			<div class="content">
				<input type="text" name="uCode" class="box" />
			</div>
		</div>

		<div class="content">
			<div id="message"></div>
			<input type="button" id="dubBtn" class="button" value="중복검사" onClick="dupCheck(this)" />
		</div>

		<div>
			<div class="item">패스워드</div>
			<div id="pwdMsg"></div>
			<div class="content">
				<input type="password" name="aCode" class="box" onBlur="pwdCheck(this)" />
			</div>
		</div>
		<div>
			<div class="item">패스워드 재확인</div>
			<div id="pwdMsg2"></div>
			<div class="content">
				<input type="password" name="aCode" class="box" onBlur="pwdDoubleCheck()"/>
			</div>
		</div>
		<div>
			<div class="item">이름</div>
			<div id="nameCheck"></div>
			<div class="content">
				<input type="text" name="uName" class="box" onBlur="nameCheck(this)" />
			</div>
		</div>
		<div>
			<div class="item">이메일</div>
			<div class="content">
				<input type="email" name="uMail" class="box" />
			</div>
		</div>

		<div>
			<input type="button" class="button" value="회원가입" onClick="sendJoinInfo()">
		</div>
		<div style="text-align: center">
			<a href="/" style="text-decoration: none; color: #bbbbbb">로그인</a>
		</div>
	</div>

</body>
</html>