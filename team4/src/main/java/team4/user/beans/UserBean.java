package team4.user.beans;


import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UserBean {

	private String uCode;
	private String aCode;
//	private ArrayList<String> info;
//	@DateTimeFormat(pattern="yyyy-MM-dd") // 패턴이 일치하는 것은 데이터형식으로 바꾸겠다는 의미
//	private Date day;
	private String uName;
	private String uMail;
	private String search;
	private String stickerPath;
	private MultipartFile[] mpFile;
}
