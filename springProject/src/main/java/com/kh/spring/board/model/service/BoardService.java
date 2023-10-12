package com.kh.spring.board.model.service;

import java.util.ArrayList;

import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;

public interface BoardService {

	// 1. 게시판 리스트 페이지 서비스 (페이징)
	int selectListCount();
	ArrayList<Board> selectList(PageInfo pi);
	
	// 2. 게시글 작성하기 서비스
	int insertBoard(Board b);
	
	// 3. 게시글 상세조회용 서비스
	int increaseCount(int BoardNo);
	Board selectBoard(int BoardNo);
	
	// 4. 게시글 삭제용 서비스
	int deleteBoard(int BoardNo);
	
	// 5. 게시글 수정용 서비스
	int updateBoard(Board b);
	
	// 6. 댓글 리스트 조회용 서비스 (ajax)
	ArrayList<Reply> selectReplyList(int BoardNo);
	
	// 7. 댓글 작성용 서비스 (ajax)
	int insertReply(Reply r);
	
	// 8. 조회수 top5 리스트 조회용 서비스
	ArrayList<Board> selectTopBoardList();
	
	
	// 첨파 여러개
	int insertAttachment(ArrayList<Attachment> atList);
}
