package com.itwillbs.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.itwillbs.domain.BoardDTO;

public class BoardDAO {
	
	Connection con=null;
	PreparedStatement pstmt=null;
	ResultSet rs =null;
	
	//1,2 단계 디비 연결 메서드  정의 => 필요로 할때 호출 사용
	public Connection getConnection() throws Exception {
		// throws Exception => 메서드 호출한 곳에서 예외처리하도록 함
		
//		// 1단계 JDBC 프로그램 가져오기 
//		Class.forName("com.mysql.cj.jdbc.Driver");
//		// 2단계 디비 연결
//		String dbUrl="jdbc:mysql://localhost:3306/jspdb?serverTimezone=Asia/Seoul";
//		String dbUser="root";
//		String dbPass="1234";
//		Connection con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
//		return con;
		
		// 서버에서 미리 디비연결을 하고 연결한 자원의 이름을
		// 불러서 사용
		// DBCP (DataBase Connection Pool)
		// 프로그램 설치 => 미리 서버에 설치 되어있음
		// webapp - META-INF - context.xml 만들기
		// => 서버에서 미리 디비연결
		
		// context.xml 불러오기위해 Context 객체생성
		// import javax.naming.Context;
		// import javax.naming.InitialContext;
		Context init = new InitialContext();
		// lookup 메서드 (자원위치/자원이름 불러오기)
		// import javax.sql.DataSource;
		DataSource ds=
				(DataSource)init.lookup("java:comp/env/jdbc/Mysql");
		// 디비연결
		Connection con=ds.getConnection();
		return con;
		// 작업 속도가 빨라짐(1,2 단계 생략)=> 성능향상
		// 디비연결 정보를 context.xml에서만 수정하면 모든 자바파일 수정 됨 
	}
	
	//기억장소 해제 메서드()
	public void dbClose() {
		// 예외 상관 없이 마무리 작업 
					//  => con, pstmt, rs 기억장소 해제
		if(rs != null) {try {rs.close();} catch (SQLException e) {	}}			
		if(pstmt != null) {try {pstmt.close();} catch (SQLException e) {	}}
		if(con != null) {try {con.close();} catch (SQLException e) {	}}
	}
	
//	insertBoard(boardDTO)
	public void insertBoard(BoardDTO boardDTO) {
		try {
			// 1단계 JDBC 프로그램 가져오기 
			// 2단계 디비 연결
			con=getConnection();
			
			// 3단계 문자열 -> sql구문 변경
			String sql = "insert into board(num,name,subject,content,readcount,date) values(?,?,?,?,?,?)";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, boardDTO.getNum());      //(물음표 순서,값)
			pstmt.setString(2, boardDTO.getName()); 
			pstmt.setString(3, boardDTO.getSubject());
			pstmt.setString(4, boardDTO.getContent());
			pstmt.setInt(5, boardDTO.getReadcount());
			pstmt.setTimestamp(6, boardDTO.getDate());
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

	public int getMaxNum() {
		int num = 0;
		try {
			// 1단계 JDBC 프로그램 가져오기 
			// 2단계 디비 연결
			con=getConnection();
			// 3단계 문자열 -> sql구문 변경
			String sql = "select max(num) from board;";
			pstmt=con.prepareStatement(sql);
			//4단계 sql구문 실행 => 실행결과 ResultSet 내장객체에 저장
			rs =pstmt.executeQuery();
			//5단계 : if  행 접근 -> 데이터 있으면 true 
			if(rs.next()) {
				num = rs.getInt("max(num)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			// 예외 상관 없이 마무리 작업 
			//  => con, pstmt, rs 기억장소 해제
			dbClose();
		}
		return num;
	}

	public List<BoardDTO> getBoardList() {
		List<BoardDTO> boardList = null;
		try {
			// 1단계 JDBC 프로그램 가져오기 
			// 2단계 디비 연결
			con=getConnection();
			// 3단계  num 기준으로 내림차순
			String sql = "select * from board order by num desc";
			pstmt=con.prepareStatement(sql);
			// //4단계 sql구문 실행 => 실행결과 ResultSet 내장객체에 저장
			rs =pstmt.executeQuery();
			// 5단계
			// 배열 객체생성
			boardList = new ArrayList<>();
			while(rs.next()) {
				BoardDTO boardDTO =new BoardDTO();
				boardDTO.setNum(rs.getInt("num"));
				boardDTO.setName(rs.getString("name"));
				boardDTO.setSubject(rs.getString("subject"));
				boardDTO.setContent(rs.getString("content"));
				boardDTO.setReadcount(rs.getInt("readcount"));
				boardDTO.setDate(rs.getTimestamp("date"));
				//배열 한칸 저장
				boardList.add(boardDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			// 예외 상관 없이 마무리 작업 
			//  => con, pstmt, rs 기억장소 해제
			dbClose();
		}
		return boardList;
	}

	public BoardDTO getBoard(int num) {
		BoardDTO boardDTO = null;
		try {
			// 1단계 JDBC 프로그램 가져오기 
			// 2단계 디비 연결
			con=getConnection();
			// 3단계  num 기준으로 내림차순
			String sql = "select * from board where num = ?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);
			// //4단계 sql구문 실행 => 실행결과 ResultSet 내장객체에 저장
			rs =pstmt.executeQuery();
			//5단계
			if(rs.next()) {
				boardDTO = new BoardDTO();
				boardDTO.setNum(rs.getInt("num"));
				boardDTO.setName(rs.getString("name"));
				boardDTO.setSubject(rs.getString("subject"));
				boardDTO.setContent(rs.getString("content"));
				boardDTO.setReadcount(rs.getInt("readcount"));
				boardDTO.setDate(rs.getTimestamp("date"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			dbClose();
		}
		return boardDTO;
	}

	public void updateReadcount(int num) {
		try {
			// 1단계 JDBC 프로그램 가져오기 
			// 2단계 디비 연결
			con=getConnection();
						
			// 3단계 문자열 -> sql구문 변경
			String sql = "update board set readcount=readcount+1 where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);      //(물음표 순서,값)
			
			// 4단계 sql구문 실행
			pstmt.executeUpdate();
						
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			dbClose();
		}
		
	}

	public void updateBoard(BoardDTO boardDTO) {
		try {
			// => BoardDAO updateBoard() 1,2 디비연결,
			// 1단계 JDBC 프로그램 가져오기 
			// 2단계 디비 연결
			con=getConnection();
			//    3 sql구문 update board  set subject,content수정 where num =
			String sql = "update board set subject=?,content=? where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, boardDTO.getSubject());
			pstmt.setString(2, boardDTO.getContent());
			pstmt.setInt(3, boardDTO.getNum());      //(물음표 순서,값)
			//    4 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			dbClose();
		}
	}

	public void deleteBoard(int num) {
		try {
		//  BoardDAO => deleteBoard() 메서드 정의
		//             1,2 디비연결, 3sql delete 4실행
			// 1단계 JDBC 프로그램 가져오기 
			// 2단계 디비 연결
			con=getConnection();
			// 3
			String sql = "delete from board where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);  
			// 4
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			dbClose();
		}
	}
	
}//class
