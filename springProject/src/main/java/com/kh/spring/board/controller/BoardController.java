package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.board.model.service.BoardServiceImpl;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.common.model.vo.PageInfo;
import com.kh.spring.common.template.Pagination;

@Controller
public class BoardController {

	@Autowired
	private BoardServiceImpl bService;
	
	// 메뉴바 클릭시   /list.bo						(기본적으로 1번 페이지 요청)
	// 페이징바 클릭시 /list.bo?cpage=요청하는 페이지수
	
	/*
	@RequestMapping("list.bo")
	public String selectList(@RequestParam(value="cpage", defaultValue="1") int currentPage, Model model) {
		
//		System.out.println(currentPage);
		
		int listCount = bService.selectListCount();
		
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 10, 5);
		
		ArrayList<Board> list = bService.selectList(pi);
		
		model.addAttribute("pi", pi);
		model.addAttribute("list", list);
		
		// 포워딩할 뷰 (WEB-INF/views/   board/boardListView   .jsp)
		return "board/boardListView";
	}
	*/
	
	@RequestMapping("list.bo")
	public ModelAndView selectList(@RequestParam(value="cpage", defaultValue="1") int currentPage, ModelAndView mv) {
		
		int listCount = bService.selectListCount();
		
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 10, 5);
		
		ArrayList<Board> list = bService.selectList(pi);

		/*
		mv.addObject("pi", pi);
		mv.addObject("list", list);
		mv.setViewName("board/boardListView");
		*/
		
		// 위의 세 줄을 한줄로
		// 위의 세개의 메소드 반환형이 ModelAndView라서 메소드체이닝(?) 가능함
		mv.addObject("pi",pi).addObject("list", list).setViewName("board/boardListView");
		
		// 포워딩할 뷰 (WEB-INF/views/   board/boardListView   .jsp)
		return mv;
	}
	
	@RequestMapping("enrollForm.bo")
	public String enrollForm() {
		// WEB-INF/views/   board/boardEnrollForm   .jsp
		return "board/boardEnrollForm";
	}
	
	@RequestMapping("insert.bo")
	public String insertBoard(Board b, MultipartFile upfile, HttpSession session, Model model) { // 첨부파일의 name값과 변수명을 동일하게해서 넘겨받기 
		
//		System.out.println(b);
//		System.out.println(upfile); // 첨부파일 선택했든 안했든 생성되는 객체임 (다만 filename에 원본명이 있는지,""없는지 차이)	
		
		// 전달된 파일이 있을 경우 => 파일명 수정 작업 후 서버 업로드 => 원본명, 서버업로드된 경로를 b에 이어서 담기
		if(!upfile.getOriginalFilename().equals("")) { //  첨부파일이 있을 때(파일을 첨부했을때)
			
			// 파일명 수정 작업 후 서버에 업로드 시키기 ("flower.png" => "2023100412153012345.png")
			/*
			String originName = upfile.getOriginalFilename(); // "flower.png"
			
			// "20231004154607" (년월일시분초) + 5자리 랜덤값 + 확장자
			String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			int ranNum = (int)(Math.random() * 90000 + 10000);
			String ext = originName.substring(originName.lastIndexOf(".")); // lastIndexOf(".") : '.' 위치한 인덱스 반환
			
			String changeName = currentTime + ranNum + ext;

			// 업로드 시키고자 하는 폴더의 물리적인 경로를 알아내기
			String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/"); // / => webapp
			
			try {
				// 서버 업로드를 위한 작업
				upfile.transferTo(new File(savePath + changeName));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			*/
			
			// 계속 사용할 코드 => 메소드로 만들기 => "리팩토링 작업"
			
			String changeName = saveFile(upfile, session);
			
			// 원본명, 서버업로드된 경로를 Board b에 이어서 담기 => db 저장을 위한 작업
//			b.setOriginName(originName);
			b.setOriginName(upfile.getOriginalFilename());
			b.setChangeName("resources/uploadFiles/" + changeName);
			
		}
		
		// 넘어온 첨부파일 있을 경우 b : 제목, 작성자, 내용, 파일원본명, 파일저장경로
		// 넘어온 첨부파일 없을 경우 b : 제목, 작성자, 내용
		
		int result = bService.insertBoard(b);
		
		if(result > 0) { // 게시글 작성 성공 => 게시글 리스트페이지(list.bo url재요청)
			session.setAttribute("alertMsg", "성공적으로 게시글이 등록되었습니다.");
			return "redirect:list.bo";
		}else { // 실패 => 에러문구 담아서 에러페이지로 포워딩
			model.addAttribute("errorMsg", "게시글 작성 실패!");
			return "common/errorPage";
		}
		
	}
	
	// 현재 넘어온 첨부파일 그 자체를 서버의 폴더에 저장시키는 역할
	public String saveFile(MultipartFile upfile, HttpSession session) {
		
		String originName = upfile.getOriginalFilename(); // "flower.png"
		
		// "20231004154607" (년월일시분초) + 5자리 랜덤값 + 확장자
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		int ranNum = (int)(Math.random() * 90000 + 10000);
		String ext = originName.substring(originName.lastIndexOf(".")); // lastIndexOf(".") : '.' 위치한 인덱스 반환
		
		String changeName = currentTime + ranNum + ext;

		// 업로드 시키고자 하는 폴더의 물리적인 경로를 알아내기
		String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/"); // / => webapp
		
		try {
			// 서버 업로드를 위한 작업
			upfile.transferTo(new File(savePath + changeName));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		
		return changeName;

	}
	
	@RequestMapping("detail.bo")
	public String selectBoard(@RequestParam(value="bno") int boardNo, Model model) {
//		System.out.println(boardNo);
		
		// 해당 게시글 조회수 증가 서비스 호출 결과 받기 (update 하고 옴)
		int result = bService.increaseCount(boardNo);
		
		if(result > 0) {
			Board b = bService.selectBoard(boardNo);
			model.addAttribute("b", b);
			return "board/boardDetail";
		}else {
			model.addAttribute("errorMsg", "게시글 작성 실패!");
			return "common/errorPage";
		}
		
	}
}








