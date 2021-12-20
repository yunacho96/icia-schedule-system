package team4.controller.front;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import team4.services.auth.Authentication;
import team4.services.auth.Encryption;
import team4.services.auth.ProjectUtils;
import team4.services.schedule.Schedule;
import team4.services.share.Share;
import team4.user.beans.AccessInfo;
import team4.user.beans.ScheduleBean;
import team4.user.beans.TDetailsBean;
import team4.user.beans.TeamBean;
import team4.user.beans.UserBean;


@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private Schedule sd;
	
	@Autowired
	private Authentication auth;

	@Autowired
	private ProjectUtils pu;

	@Autowired
	private Encryption enc;
	
//	@Autowired
//	FileUploadService fileUploadService;

	private ModelAndView mav;

	@RequestMapping(value = "/", method = {RequestMethod.GET , RequestMethod.POST})
	public ModelAndView sendAccessForm(@ModelAttribute AccessInfo ai)  {
		return auth.rootCtl(ai);
	}
	@RequestMapping(value = "/JoinForm" )
	public String sendJoinForm() {
		return "join";
	}
	
	@PostMapping( "/Access" )
	public ModelAndView Access(@ModelAttribute AccessInfo ai) throws Exception {
	
		mav = auth.accessCtl(ai);
		return mav;
	}

	@PostMapping( "/Logout" )
	public ModelAndView LogOut(@ModelAttribute AccessInfo ai) throws Exception {
		mav = auth.accessOutCtl(ai);

		return mav;


	}
	@PostMapping( "/Join" )
	public ModelAndView memberJoin(@ModelAttribute UserBean ub) throws Exception {

		mav =auth.joinCtl(ub);
		return mav;

	}

	@PostMapping( "/IsDup")
	@ResponseBody	//responseBody Ajax로 return할 떄 page가 아닌 body의 일부분 데이터만 응답해야할 때. 프론트와 서버간의 업무를 철저히 분류
	public String isDuplicateCheck(@ModelAttribute AccessInfo ai) {
		System.out.println(auth.isDulicateIdCtl(ai));
		return auth.isDulicateIdCtl(ai);
	}
	
	
	@PostMapping("/team")
	public String team() {
		return "team";
	}
	
	@GetMapping("/Schedule")
	public String schedule() {
		return "calendar";
	}
	
	@GetMapping("/mailAuth")
	public String mailAuth(String teCode) {
		return "emailAuth";
	}
	@PostMapping("/mailAccept")
	public ModelAndView emailAuth(@ModelAttribute TDetailsBean db) {
		auth.emailAuth(db);
		return mav;
	}
	@PostMapping("/searchFriend")
	public String searchFriend() {
		return "searchFriend";
	}
	
	@PostMapping("/sendFiles")
	public ModelAndView sendFiles(@ModelAttribute UserBean ub) {
		
		
		mav = sd.sendFiles(ub);
//		System.out.println(ub.getMpFile().length +" : 길이셈 ");
//		
//		for(int i=0;i<ub.getMpFile().length;i++) {	
//			System.out.println(ub.getMpFile()[i].getOriginalFilename() + " :  확인용  :" +ub.getMpFile()[i].getContentType());
//		}
		return mav;
		
		//sd.sendFiles(ub);
	}
	
	@GetMapping("/test1")
	public String test1() {
		return "test";
	}
	
	
	@PostMapping("/insSchedule")
	public ModelAndView insSchedule(@ModelAttribute ScheduleBean sb) {
		System.out.println(sb);
		return sd.addSchedule(sb);
	}
	
	
	
	
}		
	
	
	

	/*@PostMapping( "/LogIn" )
	public String Login(@RequestParam("uCode") String uCode, @RequestParam("aCode") String aCode) {


		System.out.print(uCode + aCode);
		return "login";
	}*/


	/*@PostMapping( "/LogIn2" )
	public String Login2(@RequestParam("Code") ArrayList<String> list) {


		System.out.print(list.get(0) + list.get(1));
		return "login";
	}*/


	/*@PostMapping( "/LogIn3" )
	public String Login3(@ModelAttribute UserBean ub) {


		System.out.print(ub.getUCode() + ":" + ub.getACode());
		return "login";
	}
	 */

