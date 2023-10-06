package com.kh.ajax.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AjaxController {

	/*
	 * 1. HttpServletResponse 객체로 응답데이터 응답하기 (기존의 jsp, servlet 때 했던 출력 Stream 이용한 방식)
	@RequestMapping("ajax1.do")
	public void ajaxMethod1(String name, int age, HttpServletResponse response) throws IOException { // 키값과 동일하게 변수명을 지정하면 알아서 값이 꽂힘
		System.out.println(name);
		System.out.println(age);
		
		// 요청 처리를 위해 서비스 호출
		
		// 요청 처리가 다 됐다는 가정 하에 요청한 그 페이지에 응답할 데이터가 있을 경우
		String responseData = "응답문자열 : " + name + "은(는)" + age + "살 입니다.";
		String responseData = "응답문자열 : " + name + "은(는)" + age + "살 입니다.";
		
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print(responseData);
	}
	*/
	
	/*
	 * 2. 응답할 데이터를 문자열로 리턴
	 * 	  => 장점 : HttpServletResponse 객체를 쓰지않아도 됨
	 * 	  단, 문자열 리턴하면 원래는 포워딩 방식이였음 => 응답뷰로 인식해서 해당 뷰 페이지를 찾고있음
	 * 	  따라서 내가 리턴하는 문자열이 응답뷰가 아니라 응답데이터야 라는걸 선언하는 
	 * 	  어노테이션 @ResponseBody를 붙여야됨
	 */
	@ResponseBody								// ▽ 인코딩 설정 
	@RequestMapping(value="ajax1.do", produces="text/html; charset=UTF-8") // 속성값으로 value 하나일 때는 생략가능, 다른 속성값을 부여할때에는 기입
	public String ajaxMethod1(String name, int age) {
		
		String responseData = "응답문자열 : " + name + "은(는)" + age + "살 입니다.";
		return responseData; // 404 에러남 => prefix(WEB-INF/views/) + responseData + suffix(.jsp) 아래에서 해당 응답뷰 찾고 있는거임
	}
	
}
