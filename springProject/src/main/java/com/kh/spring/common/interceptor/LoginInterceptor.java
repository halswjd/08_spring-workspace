package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	/*
	 * * Interceptor (정확히는 HandlerInterceptor)
	 * - 해당 Controller가 실행되기 전, 먼저 실행되어 낚아채서 실행할 내용 작성 가능
	 * - 로그인 유무 판단, 회원의 권한 체크
	 * 
	 * preHandele(전처리) : 컨트롤러를 호출하기 전에 낚아채는 영역
	 * postHandle(후처리) : 
	 * 
	 */
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// true 리턴 => 기존 요청 흐름대로 해당 Controller 제대로 실행
		// false 리턴 => Controller 실행되지 않게함
		
		// 현재 요청을 보낸 사람이 로그인이 되어 있을 경우 => Controller 실행
		HttpSession session = request.getSession();
		if(session.getAttribute("loginMember") != null) {
			return true;
		}else {
			// 로그인이 되어있지 않을 경우 => Controller 실행X
			session.setAttribute("alertMsg", "로그인 후 이용가능한 서비스 입니다.");
			response.sendRedirect(request.getContextPath());
			return false;
		}
		
	}
	
	
	
}
