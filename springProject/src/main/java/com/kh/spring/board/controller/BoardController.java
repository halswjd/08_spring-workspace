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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.kh.spring.board.model.service.BoardServiceImpl;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
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
	
	@RequestMapping("insert.test")
	public void insertBoardTest(Board b, MultipartFile[] upfile, HttpSession session, Model model) {
		
		int result = bService.insertBoard(b);
		
		if(result > 0) {
			
			ArrayList<Attachment> atList = new ArrayList<Attachment>(); 
			Attachment at = null;
			for(MultipartFile f:upfile) {
//			System.out.println(f);
				if(!f.getOriginalFilename().equals("")) {
//				list.add(saveFile(f, session));
					at = new Attachment();
					at.setOriginName(f.getOriginalFilename());
					at.setChangeName(saveFile(f, session));
					atList.add(at);
				}
			}
			
			
		}
		
		
		
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

			// 업로드 시키고자 하는 폴더의 물리적인 경로를 알아내기 => application 영역 접근 => for HttpSesison session
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
	
		// 만약 다중파일 업로드 기능시?
		// 여러개의 input type="file" 요소에 다 동일한 키값(name)으로 부여 ex)upfile
		// MultiFile[] upfile 로 받으면 됨(0번 인덱스부터 차곡차곡 첨부파일 담겨있음)
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
	public String selectBoard(@RequestParam(value="bno") int boardNo, Model model) { // 키 값 int bno로 받아도 됨
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
	
	@RequestMapping("delete.bo")
	public String deleteBoard(int bno, String filePath, HttpSession session, Model model) { // filePath="resources/upfiles/xxxxxx.jpg" | ""
		
		int result = bService.deleteBoard(bno);
		
		if(result > 0) { 
			// 삭제 성공
			
			// 첨부파일이 있었을 경우 => 파일 삭제
			if(!filePath.equals("")) {
				// 물리적 경로 알아내기 => application 영역 접근필요 => session.getServletContext()
				new File(session.getServletContext().getRealPath(filePath)).delete();
			}
			
			// 게시판 리스트 페이지 list.bo url 재요쳥
			session.setAttribute("alertMsg", "성공적으로 게시글이 삭제되었습니다.");
			return "redirect:list.bo";
			
		}else { // 삭제 실패
			model.addAttribute("errorMsg", "게시글 삭제 실패!");
			return "common/errorPage";
		}
		
	}
	
	@RequestMapping("updateForm.bo")
	public String updateForm(int bno, Model model) {
		model.addAttribute("b", bService.selectBoard(bno));
		return "board/boardUpdateForm";
	}
	
	@RequestMapping("update.bo")
	public String update(@ModelAttribute Board b, MultipartFile reupfile, HttpSession session, Model model) {
		// @ModelAttribute : jsp에서 넘겨주는 값을 Board 객체로 받을거다라고 명시하는 구문, 생략해도 가능함
		// 새로 넘어온 첨부파일이 있을 경우
		if(!reupfile.getOriginalFilename().equals("")) {
			
			// 기존에 첨부파일이 있었을 경우 => 기존의 첨부파일 지우기
			if(b.getOriginName() != null) {
				// 애플리케이션 영역 접근해서 uploadFile에 있는 파일 삭제
				new File(session.getServletContext().getRealPath(b.getChangeName())).delete();
			}
			
			// 새로 넘어온 첨부파일 서버 업로드 시키기
//			String changeName = saveFile(reupfile, session)
			
			// b에 새로 넘어온 첨부파일에 대한 원본명, 저장경로 담기
			b.setOriginName(reupfile.getOriginalFilename());
//			b.setChangeName("resources/uploadFiles/" + changeName);
			b.setChangeName("resources/uploadFiles/" + saveFile(reupfile, session));
			
		}
		
		/*
		 * b에 boardNo, boardTitle, boardContent 무조건 담겨있음!!
		 * 
		 * 1. 새로 첨부된 파일 X, 기존 첨부 파일 X => originName : null, changeName : null
		 * 2. 새로 첨부된 파일 X, 기존 첨부 파일 O => originName : 기존첨파원본명, changeName : 기존첨파저장경로
		 * 3. 새로 첨부된 파일 O, 기존 첨부 파일 X => 새로 첨부된 파일을 서버에 업로드
		 * 								   => originName : 새로운 첨부파일 원본명, changeName : 새로운 첨부파일 저장경로
		 * 4. 새로 첨부된 파일 O, 기존 첨부 파일 O => 기존의 파일 삭제
		 * 								   => 새로 전달된 파일 서버에 업로드
		 * 								   => originName : 새로운 첨부파일 원본명, changeName : 새로운 첨부파일 저장경로
		 */
		
		int result = bService.updateBoard(b);
		
		if(result > 0) { // 수정 성공 => 상세페이지 url 페이지 detail.bo?bno=해당게시글번호
			session.setAttribute("alertMsg", "성공적으로 게시글이 수정되었습니다.");
			return "redirect:detail.bo?bno=" + b.getBoardNo();
		}else { // 수정 실패
			model.addAttribute("errorMsg", "게시판 수정 실패!");
			return "common/errorPage";
		}
	}
	
	@ResponseBody
	@RequestMapping(value="rlist.bo", produces="application/json; charset=UTF-8")
	public String ajaxSelectReplyList(int bno) {
		ArrayList<Reply> list = bService.selectReplyList(bno);
		
		return new Gson().toJson(list);
	}
	
	@ResponseBody
	@RequestMapping("rinsert.bo")
	public String ajaxInsertReply(Reply r) {
		int result = bService.insertReply(r);
		
		return result>0? "success" : "fail";
	}
}
