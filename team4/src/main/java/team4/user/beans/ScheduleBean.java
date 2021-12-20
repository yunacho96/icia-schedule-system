package team4.user.beans;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ScheduleBean {

	private String tecode;
	private String tename;
	private int num;
	private String mbid;
	private String mbname;
	private String title;
	private String date; // to_char 으로 가져오기 때문에 
	private String location;
	private String content;
	private String process;
	private String prname;
	private String open;
	private String opname;
	private String loop;
	private String loname;
	private String[] stickerPath;
	private String path;
	private MultipartFile[] mpFile;
}
