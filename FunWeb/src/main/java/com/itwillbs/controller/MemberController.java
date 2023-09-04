package com.itwillbs.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.itwillbs.domain.BoardDTO;
import com.itwillbs.domain.MemberDTO;
import com.itwillbs.domain.PageDTO;
import com.itwillbs.service.BoardService;
import com.itwillbs.service.MemberService;


public class MemberController extends HttpServlet{
	
	RequestDispatcher dispatcher =null;
	MemberService memberService = null;
	BoardService boardService = null;
	
// HttpServlet 처리담당자 -> 자동으로 doGet, doPost 호출
	// -> 재정의 해서 사용
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("MemberController doGet()");
		doProcess(request, response);
	}//doGet()

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("MemberController doPost()");
		doProcess(request, response);
	}//doPost()
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("MemberController doProcess()");
		//가상주소 뽑아오기
		String sPath=request.getServletPath();
		System.out.println("뽑은 가상주소 :  " + sPath);
		//뽑은 가상주소 비교하기 => 실제페이지 연결
		if(sPath.equals("/insert.me")) {
			// member/join.jsp 주소변경 없이 연결
			dispatcher 
		    = request.getRequestDispatcher("member/join.jsp");
		dispatcher.forward(request, response);
		}//
		if(sPath.equals("/insertPro.me")) {
			System.out.println("뽑은 가상주소 비교 : /insertPro.me");
			//회원가입 => MemberService => MemberDAO
			// MemberService 객체생성
			memberService = new MemberService();
			// insertMember() 메서드 호출
			memberService.insertMember(request);
			//로그인 이동 => 주소변경하면서 이동
			response.sendRedirect("login.me");
		}//
		if(sPath.equals("/login.me")) {
			// member/login.jsp 주소변경 없이 이동
			dispatcher 
		    = request.getRequestDispatcher("member/login.jsp");
		dispatcher.forward(request, response);
		}//
		
		if(sPath.equals("/loginPro.me")) {
			System.out.println("뽑은 가상주소 비교 : /loginPro.me");
	// MemberService 객체생성
			memberService = new MemberService();
	// MemberDTO memberDTO = userCheck(request) 메서드 호출
	MemberDTO memberDTO = memberService.userCheck(request);

	if(memberDTO != null) {
		// memberDTO != null 아이디 비밀번호 일치=> 세션값 저장=>main.me
		HttpSession session = request.getSession();
		session.setAttribute("id", memberDTO.getId());
		response.sendRedirect("main.me");
	}else {
		// memberDTO == null 아이디 비밀번호 틀림=> member/msg.jsp
		dispatcher 
	    = request.getRequestDispatcher("member/msg.jsp");
	dispatcher.forward(request, response);
	}
	
	}//	if
		
	if(sPath.equals("/main.me")) {
		PageDTO pageDTO = new PageDTO();
		pageDTO.setPageSize(5);
		pageDTO.setCurrentPage(1);
		boardService = new BoardService();
		List<BoardDTO> boardList = boardService.getBoardList(pageDTO);
		
		request.setAttribute("boardList",boardList);
		
		dispatcher 
	    = request.getRequestDispatcher("main/main.jsp");
	dispatcher.forward(request, response);
	}//
	
	if(sPath.equals("/logout.me")) {
		//세션값 전체 삭제
		HttpSession session = request.getSession();
		session.invalidate();
		// main.me 주소변경하면서 이동
		response.sendRedirect("main.me");
	}//
	
	if(sPath.equals("/update.me")) {
		System.out.println("뽑은 가상주소 비교 : /update.me");
		//수정하기 전에 db 나의 정보 조회(세션값 id) => jsp 화면 출력
		// 세션 객체생성
		HttpSession session = request.getSession();
		// "id" 세션값 가져오기=> String id 변수 저장
		String id = (String)session.getAttribute("id");
		// MemberService 객체생성
		memberService = new MemberService();
		// MemberDTO memberDTO = getMember(id) 메서드 호출
		MemberDTO memberDTO = memberService.getMember(id);
		// request에 memberDTO 저장 ("이름",값)
		request.setAttribute("memberDTO", memberDTO);
		// member/update.jsp 주소변경없이 이동
		dispatcher 
	    = request.getRequestDispatcher("member/update.jsp");
	dispatcher.forward(request, response);
	}//
	
	if(sPath.equals("/updatePro.me")) {
		System.out.println("뽑은 가상주소 비교 : /updatePro.me");
		// request안에 폼에서 입력한 값이 저장
		// MemberService 객체생성		
		memberService = new MemberService();
		// 아이디 비밀번호 일치 확인 -> MemberDTO memberDTO = userCheck(request) 메서드 호출
		MemberDTO memberDTO = memberService.userCheck(request);
		// memberDTO != null 아이디 비밀번호 일치-> 수정 리턴할형없음 -> updateMember(request) 메서드 호출
		// sql -> update members set name = ? where id = ? 
		// -> main.me
		// memberDTO == null 아이디 비밀번호 틀림=> member/msg.jsp
		if(memberDTO != null) {
			memberService.updateMember(request);
			response.sendRedirect("main.me");
		} else {
			dispatcher 
			= request.getRequestDispatcher("member/msg.jsp");
			dispatcher.forward(request, response); 
		}

	}//
	if(sPath.equals("/delete.me")) {
		System.out.println("뽑은 가상주소 비교 : /delete.me");
		// member/delete.jsp 주소변경없이 이동 
		dispatcher 
		= request.getRequestDispatcher("member/delete.jsp");
		dispatcher.forward(request, response); 
	}
	
	if(sPath.equals("/deletePro.me")) {
		System.out.println("뽑은 가상주소 비교 : /deletePro.me");
		// MemberService 객체생성
		memberService = new MemberService();
		// 아이디 비밀번호 일치 확인 -> MemberDTO memberDTO = userCheck(request) 메서드 호출
		MemberDTO memberDTO = memberService.userCheck(request); 
		// memberDTO != null 아이디 비밀번호 일치-> 수정 리턴할형없음 -> deleteMember(request) 메서드 호출
		if(memberDTO != null) {
			// -> 세션값 초기화 
			// -> main.me로 이동 
			memberService.deleteMember(request);
			HttpSession session = request.getSession(); 
			session.invalidate(); 
			response.sendRedirect("main.me");
		} else {
		// 틀렸을경우 
		// memberDTO == null 아이디 비밀번호 틀림=> member/msg.jsp
			dispatcher 
			= request.getRequestDispatcher("member/msg.jsp");
			dispatcher.forward(request, response); 
		}

	}
	// 아이디 중복체크 
	if(sPath.equals("/idCheck.me")) {
		System.out.println("뽑은 가상주소 비교 : /idCheck.me");
		String id = request.getParameter("id");
		System.out.println("받은 아이디" + id); 
		// memberService 객체생성 
		memberService = new MemberService();
		// getMember() 메서드 호출 
		MemberDTO memberDTO =  memberService.getMember(id);
		String result = "";
		if(memberDTO != null) {
			// 아이디있음 -> 아이디 중복
			System.out.println("아이디있음 -> 아이디 중복"); 
			result = "아이디 중복";
		} else {
			// 아이디없음 -> 아이디 사용가능 
			System.out.println("아이디없음 -> 아이디 사용가능");
			result = "아이디 사용가능";
		}
	
	// 이동하지않고 결과 웹에 출력 -> 출력결과를 가지고 되돌아감 
	response.setContentType("text/html; charset=UTF-8");
	PrintWriter printWriter = response.getWriter();
	printWriter.println(result);
	printWriter.close();
	}
	
	if(sPath.equals("/list.me")) {
		System.out.println("뽑은 가상주소 비교 : /list.me");
		
		// memberService 객체생성 
		memberService = new MemberService();
		// List<MemberDTO> memberList = getMemberList(); 메서드 호출 
		List<MemberDTO> memberList = memberService.getMemberList();
		// request에 "memberList", memberList를 담기 
		request.setAttribute("memberList", memberList);
		// member/login.jsp 주소변경 없이 이동 
		dispatcher 
		= request.getRequestDispatcher("member/list.jsp");
		dispatcher.forward(request, response); 
		
		}// 
	if(sPath.equals("/listjson.me")) {
		// memberService 객체생성 
		memberService = new MemberService();
		// List<MemberDTO> memberList = getMemberList(); 메서드 호출 
		List<MemberDTO> memberList = memberService.getMemberList();
		// memberList -> json 변경해서 출력 
		// 자바 -> json으로 변경하는 프로그램 설치 (json-simple-1.1.1.jar)
		
		// List -> JSONArray 
		// import org.json.simple.JSONArray
		JSONArray arr = new JSONArray();
		// 날짜 -> 원하는 형으로 문자열 변경 
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		for(int i = 0; i<memberList.size(); i++) {
			MemberDTO memberDTO = memberList.get(i);
			// MemberDTO -> JSONObject	
			JSONObject object = new JSONObject();
			object.put("id",memberDTO.getId());
			object.put("pass",memberDTO.getPass());
			object.put("name",memberDTO.getName());
			object.put("date",format.format(memberDTO.getDate()));
			arr.add(object);
		}
		
		// 이동하지않고 결과 웹에 출력 -> 출력결과를 가지고 되돌아감 
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.println(arr);
		printWriter.close(); 
		// json 데이터 결과확인 
		// http://localhost:8080/FunWeb/listjson.me

		}
	
	}//doProcess()

}//클래스
