package com.kh.spring.board.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 매개변수생성자
@Setter 
@Getter 
@ToString
public class Board {
	
	private int boardNo;
	private String boardTitle;
	private String boardWriter;
	private String boardContent;
	private String originName;
	private String changeName; // "resources/uploadFiles/xxxxx.jpg"
	private int count;
	private String createDate;
	private String status;
	
}
