package team4.services.auth;


import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import team4.services.share.Share;
import team4.user.beans.AccessInfo;
import team4.user.beans.TDetailsBean;
import team4.user.beans.UserBean;


@Service	 //싱글톤으로 올라가있음//
public class Authentication {

	@Autowired
	Share sh;
	
	@Autowired
	AuthDAO dao;
	private ModelAndView mav= null;

	@Autowired
	Gson gson;
	@Autowired
	Encryption enc;
	@Autowired
	ProjectUtils pu;
	@Autowired
	SqlSessionTemplate sqlSession;


	//model=ai view= page)
	public ModelAndView accessCtl(AccessInfo ai) throws Exception {
		mav = new ModelAndView();
		String encPwd =null; 
		boolean check = true;
		
		//		if(pu.getAttribute("uCode")==null)

		if(pu.getAttribute("uCode") ==null) {

			if(dao.isAccess(ai)) {
				dao.forceLogOut(ai);
			}

			//암호화된 패스워드가 !null이면 (아이디가 존재)

			encPwd = dao.getPwdInfo(ai);

			if(check) {
				if(check= (encPwd != null)) {

					//매치시켜본다 원본pw와, encoding된 pwd
					if(check= enc.matches(ai.getACode(), encPwd)){
						if(check =dao.insAccessHistory(ai)) {
							pu.setAttribute("uName", enc.aesDecode(dao.selMemberInfo(ai).get(0).getUName(), ai.getUCode()));
							pu.setAttribute("uMail", enc.aesDecode(dao.selMemberInfo(ai).get(0).getUMail(), ai.getUCode()));
							pu.setAttribute("stickerPath", dao.selMemberInfo(ai).get(0).getStickerPath());
//							mav.addObject("uName", pu.getAttribute("uName"));
//							mav.addObject("uMail", pu.getAttribute("uMail"));
							
							
							mav.setViewName("dashboard");
							System.out.println("오여기셈 ");
							
							//Session이 만들어짐
							pu.setAttribute("uCode", ai.getUCode());
							//mav.setViewName("redirect:/");
							
							
//							mav.addObject("publicIp",ai.getPublicIp());
//							mav.addObject("privateIp",ai.getPrivateIp());
//							mav.addObject("browser",ai.getBrowser());
							
							//대쉬보드에 넘겨준 암호화된 uCode"
							System.out.println("로그인성공.");
						
							pu.setAttribute("publicIp",ai.getPublicIp());
							pu.setAttribute("privateIp",ai.getPrivateIp());
							pu.setAttribute("browser",ai.getBrowser());
						}
					}							
				}else {
					
				}
			} 
			else { System.out.println("로그인실패.");
			mav.addObject("message","아이디가 존재하지 않거나 아이디와 비밀번호가 일치하지 않습니다.");
			}
			mav.setViewName("redirect:/");
		}
		return mav;
	}


    /* 파일 업로드 작업 추가 */
	public ModelAndView joinCtl(UserBean ub) throws Exception {
		mav = new ModelAndView();
		mav.setViewName("join");
		mav.addObject("message", "다시 시도 해주세요.");

		//비밀번호를 암호화		
		ub.setACode(enc.encode(ub.getACode()));

		//UserBean에 Uname을 set(저장해준다)  Triple암호화 ( 암호화 할 항목, 암호화의 힌트가 될 항목) + 전부다 암호화되버리면 정보를 부르기 어려워짐

		ub.setUName(enc.aesEncode(ub.getUName(), ub.getUCode()));
		ub.setUMail(enc.aesEncode(ub.getUMail(), ub.getUCode()));
		
		ub.setStickerPath(pu.savingFile(ub.getMpFile()[0])); // 저장이 안되면 null로 들어감 
		
		if(dao.insMembers(ub)) {			
			mav.setViewName("login");
			mav.addObject("message","가입을 축하합니다. 로그인 하십죠");			
		}		
		return mav;
	}

	public String isDulicateIdCtl(AccessInfo ai) {
		boolean message = false;
		if(!dao.isUserId(ai)) {
			message=true;
		}	
		return gson.toJson(message); // {"message":"true"} 형태로 보내짐  

	}
	public ModelAndView accessOutCtl(AccessInfo ai) throws Exception {
		mav = new ModelAndView();		
		boolean check = false;
		//ai.setUCode(enc.aesDecode(ai.getUCode(), "LogOut"));	
		
		//로그인한 ID & privateIP가 동일? & SUM이 1 이라는 뜻은  로그인 되어 있는 상태고, BROWSER값이 다를때 로그아웃!

		//SQL에서 카운트써서 로그인한 ID&프로필 동일  썸(카운트)=1  이면 카운트 1./?ㅊㅇㄴㅍ'ㄴ유룵'ㅎ
		if(pu.getAttribute("uCode")!= null) {
			while(!check) {
				check=dao.insAccessHistory(ai);
			}
			pu.removeAttribute("uCode");
			mav.setViewName("redirect:/");
			mav.addObject("message","정상적..으로....로그아웃... 되었..습니다....");
		}else {
			mav.setViewName("redirect:/");
			mav.addObject("message","이미...! 로그아웃 ..되었습니다...");
		}
		return mav;
	}
	public ModelAndView rootCtl(AccessInfo ai)  {
		mav = new ModelAndView();
		
		try {
			System.out.println(pu.getAttribute("uCode")+"root");
			ai.setUCode((String)pu.getAttribute("uCode"));
			ai.setPrivateIp((String)pu.getAttribute("privateIp"));
			ai.setPublicIp((String)pu.getAttribute("publicIp"));
			ai.setBrowser((String)pu.getAttribute("browser"));
			
			if(pu.getAttribute("uCode")!=null) {	
				if(!dao.isCurrentAccess(ai)) {
					pu.removeAttribute("uCode");
					mav.setViewName("login");
					mav.addObject("message","세션 종료");
					
				}else {

					mav.setViewName("dashboard");			
					//변수이름=uCode, 변수대상!                                힌트값
//					mav.addObject("uCode", enc.aesEncode((String)pu.getAttribute("uCode"),"LogOut"));
//					mav.addObject("uMail",enc.aesDecode((String)pu.getAttribute("uMail"),(String)pu.getAttribute("uCode")));
//					mav.addObject("uName",enc.aesDecode((String)pu.getAttribute("uName"),(String)pu.getAttribute("uCode")));
//					
					mav.addObject("uCode",ai.getUCode());
					mav.addObject("publicIp",ai.getPublicIp());
					mav.addObject("privateIp",ai.getPrivateIp());
					mav.addObject("browser",ai.getBrowser());
				}	

			}else {
				mav.setViewName("login");
			}
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		
		//pu.removeAttribute("uCode");
		
		return mav;
	}



	public ModelAndView emailAuth(TDetailsBean db) {
		ModelAndView mav = new ModelAndView();
		
		db.setCgCode("G");
		
		System.out.println(db.getMbId()+ " : "+db.getTeCode()+ " : "+db.getCgCode());
		
		if(sh.insTeamDetailsTable(db)) {
			mav.setViewName("dashboard");
		}else {
			mav.setViewName("emailAuth");
		}
		
	
		return mav;
	}
}