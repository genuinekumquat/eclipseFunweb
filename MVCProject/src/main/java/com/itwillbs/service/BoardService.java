package com.itwillbs.service;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.itwillbs.dao.BoardDAO;
import com.itwillbs.domain.BoardDTO;

public class BoardService {
	BoardDAO boardDAO = null;
	
//	insertBoard(request)
	public void insertBoard(HttpServletRequest request) {
		try {
			// request => name subject content 가져오기 => 변수저장
			String name = request.getParameter("name");
			String subject = request.getParameter("subject");
			String content = request.getParameter("content");
			// num, readcount, date => 변수저장
			int readcount = 0; //조회수
			Timestamp date = new Timestamp(System.currentTimeMillis());
			
			int num = 1;
			// BoardDAO 객체생성
			boardDAO = new BoardDAO();
			
			num = boardDAO.getMaxNum() + 1;
			
		
			// BoardDTO 객체생성(num name subject content readcount date)
			BoardDTO boardDTO = new BoardDTO();
			// set 메서드 호출 변수값 저장
			boardDTO.setNum(num);
			boardDTO.setName(name);
			boardDTO.setSubject(subject);
			boardDTO.setContent(content);
			boardDTO.setReadcount(readcount);
			boardDTO.setDate(date);
			
			
			// insertBoard(boardDTO) 메서드호출
			boardDAO.insertBoard(boardDTO);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//insertBoard()

	public List<BoardDTO> getBoardList() {
		List<BoardDTO> boardList=null;
		try {
			boardDAO = new BoardDAO();
			boardList=boardDAO.getBoardList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return boardList;
	}

	public BoardDTO getBoard(HttpServletRequest request) {
		BoardDTO boardDTO=null;
		try {
			// request에 num 파라미터 값 가져오기
     int num = Integer.parseInt(request.getParameter("num"));
			// BoardDAO 객체생성 
     boardDAO = new BoardDAO();
			// boardDTO = getBoard(num);
     boardDTO=boardDAO.getBoard(num);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return boardDTO;
	}

	public void updateReadcount(HttpServletRequest request) {
		try {
			// request에 num 파라미터 값 가져오기
    int num = Integer.parseInt(request.getParameter("num"));
		  // BoardDAO 객체생성 
    boardDAO = new BoardDAO();
		  // updateReadcount(num) 호출 
    boardDAO.updateReadcount(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateBoard(HttpServletRequest request) {
		try {
			// => request 한글처리, request 값 가져오기, BoardDTO 값저장
		    //    BoardDAO 객체생성 updateBoard(boardDTO) 호출
			request.setCharacterEncoding("utf-8");
			int num=Integer.parseInt(request.getParameter("num"));
			String subject=request.getParameter("subject");
			String content=request.getParameter("content");
			
			BoardDTO boardDTO =new BoardDTO();
			boardDTO.setNum(num);
			boardDTO.setSubject(subject);
			boardDTO.setContent(content);
			
			boardDAO =new BoardDAO();
			boardDAO.updateBoard(boardDTO);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteBoard(HttpServletRequest request) {
		try {
		//  BoardService => int num = request 파라미터값 가져오기
		int num=Integer.parseInt(request.getParameter("num"));
//			 => BoardDAO 객체생성 , deleteBoard(num) 호출
		boardDAO =new BoardDAO();
		boardDAO.deleteBoard(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}//class
