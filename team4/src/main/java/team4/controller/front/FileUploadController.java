package team4.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import team4.services.auth.ProjectUtils;
import team4.services.schedule.Schedule;
import team4.user.beans.ScheduleBean;
import team4.user.beans.UserBean;

@Controller
public class FileUploadController {

	@Autowired
	ProjectUtils pu;
	@Autowired
	Schedule sd;
	
	@PostMapping("/upload")
	public String upload(ModelAndView mav, @RequestParam("file1") MultipartFile file) {
		System.out.println("하이루 아하하하ㅏ ");
		System.out.println(file.getOriginalFilename());
		return "redirect:/";
	}
	
	
}
