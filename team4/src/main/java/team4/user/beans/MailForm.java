package team4.user.beans;

import lombok.Data;

@Data
public class MailForm {

	private String to;
	private String from;
	private String contents;
	private String subject;
}
