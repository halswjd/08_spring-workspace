package com.kh.spring.member.cotroller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.member.model.service.MemberServiceImpl;
import com.kh.spring.member.model.vo.Member;

// @Controller => import함으로써 어노테이션 방식(스프링이 관리가능할수있게끔 bean에 등록됨), 이건 내가 만든 Class여야 가능한거임
@Controller // Controller 타입의 어노테이션을 붙여주면 빈스캐닝을 통해 자동으로 빈 등록 => Spring Explorer탭에서 빈 등록된걸 확인 가능 
public class MemberController {
	
//	private MemberServiceImpl mService = new MemberServiceImpl();
	
	@Autowired // 기존 위의 코드 대신 스프링이 알아서 필요할 때 생성하고 필요없으면 소멸시켜주는 역할 => DI(Dependency Injection) 특징 
	private MemberServiceImpl mService;
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	

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
	 *    
	 *    
	 *  * return "main"
	 *  <beans:bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
			<beans:property name="prefix" value="/WEB-INF/views/" /> => 경로의 앞부분
			<beans:property name="suffix" value=".jsp" /> => 뒷부분
		</beans:bean> 
		==> /WEB-INF/views/main.jsp 응답페이지 요청
	 *    
	 */
	
	/*
	@RequestMapping("login.me")
	public String loginMember(Member m, Model model, HttpSession session) { // 내가 service단에 Member 객체를 넘길 때 키값(name)과 Member 객체의 필드명이 일치할 경우 객체 바로 생성 가능함
		
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
	 */
	
	
	/*
	 * 2. 스프링에서 제공하는 ModelAndView 객체를 이용하는 방법
	 * 
	 *   Model은 데이터를 key-value 세트로 담을 수 있는 공간이라고 한다면
	 *   View는 응답뷰에 대한 정보를 담을 수 있는 공간
	 * 
	 */
	
	@RequestMapping("login.me")
	public ModelAndView loginMember(Member m, HttpSession session, ModelAndView mv) { // 내가 service단에 Member 객체를 넘길 때 키값(name)과 Member 객체의 필드명이 일치할 경우 객체 바로 생성 가능함
		
		/* 암호화 작업 전에 했던 과정
		Member loginMember = mService.loginMember(m);
		
		if(loginMember == null) { 
			// 로그인 실패 => 에러메시지(응답데이터) requestScope에 담아서 에러페이지(/WEB-INF/views/common.errorPage.jsp)로 포워딩
			mv.addObject("errorMsg", "로그인실패"); // addObjects("키", "벨류"); 
			mv.setViewName("common/errorPage"); // 응답페이지 요청
			
		}else { 
			// 로그인 성공 => loginMember sessionScope에 담고 메인페이지 url 재요청
			session.setAttribute("loginMember", loginMember);
			mv.setViewName("redirect:/");
		}
		
		return mv;
		*/
		
		// 암호화 작업 후에 해야되는 과정
		// Member m userId 필드 : 사용자가 입력한 아이디
		// 		   userPwd 필드 : 사용자가 입력한 비번(평문)
		Member loginMember = mService.loginMember(m);
		// loginMember : 오로지 아이디만을 가지고 조회된 회원
		// loginMember userPwd 필드 : db에 기록된 비번(암호문)
		
		// matches("평문비번", "암호화비번") : boolean 반환, 두 인자가 일치하면 true 반환 
		if(loginMember != null && bcryptPasswordEncoder.matches(m.getUserPwd(), loginMember.getUserPwd())) { // Id로 조회된 loginMember 객체가 있으면서 평문비번과 암호화비번이 같은 경우
			// 로그인 성공
			session.setAttribute("loginMember", loginMember);
			mv.setViewName("redirect:/");
		}else {
			// 로그인 실패
			mv.addObject("errorMsg", "로그인실패"); // addObjects("키", "벨류"); 
			mv.setViewName("common/errorPage"); // 응답페이지 요청
			
		}
		
		return mv;
		
	}
	
	@RequestMapping("logout.me")
	public String logoutMember(HttpSession session) {
		session.invalidate(); // session 영역 초기화
		return "redirect:/"; // 메인페이지로 이동
	}
	
	@RequestMapping("enrollForm.me")
	public String enrollForm() {
		// WEB-INF/views/member/memberEnrollForm.jsp 로 포워딩     
		return "member/memberEnrollForm";
		
	}
	
	@RequestMapping("myPage.me")
	public String myPageForm() {
		// WEB-INF/views/member/myPage.jsp
		return "member/myPage";
	}
	

	@RequestMapping("insert.me")
	public String insertMember(Member m, Model model, HttpSession session) {
//		System.out.println(m);
		// 1. 한글 깨짐 => 스프링에서 제공하는 인코딩 필터 등록 필요
		// 2. 나이를 입력하지 않았을 경우 "" 빈문자열이 넘어오는데 int형 필드에 담을 수 없어서 400 에러 발생
		//    => Member 클래스에 age 필드를 int형 --> String형으로 변경 --> DB member 테이블에 age 자료형이 number인데 괜찮나? -> 오라클은 자동형변환이 가능!
		// 3. 비밀번호가 사용자가 입력한 있는 그대로의 평문(pass01) -> 암호화 필요 -> 암호문(adfa@ds!XX)
		//	  => Bcrypt 방식의 암호화를 통해서 암호문으로 변경    
		//	  	=> 1) 스프링 시큐리티 모듈에서 제공(라이브러리 추가 pom.xml)
		//	  	=> 2) BcryptPassWirdEncoder 라는 클래스를 빈으로 등록(xml방식) -> -> spring-security.xml 파일에
		//			  외부 클래스라 어노테이션방식 안되고 xml 방식으로 등록해야함
		//		=> 3) web.xml에 spring-security.xml 파일을 pre-loading 할 수 있도록 작성
		
//		System.out.println("평문 : " + m.getUserPwd());
		
		// 암호화 작업 (암호문을 만들어내는 과정)
//		bcryptPasswordEncoder.encode("평문");
		String encPwd = bcryptPasswordEncoder.encode(m.getUserPwd());
//		System.out.println("암호문 : " + encPwd);
		m.setUserPwd(encPwd); // Member 객체에 userPwd에 평문이 아닌 암호문으로 변경
		
		int result = mService.insertMember(m);
		
		if(result > 0) { // 회원가입 성공 => 메인페이지로 url 재요청
			session.setAttribute("alertMsg", "성공적으로 회원가입되었습니다.");
			return "redirect:/";
		}else { // 실패 => 에러문구 담아서 errorPage로 포워딩
			model.addAttribute("errorMsg", "회원가입 실패!");
			return "common/errorPage";
		}
		
	}
	
	@RequestMapping("update.me")
	public String updateMember(Member m, Model model, HttpSession session) {
		
		int result = mService.updateMember(m);
		
		if(result > 0) { // 수정 성공
			// db로 부터 수정된 회원 정보를 다시 조회해와서
			// session에 loginMember 키값으로 덮어씌워야함
			
			Member updateMember = mService.loginMember(m);
			session.setAttribute("loginMember", updateMember);
			// session.setAttribute("loginMember", mService.loginMember(m));
			
			// alert 띄워줄 문구 담기
			session.setAttribute("alertMsg", "성공적으로 회원정보 변경되었습니다.");
			
			// 마이페이지로 url 재요청
			return "redirect:myPage.me"; // :/ => contextPath , :myPage.me => myPage.me Controller 탐
			
		}else { // 수정 실패 => 에러문구 담아서 에러페이지 포워딩
			model.addAttribute("errorMsg", "회원정보 변경 실패!");
			return "common/errorPage";
		}
		
	}
	
	@RequestMapping("delete.me")
	public String deleteMember(String userPwd, String userId, HttpSession session, Model model) { // name값이 userPwd, userId을 찾아서 바로 변수 설정
		
		// userPwd : 회원탈퇴 요청시 사용자가 입력한 비밀번호 평문이 담겨있음
		// session에 loginMember Member객체 userPwd필드에 db로 부터 조회된 비번(암호문)이 담겨있음
		String encPwd = ((Member)session.getAttribute("loginMember")).getUserPwd();
		
		if(bcryptPasswordEncoder.matches(userPwd, encPwd)) { // 입력한 비번이 맞음 => 탈퇴처리
			int result = mService.deleteMember(userId);			
			
			if(result > 0) { // 탈퇴처리 성공 => session에 있는 loginMember 지우고, alert문구 담기 => 메인페이지 url 재요청
				
				session.removeAttribute("loginMember");
				session.setAttribute("alertMsg", "성공적으로 탈퇴되었습니다. 그동안 이용해주셔서 감사합니다.");
				return "redirect:/";
				
			}else { // 탈퇴처리 실패 => 에러문구 담아서 에러페이지 포워딩
				model.addAttribute("errorMsg","회원탈퇴 실패!");
				return "common/errorPage";
			}
			
		}else { // 비번 틀림 => 비밀번호가 틀림을 알리고 마이페이지가 보여지게 처리, url 재요청
			session.setAttribute("alertMsg", "비밀번호를 잘못 입력하셨습니다. 확인해주세요");
			return "redirect:myPage.me";
		}
	}
	
	@ResponseBody
	@RequestMapping(value="idCheck.me")
	public String idCheck(String checkId) {
		
		int count = mService.idCheck(checkId);
		
		/*
		if(count > 0) { // 이미 존재하는 아이디 => 사용불가능(NNNNN)
			return "NNNNN"
		}else { // 사용 가능(NNNNY)
			return "NNNNY"
		}
		*/
		
		// 위의 구문 삼항연산자로 (조건?true일때 반환결과 :false일때 반환결과)
		return count > 0 ? "NNNNN" : "NNNNY";
		
	}
}





