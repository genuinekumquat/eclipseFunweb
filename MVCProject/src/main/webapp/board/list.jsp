<%@page import="com.itwillbs.domain.BoardDTO"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jsp4/list.jsp</title>
</head>
<body>
<%
//세션에서 로그인정보 가져오기
String id=(String)session.getAttribute("id");
%>
<h1>게시판 글목록(로그인 : <%=id %>)</h1>
<%
// //1단계 JDBC 프로그램 가져오기 
// Class.forName("com.mysql.cj.jdbc.Driver");
// // 2단계 디비 연결
// String dbUrl="jdbc:mysql://localhost:3306/jspdb?serverTimezone=Asia/Seoul";
// String dbUser="root";
// String dbPass="1234";
// Connection con=DriverManager.getConnection(dbUrl, dbUser, dbPass);

// // 3단계 문자열 -> sql구문 변경
// //  select * from board 
// String sql = "select * from board";
// PreparedStatement pstmt=con.prepareStatement(sql);
// //4단계 sql구문 실행 => 실행결과 ResultSet 내장객체에 저장
// ResultSet rs =pstmt.executeQuery();
// 다운캐스팅
List<BoardDTO> boardList
     =(List<BoardDTO>)request.getAttribute("boardList");
%>
<table border="1">
<tr><td>글번호</td><td>글쓴이</td><td>제목</td>
    <td>조회수</td><td>글쓴날짜</td></tr>
    <%
  //5단계 : while  행 접근 -> 데이터 있으면 true 
  //-> 글번호 글쓴이 제목 조회수 글쓴날짜 출력 
    for(int i=0;i<boardList.size();i++){
    	BoardDTO boardDTO=boardList.get(i);
	  %>
<tr><td><%=boardDTO.getNum() %></td>
    <td><%=boardDTO.getName() %></td>
    <td>
    <a href="content.bo?num=<%=boardDTO.getNum() %>">
    <%=boardDTO.getSubject() %></a>
    </td>
    <td><%=boardDTO.getReadcount() %></td>
    <td><%=boardDTO.getDate() %></td></tr> 	  
	  <%
  }
    %>
   
</table>
</body>
</html>