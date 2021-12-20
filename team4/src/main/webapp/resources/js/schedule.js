let publicIp;
let pTecode;
let selDay;

function getTeCode(data){
	pTecode =data;
	alert(data);
}

function sendJoinInfo(){
	
	//name값을 가져온다. name객체를 가져온다.	
	const uCode = document.getElementsByName("uCode")[0];
	const aCode = document.getElementsByName("aCode")[0];
	const uName = document.getElementsByName("uName")[0];
	const uMail = document.getElementsByName("uMail")[0];
	const file = document.getElementsByName("mpFile")[0];
	
	
	//makeForm이라는 functiom을 불러오고 form에 저장해준다.
	let form = makeForm("Join","post");
	form.enctype="multipart/form-data";
	
	//form의 자식에 uCode,aCode,uName,uMail 저장
	
	form.appendChild(uCode);
	form.appendChild(aCode);
	form.appendChild(uName);
	form.appendChild(uMail);	
	form.appendChild(file);	
	document.body.appendChild(form);	
	form.submit();	
}


function sendLogoutInfo(){
	
	const uCode = document.getElementsByName("uCode")[0];	
	const pubIp = makeInput("hidden", "publicIp", publicIp);
	const priIp = makeInput("hidden", "privateIp", location.host);
	const method = makeInput("hidden","method", -1 );
	const browser = makeInput("hidden", "browser", navigator.userAgent);
	
	let form = makeForm("Logout","post");
	
	
	form.appendChild(uCode);
	form.appendChild(pubIp);
	form.appendChild(priIp);
	form.appendChild(method);
	form.appendChild(browser);
	
	document.body.appendChild(form);
	
	form.submit();
	
	
}

function sendAccessInfo(){
	
	const uCode = document.getElementsByName("uCode")[0];
	const aCode = document.getElementsByName("aCode")[0];
	const method = makeInput("hidden", "method", 1);
   const pubIp = makeInput("hidden", "publicIp", publicIp);
   const priIp = makeInput("hidden", "privateIp", location.host);	
	const browser = makeInput("hidden", "browser", navigator.userAgent);

	//name,age는 가운데 "name"값이 같아서 ArrayList로 넘김.
	
	
	let form = makeForm("Access","post");
	

	form.appendChild(uCode);
	form.appendChild(aCode);
	form.appendChild(method);
    form.appendChild(pubIp);
    form.appendChild(priIp);
	form.appendChild(browser);
	
	document.body.appendChild(form);
	form.submit();

}

//form을 만드는 method
function makeForm(action, method, name=null){
	
	let form = document.createElement("form");
	if(name != null){
		form.setAttribute("name",name);}		
		form.setAttribute("action",action);
		form.setAttribute("method", method);	
	return form;
	}

function makeInput(type, name, value){
	//input 객체생성
	
	let input = document.createElement("input");
	
	//
	
	input.setAttribute("type", type);
	input.setAttribute("name", name);
	input.setAttribute("value", value);
	
	return input;
	//type,name,value
}

function getAjax(jobCode, clientData, fn){
	
   //Step 1
   let ajax = new XMLHttpRequest();
   
   //Step 2
   ajax.onreadystatechange = function(){
      if(ajax.readyState == 4 && ajax.status == 200){
         //Step 5		
		
		 jsonData = JSON.parse(ajax.responseText);
         window[fn](jsonData);
      }
   };
   //https://api.ipify.org?format=json
   //Step 3                      클라이언트 입력값
   if(clientData !=""){jobCode += "?" + clientData; }
   ajax.open("GET", jobCode);
   //Step 4
   //ajax.setRequestHeader("content-type", "application/x-www-form-urlencoded")
   ajax.send();
   
}

function setPublicIp(data){
   publicIp = data.ip;
}


function postAjax(jobCode, clientData, fn, type){
   //Step 1
   let ajax = new XMLHttpRequest();
   
   //Step 2
   ajax.onreadystatechange = function(){
      if(ajax.readyState == 4 && ajax.status == 200){
         //Step 5
		jsonData = JSON.parse(ajax.responseText);
        window[fn](jsonData);
		
		}
   };
   //https://api.ipify.org?format=json
   //Step 3   +=   <-- 앞의변수에  뒤에 값을  더해줌.
  	ajax.open("POST", jobCode);
	if(type=="json"){
		ajax.setRequestHeader("Content-type","application/json;");
	}else{
		ajax.setRequestHeader("Content-type","application/x-www-form-urlencoded;");
	}

   //Step 4 form처럼 전송
  //ajax.setRequestHeader("content-type", "application/x-www-form-urlencoded");
  //	ajax.setRequestHeader("content-type", "application/json");
 
 	ajax.send(clientData);
}


function isValidateCheck(type, word){
	
	let result;
	const codeComp = /^[a-z]{1}[a-z|0-9]{3,11}$/g;
		
	const pwdComp1 = /[a-z]/g;
	const pwdComp2 = /[A-Z]/g;
	const pwdComp3 = /[0-9]/g;
	const pwdComp4 = /[!@#$%^&*]/g;
	
	if(type ==1){
		result= codeComp.test(word);
		} else if(type ==2){
			let count=0;
			
			count += pwdComp1.test(word)? 1: 0;
			count += pwdComp2.test(word)? 1: 0;
			count += pwdComp3.test(word)? 1: 0;
			count += pwdComp4.test(word)? 1: 0;
			result = (count>=3)? true: false;	
			
			}
		
			
		return result;			
}


function korCheck(obj){	
	const pattern =/^[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+$/;
	return pattern.test(obj);
}


function charCount(word, min, max){
	
return word.length>=min && word.length<max;
}


function scheduleList(){
	let form = makeForm("Schedule","get");
	document.body.appendChild(form);
	form.submit();
}



