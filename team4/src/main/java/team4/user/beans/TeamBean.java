package team4.user.beans;

import java.util.List;

import lombok.Data;

@Data 
//@NoargsConstructor 생성자가 없습니다. 
public class TeamBean {
	private String teCode;
	private String teName;
	private String mbId;
	private List<TDetailsBean> tdetails;
	private int index;
}
