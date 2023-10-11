package com.kh.ajax.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.kh.ajax.model.vo.Member;

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
		
		response.setContentType("text/html; charset=UTF-8"); 
		response.getWriter().print(responseData); // throws 처리
	}
	*/
	
	/*
	 * 2. 응답할 데이터를 문자열로 리턴
	 * 	  => 장점 : HttpServletResponse 객체를 쓰지않아도 됨
	 * 	  단, 문자열 리턴하면 원래는 포워딩 방식이였음 => 응답뷰로 인식해서 해당 뷰 페이지를 찾고있음
	 * 	  따라서 내가 리턴하는 문자열이 응답뷰가 아니라 응답데이터야 라는걸 선언하는 
	 * 	  어노테이션 @ResponseBody를 붙여야됨
	@ResponseBody								// ▽ 인코딩 설정 
	@RequestMapping(value="ajax1.do", produces="text/html; charset=UTF-8") // 속성값으로 value 하나일 때는 생략가능, 다른 속성값을 부여할때에는 기입
	public String ajaxMethod1(String name, int age) {7
		
		String responseData = "응답문자열 : " + name + "은(는)" + age + "살 입니다.";
		return responseData; // 404 에러남 => prefix(WEB-INF/views/) + responseData + suffix(.jsp) 아래에서 해당 응답뷰 찾고 있는거임
	}
	 */
	
	/*
	// 다수의 응답데이터가 있을 경우
	@RequestMapping("ajax1.do")
	public void ajaxMethod1(String name, int age, HttpServletResponse response) throws IOException {
		
		// 요청처리가 다 됐다는 가정하에 데이터 응답
		// 위의 방법1로 응답 -> HttpServletResponse
//		response.setContentType("text/html; charset=UTF-8"); // 인코딩 설정
//		response.getWriter().print(name);
//		response.getWriter().print(age);
		=> 연이어서 출력하는 데이터가 하나의 문자열로 연이어져 있음 ex) 차은우20
		
		// JSON(Javascript Object Notation) 형태로 응답
		// JSONArray => [값, 값, 값, ...] => 인덱스 구별O(인덱스로 접근해서 따로 출력 가능), JAVA에서의 ArrayList와 유사 (list에 추가할때는 add메소드)
		// JSONObject => {키:값, 키:값, ...} => 인덱스 구별X(값 넣은 순서대로 유지X), JAVA에서의 HashMap과 유사 (map에 추가할때는 put메소드)
		
		// 첫번째 방법 . JSONArray로 담아서 응답
		// 먼저 JSONArray 생성위해서 pom.xml에 라이브러리 등록 필요(eclips에서는 jar를 넣음)
//		JSONArray jArr = new JSONArray(); // [] 텅빈 상태
//		jArr.add(name); // ["차은우"]
//		jArr.add(age); // ["차은우", 20]
		
		// 두번째 방법 . JSONObejct로 담아서 응답
//		JSONObject jObj = new JSONObject(); // {} 텅빈 상태
//		jObj.put("name", name); // put(키,벨류), {name:'차은우'}
//		jObj.put("age", age); // {name:'차은우', age:20}
		
		// HttpServletResponse로 응답, 데이터 타입을 지정해줘야함
//		response.setContentType("application/json; charset=UTF-8"); // 내가 보내려는 데이터가 JSON형태의 데이터야~
//		response.getWriter().print(jArr);
//		response.getWriter().print(jObj);
		
	}
	*/

	@ResponseBody
	@RequestMapping(value="ajax1.do", produces="application/json; charset=UTF-8")
	public String ajaxMethod1(String name, int age) {
		
		JSONObject jObj = new JSONObject(); // {}
		jObj.put("name", name); // {name:'차은우'}
		jObj.put("age", age); // {name:'차은우', age:20}
		
		return jObj.toJSONString(); // "{name:'차은우', age:20}"
		
	}
	
	/*
	@ResponseBody
	@RequestMapping(value="ajax2.do", produces="application/json; charset=UTF-8")
	public String ajaxMethod2(int num) {
		
		// Member m = mService.selectMember(num); db에서 조회해왔다 가정하고
		Member m = new Member("user01", "pass01", "차은우", 20, "01011112222");
		
		// JSON형태로 만들어서 응답
		JSONObject jObj = new JSONObject(); // {}
		jObj.put("userId", m.getUserId());
		jObj.put("userName", m.getUserName());
		jObj.put("age", m.getAge());
		jObj.put("phone", m.getPhone());
		
		return jObj.toJSONString(); 
		
	}
	*/
	
	@ResponseBody
	@RequestMapping(value="ajax2.do", produces="application/json; charset=UTF-8")
	public String ajaxMethod2(int num) {
		
		// Member m = mService.selectMember(num); db에서 조회해왔다 가정하고
		Member m = new Member("user01", "pass01", "차은우", 20, "01011112222");
		
		return new Gson().toJson(m); // {userId:"user01", userPwd:"pass01", ...}
		// 내부적으로 Gson에서 여러개의 객체인지 하나의 객체인지 판단
		// 지금은 객체 하나니까 JSONObject 형태로 넘김
		// 키값은 객체의 필드명으로 알아서 셋팅됨
	}
	
	@ResponseBody
	@RequestMapping(value="ajax3.do", produces="application/json; charset=UTF-8")
	public String ajaxMethod3() {
		
//		ArrayList<Member> list = mService.selectList(); 조회했다 가정
		ArrayList<Member> list = new ArrayList<Member>(); // []
		
		list.add(new Member("user01", "pass01", "차은우", 20, "01011112222")); // [{은우객체}]
		list.add(new Member("user02", "pass02", "장원영", 21, "01012345678")); // [{은우객체}, {원영객체}]
		list.add(new Member("user03", "pass03", "하조비", 22, "01022223333")); // [{은우객체}, {원영객체}, {조비객체}]
		
		return new Gson().toJson(list);
		
	}
}
