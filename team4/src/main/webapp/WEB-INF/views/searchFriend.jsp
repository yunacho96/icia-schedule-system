<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="resources/js/schedule.js"></script>
<link
	href="${pageContext.request.contextPath}/resources/css/searchFriend.css"
	type="text/css" rel="stylesheet" />
<title>Search Friend Page</title>
<script>
function sendSearchInfo(){
	let search1 = document.getElementsByName("search")[0].value;
	let clientData= {search:search1};
	postAjax('schedule/searchFriend',JSON.stringify(clientData), 'getSearchResult' ,'json');
	
	}
	
function getSearchResult(jsonD){
	console.log(jsonD);
	
	let input = document.getElementById("inputbox");
	input.style.display="none";
	
	if(jsonD.length == 0){
		let inputBox = "<div>검색단어를 포함한 아이디나 이름이 존재하지 않습니다.</div>"
		inputBox += "<input type='email' class='box' name='mail' placeholder='초대할 이메일을 입력하세요.'></>"
		inputBox += "<input type='button'  class='submit' value='이메일 전송' onClick='sendEmail()'>";
		input.innerHTML = inputBox;
		input.style.display="block";
	}
	
	
	let data="";
	for(var i=0; i<jsonD.length;i++){
		data +="<input type='checkbox' value='"+ jsonD[i].ucode +"' name='friend' class='memList'>" +" 아이디 : "+ jsonD[i].ucode 
					+ " |		 이름 : " + jsonD[i].uname + "  |   이메일 : "+ jsonD[i].umail+"</><br><br>";
	}
	if(jsonD.length !=0){
		data+= "<input type='button' class='submit' value='친구 추가' onClick='sendCheckedFriends()'/>";
	}
		
	let userlist = document.getElementById("userlist");
	userlist.innerHTML = data;
} 

function sendCheckedFriends(){
	let friend = document.getElementsByName("friend");
	let list = [];
	for(i=0;i<friend.length;i++){
		if(friend[i].checked){
			list.push({rmbId:friend[i].value});
		}
	}
	let clientData = JSON.stringify(list);
	
	postAjax('schedule/sendFriendInfo',clientData,'friendReqResult','json');
}
function friendReqResult(jsonData){
	alert(jsonData.message);
}

function sendEmail(){ //메일 을 서버로 보내는 펑션 
	let email = document.getElementsByName("mail")[0].value;
	let clientData = "to="+email;
	getAjax('schedule/sendEmail',clientData,'mailCheck');
}
function mailCheck(jsonData){
	alert(jsonData.message);
}

</script>
</head>

<body>
	<div id="accesszone">
		<input type="text"  name="search" class="box" placeholder="검색어를 입력하세요."/>
		<input type="button" class="submit" value="검색하기" onClick="sendSearchInfo()"/>
		<div id="userlist"></div>
		<div id="inputbox"></div>
	</div>
	
</body>
</html>