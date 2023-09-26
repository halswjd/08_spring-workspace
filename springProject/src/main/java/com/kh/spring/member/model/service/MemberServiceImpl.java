package com.kh.spring.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.vo.Member;

	
// 스프링이 new~ 없이 생성할 수 있도록 bean 등록
@Service
public class MemberServiceImpl implements MemberService {
	
//	private MemberDao mDao = new MemberDao();
	@Autowired
	private MemberDao mDao;
	
	// bean에 등록된 sqlSession 사용
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Override
	public Member loginMember(Member m) {
		
		// 기존 : sqlSession 생성 => dao 호출
		Member loginMember = mDao.loginMember(sqlSession, m);
		
//		sqlSession.close(); 스프링이 알아서 close해줘서 생략 가능
		
		return loginMember;
	}

	@Override
	public int insertMember(Member m) {
		
		int result = mDao.insertMember(sqlSession, m);
		
		return result;
	}

	@Override
	public int updateMember(Member m) {
		return 0;
	}

	@Override
	public int deleteMember(String userId) {
		return 0;
	}

	@Override
	public int idCheck(String userId) {
		return 0;
	}

}
