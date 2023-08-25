package com.itwillbs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.itwillbs.domain.BoardDTO;
import com.itwillbs.domain.PageDTO;

public class BoardDAO { 
	
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null; 
	
	// 1,2단계 DB연결 메서드 정의 -> 필요로 할때 호출 사용 
	public Connection getConnection() throws Exception {     
	// throws Exception -> 메서드 호출한 곳에서 예외처리하도록 함 (예외처리 두번안하도록)
	
	/*
	 * // 1단계 JDBC 프로그램 가져오기 Class.forName("com.mysql.cj.jdbc.Driver"); 
	 * // 2단계 디비 연결
	 * String dbUrl="jdbc:mysql://localhost:3306/jspdb?serverTimezone=Asia/Seoul";
	 * String dbUser="root"; String dbPass="1234"; Connection
	 * con=DriverManager.getConnection(dbUrl, dbUser, dbPass); return con;
	 */
	
	// 서버에서 미리 DB 연결을 하고 연결한 자원의 이름을 불러서  사용 
	// DBCP (Database Connection Pool) 
	// DBCP 프로그램 설치는 미리 아파치 톰캣 서버에 설치되어있음 
	// webapp - META-INF - context.xml 만들기 
	// -> 서버에서 미리 DB 연결 
	
	// context.xml 불러오기 위해 객체생성 
	// import javax.naming.Context;
	// import javax.naming.InitialContext;
		Context init = new InitialContext();
	// lookup 메서드 (자원위치/자원이름 불러오기)
	// import javax.sql.DataSource;
		DataSource ds=(DataSource)init.lookup("java:comp/env/jdbc/Mysql");
	// DB연결 
		Connection con=ds.getConnection();
		return con;
		
	//작업속도가 빨라짐 (1, 2단계 생략)  -> 성능 향상 
	// DB 연결정보를 context.xml에서만 수정하면 모든 자바파일 수정됨, 	
	}
	
	public void dbClose() {
		if(pstmt != null) {try {pstmt.close();} catch (SQLException e) {} }				
		if(con != null) {try {con.close();} catch (SQLException e) {} }			
		if(rs != null) {try {rs.close();} catch (SQLException e) {} }
	}

	public List<BoardDTO> getBoardList(PageDTO pageDTO) {
		System.out.println("BoardDAO getBoardList()");
		List<BoardDTO> boardList = null;
		try {
			//1,2 연결
			con = getConnection();
			//3 sql  => mysql 제공 => limit 시작행-1, 몇개
//			String sql="select * from board order by num desc";
			String sql="select * from board order by num desc limit ?, ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, pageDTO.getStartRow()-1); // 시작행 -1 
			pstmt.setInt(2, pageDTO.getPageSize());//몇개	
			//4 실행 => 결과 저장 
			rs = pstmt.executeQuery();
			// boardList 객체생성
			boardList = new ArrayList<>();
			// 5 결과행접근 -> BoardDTO 객체생성 -> set 호출(열접근저장)			
			while(rs.next()) {
				BoardDTO boardDTO = new BoardDTO();
				boardDTO.setNum(rs.getInt("num"));
				boardDTO.setName(rs.getString("name"));
				boardDTO.setSubject(rs.getString("subject"));
				boardDTO.setContent(rs.getString("content"));
				boardDTO.setReadcount(rs.getInt("readcount"));
				boardDTO.setDate(rs.getTimestamp("date"));
				// -> 배열 한칸에 저장 
				boardList.add(boardDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return boardList;
	}//getBoardList()

	public int getMaxNum() {
		System.out.println("BoardDAO getMaxNum()");
		int num = 0;
		try {
			//1,2 디비연결
			con=getConnection();
			//3 sql select max(num) from members
			String sql = "select max(num) from board;";
			pstmt=con.prepareStatement(sql);
			//4 실행 => 결과저장
			rs =pstmt.executeQuery();
			//5 if 다음행  => 열데이터 가져와서 => num저장
			if(rs.next()) {
				num = rs.getInt("max(num)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			dbClose();
		}
		return num;
	}//getMaxNum()
	
	public void insertBoard(BoardDTO boardDTO) {
		System.out.println("BoardDAO insertBoard()");
		// board 테이블 file 열추가 
		// mysql -uroot -p1234 jspdb
		// alter table board 
		// add file varchar(100);
		try {
			// 1단계 JDBC 프로그램 가져오기 
			// 2단계 디비 연결
			con=getConnection();
			
			// 3단계 문자열 -> sql구문 변경
			String sql = "insert into board(num,name,subject,content,readcount,date,file) values(?,?,?,?,?,?,?)";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, boardDTO.getNum());      //(물음표 순서,값)
			pstmt.setString(2, boardDTO.getName()); 
			pstmt.setString(3, boardDTO.getSubject());
			pstmt.setString(4, boardDTO.getContent());
			pstmt.setInt(5, boardDTO.getReadcount());
			pstmt.setTimestamp(6, boardDTO.getDate());
			// 파일 추가 
			pstmt.setString(7, boardDTO.getFile());
			// 4단계 sql구문 실행
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			// 예외 상관 없이 마무리 작업 
			//  => con, pstmt, rs 기억장소 해제
			dbClose();
		}
	}//insertBoard()

	public int getBoardCount() {
		int count =0;
		try {
			// 1단계 JDBC 프로그램 가져오기 
			// 2단계 디비 연결
			con=getConnection();
			
			// 3단계 문자열 -> sql구문 변경			// 3 select count(*) from board	
			String sql = "select count(*) from board;";
			pstmt=con.prepareStatement(sql);
			//4 실행 => 결과저장
			rs =pstmt.executeQuery();
		if(rs.next()) {
			count = rs.getInt("count(*)");
		}
			// 실행  -> 결과 저장 
			// 5결과 행접근 -> 열접근-> count 변수 저장 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return count;
	}

	public BoardDTO getBoard(int num) {
		BoardDTO boardDTO = null;
		try {
			// 1,2
			con=getConnection();
			// 3 sql select * from board where num = ?
			String sql = "select * from board where num = ?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num); 
			// 4 실행 -> 결과 저장 
			rs =pstmt.executeQuery();
			// 5 결과 행접근 -> boardDTO 객체생성 
			//          -> set메서드 호출 -> 열접근 데이터 저장 
			if(rs.next()) {
				boardDTO = new BoardDTO();
				boardDTO.setNum(rs.getInt("num"));
				boardDTO.setName(rs.getString("name"));
				boardDTO.setSubject(rs.getString("subject"));
				boardDTO.setContent(rs.getString("content"));
				boardDTO.setReadcount(rs.getInt("readcount"));
				boardDTO.setDate(rs.getTimestamp("date"));
				// 첨부파일 
				boardDTO.setFile(rs.getString("file"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return boardDTO;
	}


}//클래스
