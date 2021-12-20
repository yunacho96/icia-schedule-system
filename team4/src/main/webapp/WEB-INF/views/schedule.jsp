<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Schedule page</title>
<link href="${pageContext.request.contextPath}/resources/css/schedule.css"
	type="text/css" rel="stylesheet" />
<script src="resources/js/schedule.js"></script>
</head>
<script>

var count =0;
function addFile(){
	count++;
	if(count>5){
		data = "";
	}else{
		let sendButton = document.getElementById("sendButton");
		let data1 ="<input type='button' value='전송' onClick='sendFiles()'>"
		sendButton.innerHTML = data1;
		
		let input = document.getElementById("pressAdd");
		let data = "<div class='sbox' name='delete'><br>"
		data += "<input type='file' name='mpFile' class='child' value='찾아보기' />";
		data += "<input type='button' value='삭제' class='child' onClick='deleteDiv(this)'> </div><br>";
		input.innerHTML += data;
	}	
	
}
function sendFiles(){
	let files = document.getElementsByName("mpFile");
	let form = makeForm("sendFiles","post");

	form.enctype="multipart/form-data";

	for(i=files.length-1 ;i>=0;i--){
		form.appendChild(files[i]);
		}
	document.body.appendChild(form);
	form.submit();
}

function deleteDiv(data){
	/* let div = document.getElementsByName("delete"); */
	/* div.style.display="none"; */
	data.parentNode.remove();
}
	
	
	
	
</script>
<body>

	<div id="box">
		<div class="sbox">
			<input type="button" class="child" value="추가" onClick="addFile()"/>
			<div id="sendButton" class="child"></div>
		</div>
		<div id="border">
			<div id="pressAdd">
			</div>
		</div>
		
		<div>
			<img src="/resources/images/${path}"
			style="width: 160px; height: auto; margin-bottom:30px;">
		</div>
	</div>

</body>
</html>