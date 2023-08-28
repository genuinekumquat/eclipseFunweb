package com.itwillbs.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itwillbs.domain.BoardDTO;
import com.itwillbs.domain.PageDTO;
import com.itwillbs.service.BoardService;

public class BoardController extends HttpServlet{ 
	
	BoardService boardService = null;
	RequestDispatcher dispatcher = null;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("BoardController doGet()");
		doProcess(request, response);
	}//doGet

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("BoardController doPost()");
		doProcess(request, response);
	}//doPost
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("BoardController doProcess()");
		String sPath = request.getServletPath();
		System.out.println("뽑아온 가상주소"+sPath);
		// 주소 비교 => 실제페이지 매핑
		if(sPath.equals("/list.bo")) {
			System.out.println("뽑은 가상주소 비교 : /list.bo");
			// 한 페이지에서 보여지는 글 개수 설정 
			int pageSize=10;
			// 페이지번호 
			String pageNum = request.getParameter("pageNum");
			// 페이지번호가 없으면 1페이지 설정  
			 if(pageNum==null) {
				 pageNum = "1";
			 }
			 // 페이지 번호를 정수형으로 변겅 
			 int currentPage = Integer.parseInt(pageNum);
			
			 PageDTO pageDTO = new PageDTO();
			 pageDTO.setPageSize(pageSize);
			 pageDTO.setPageNum(pageNum);
			 pageDTO.setCurrentPage(currentPage);
			 
			//BoardService 객체생성 
			boardService = new BoardService();
			// List<BoardDTO>=getBoardList(); 메서드 호출 
			List<BoardDTO> boardList=boardService.getBoardList(pageDTO);
			// 게시판 전체 글 개수 구하기 
			int count = boardService.getBoardCount();
			// 한화면에 보여줄 페이지개수 설정
			int pageBlock = 10;
			// 시작하는 페이지번호
			// currentPage  pageBlock  => startPage
			//   1~10(0~9)      10     =>  (0~9)/10*10+1=>0*10+1=> 0+1=> 1 
			//   11~20(10~19)   10     =>  (10~19)/10*10+1=>1*10+1=>10+1=>11
			//   21~30(20~29)   10     =>  (20~29)/10*10+1=>2*10+1=>20+1=>21
			int startPage=(currentPage-1)/pageBlock*pageBlock+1;
			// 끝나는페이지번호
			//  startPage   pageBlock => endPage
			//        1            10          -> 10
			//       11            10          -> 20
			//        21            10          -> 30
			int endPage=startPage+pageBlock-1;
			// 계산한값  endPage 은 10 실제페이지는 2
			// 전체페이지 구하기 
			// 글 개수 50 한 화면에 보여줄 글개수 10 일때 페이지수는 5개 +0
			// 글 개수 57 한 화면에 보여줄 글개수 10 일때 페이지수는 5개  + 나머지가 있는경우 1페이지를 더함 
			int pageCount = count/pageSize + (count%pageSize==0?0:1);
			
			if(endPage >pageCount) {
				endPage = pageCount; 
			}
			
			// pageDTO에 저장  
			pageDTO.setCount(count);
			pageDTO.setPageBlock(pageBlock);
			pageDTO.setStartPage(startPage);
			pageDTO.setStartPage(startPage);
			pageDTO.setEndPage(endPage);
			pageDTO.setPageCount(pageCount);
			
			// request에 "boardList",boardList 저장 
			request.setAttribute("boardList", boardList);
			request.setAttribute("pageDTO", pageDTO);
			
			// 주소변경없이 이동 center/notice.jsp
			dispatcher 
			= request.getRequestDispatcher("center/notice.jsp");
			dispatcher.forward(request, response);
		}// 
		
		if(sPath.equals("/write.bo")) {
			// 주소변경없이 이동 center/write.jsp
			dispatcher 
		    = request.getRequestDispatcher("center/write.jsp");
		dispatcher.forward(request, response);
		}//
		
		if(sPath.equals("/writePro.bo")) {
			System.out.println("뽑은가상주소비교 :/writePro.bo");
			// BoardService 객체생성 
			boardService = new BoardService();
			// 리턴할 형 insertBoard(request) 메서드 호출
			boardService.insertBoard(request);
			// list.bo 주소변경되면서 이동 
			response.sendRedirect("list.bo");
		}
		
		if(sPath.equals("/content.bo")){
			System.out.println("뽑은가상주소비교 :/content.bo");
			// BoardService 객체생성 
			boardService = new BoardService();
			// BoardDTO boardDTO = getBoard(request) 메서드 호출
			BoardDTO boardDTO = boardService.getBoard(request);
			// request에 "boardDTO", boardDTO담아서
			request.setAttribute("boardDTO", boardDTO);
			// center/content.jsp 주소변경없이 이동 
			dispatcher 
		    = request.getRequestDispatcher("center/content.jsp");
		dispatcher.forward(request, response);
		}
		
		//첨부파일 
		if(sPath.equals("/fwrite.bo")) {
			// 주소 변경없이 이동 
			dispatcher 
		    = request.getRequestDispatcher("center/fwrite.jsp");
		dispatcher.forward(request, response);
		}
		
		if(sPath.equals("/fwritePro.bo")) {
			System.out.println("뽑은가상주소비교 :/fwritePro.bo");
			// BoardService 객체생성 
			boardService = new BoardService();
			// 리턴할 형 finsertBoard(request) 메서드 호출
			boardService.finsertBoard(request);
			// list.bo 주소변경되면서 이동 
			response.sendRedirect("list.bo");
		}

		if(sPath.equals("/update.bo")) {
			System.out.println("뽑은 가상주소 비교 : /update.bo");
			// BoardService 객체생성
			boardService = new BoardService();
			// BoardDTO boardDTO = getBoard(request) 메서드 호출
			BoardDTO boardDTO = boardService.getBoard(request);
			// request에 "boardDTO",boardDTO 담아서
			request.setAttribute("boardDTO", boardDTO);
			// center/update.jsp 주소변경없이 이동
			dispatcher 
			= request.getRequestDispatcher("center/update.jsp");
			dispatcher.forward(request, response);
		}//
		
		if(sPath.equals("/updatePro.bo")) {
			System.out.println("뽑은 가상주소 비교 : /updatePro.bo");
			// BoardService 객체생성
			boardService = new BoardService();
			// updateBoard(request) 메서드 호출
			boardService.updateBoard(request);
			// 글목록 list.bo 주소 변경 되면서 이동
			response.sendRedirect("list.bo");
		}
		
		if(sPath.equals("/fupdate.bo")) {
			System.out.println("뽑은 가상주소 비교 : /fupdate.bo");
			// BoardService 객체생성
			boardService = new BoardService();
			// BoardDTO boardDTO = getBoard(request) 메서드 호출
			BoardDTO boardDTO = boardService.getBoard(request);
			// request에 "boardDTO",boardDTO 담아서
			request.setAttribute("boardDTO", boardDTO);
			// center/update.jsp 주소변경없이 이동
			dispatcher 
			= request.getRequestDispatcher("center/fupdate.jsp");
			dispatcher.forward(request, response);
		}//
		
		if(sPath.equals("/fupdatePro.bo")) {
			System.out.println("뽑은 가상주소 비교 : /fupdatePro.bo");
			// BoardService 객체생성
			boardService = new BoardService();
			// updateBoard(request) 메서드 호출
			boardService.fupdateBoard(request);
			// 글목록 list.bo 주소 변경 되면서 이동
			response.sendRedirect("list.bo");
		}
		
	}//doProcess()

}//클래스
