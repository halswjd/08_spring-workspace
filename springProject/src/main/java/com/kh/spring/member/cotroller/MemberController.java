package com.kh.spring.member.cotroller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.spring.member.model.service.MemberServiceImpl;
import com.kh.spring.member.model.vo.Member;

@Controller // Controller 타입의 어노테이션을 붙여주면 빈스캐닝을 통해 자동으로 빈 둥록
public class MemberController {
	
//	private MemberServiceImpl mService = new MemberServiceImpl();
	
	@Autowired // 기존 위의 코드 대신 스프링이 알아서 필요할 때 생성하고 필요없으면 소멸시켜주는 역할 => DI(Dependency Injection) 특징 
	private MemberServiceImpl mService;
	
// @Controller => import함으로써 어노테이션 방식(스프링이 관리가능할수있게끔 bean에 등록됨), 이건 내가 만든 Class여야 가능한거임

	/*
	@RequestMapping(value="login.me") // RequestMapping 타입의 어노테이션을 붙여줌으로써 HandlerMapping 등록 => 절대 중복 불가!
	public void loginMember() {
		
	}
	
	public void insertMember() {
		
	}
	
	public void updateMember() {
		
	}
	*/
	
	/*
	 * * 파리미터(요청시 전달값)를 받는 방법 (요청시 전달되는 값들 처리방법)
	 * 
	 *  1. HttpServletRequest(request)를 이용해서 전달받기 (기존의 jsp/servlet 방식)
	 *     해당 메소드의 매개번수로 HttpServletRequest를 작성해두면
	 *     스프링컨테이너가 해당 메소드 호출시(실행시) 자동으로 해당 객체를 생성해서 인자로 주입해줌
	 *  
	 */
	
	/*
	@RequestMapping("login.me") // RequestMapping 속성에는 여러개가 있는데 value하나라면 그냥 기입 가능
	public String loginMember(HttpServletRequest request) {
		String userId = request.getParameter("id");
		String userPwd = request.getParameter("pwd");
		
		System.out.println("ID : " + userId + ", PWD : " + userPwd);
		
		return "main";
	}
	*/
	
	/*
	 * 2. @RequestParam 어노테이션을 이용하는 방법 ***
	 * 	  request.getParameter("키") : 벨류 의 역할을 대신해주는 어노테이션
	 * 	  defaultValue 값을 설정할 수 있는 장점이 있음
	 */
	
	/*
	@RequestMapping("login.me")
	public String loginMember( @RequestParam(value="id", defaultValue="aaa") String userId, // key가 id인 것을 찾아 userId 변수로 저장하겠다, key값이 null이면 defaultValue값으로 하겠다
							   @RequestParam(value="pwd") String userPwd) { 
		
		System.out.println("ID : " + userId + ", PWD : " + userPwd);
		
		return "main";
	}
	*/
	
	/*
	 * 3. @RequestParam 어노테이션을 생략하는 방법
	 * 	  ** 단, 매개변수명 name값(요청시전달값의 키값)과 동일하게 세팅해둬야 자동으로 값이 주입됨
	 */
	
	/*
	@RequestMapping("login.me")
	public String loginMember(String id, String pwd) {
		
		System.out.println("ID : " + id + ", PWD : " + pwd);
		
		Member m = new Member();
		m.setUserId(id);
		m.setUserPwd(pwd);

		// Service쪽 메소드에 m을 전달하며 조회
		
		
		return "main";
	}
	*/
	
	/*
	 * 4. 커맨드 객체 방식 ***
	 *    해당 메소드 매개변수로 요청시 전달값을 담고자 하는 vo클래스 타입을 셋팅 후
	 *    요청시 전달값의 키값(name값)을 vo클래스에 담고자 하는 필드명으로 작성
	 *    
	 *    스프링컨테이너가 해당 객체를 기본생성자로 생성 후 setter 메소드 찾아서
	 *    요청시 전달값을 해당 필드에 담아주는 내부적인 원리
	 *    
	 *    ** 커맨드 객체 방식을 사용하고자 한다면 반드시 name속성값(키값)과 담고자하는 필드명 동일해야됨!
	 *    
	 */
	
	/*
	 * * 요청 처리 후 응답페이지로 포워딩 또는 url 재요청, 응답데이터 담는 방법
	 * 
	 * 1. 스프링에서 제공하는 Model 객체를 사용하는 방법
	 * 	  포워딩 할 뷰로 전달하고자하는 데이터를 맵형식(key-value)으로 담을 수 있는 영역
	 * 	  Model 객체는 requestScope 이다.
	 *    단, setAttribute가 아닌 addAttribute 메소드 이용
	 *    <beans:bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
				<beans:property name="prefix" value="/WEB-INF/views/" />
				<beans:property name="suffix" value=".jsp" />
			</beans:bean>
	 *    
	 */
	@RequestMapping("login.me")
	public String loginMember(Member m, Model model, HttpSession session) { // 내가 service단에 Member 객체를 넘길거로 키값(name)과 Member 객체의 필드명이 일치할 경우 객체 바로 생성 가능함
		
//		System.out.println("ID : " + m.getUserId() + ", PWD : " + m.getUserPwd());
		Member loginMember = mService.loginMember(m);
		
		if(loginMember == null) { 
			// 로그인 실패 => 에러메시지(응답데이터) requestScope에 담아서 에러페이지(/WEB-INF/views/common.errorPage.jsp)로 포워딩
			model.addAttribute("errorMsg","로그인실패");
			return "common/errorPage"; // prefix:"/WEB-INF/views/", suffix;".jsp" 
			
		}else { 
			// 로그인 성공 => loginMember sessionScope에 담고 메인페이지 url 재요청
			session.setAttribute("loginMember", loginMember);
			
			return "redirect:/";
		}
		
	}
}





