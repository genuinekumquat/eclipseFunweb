<%@page import="com.itwillbs.domain.BoardDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jsp4/update.jsp</title>
</head>
<body>
<%
//세션에서 로그인정보 가져오기
String id=(String)session.getAttribute("id");
// // update.jsp?num=1
// // num=1 사용자가 선택한 정보를 http가 들고와서 서버에 request 에 저장
// // request num 가져와서 -> int num 변수에 저장
// int num= Integer.parseInt(request.getParameter("num"));
// //1단계 JDBC 프로그램 가져오기 
// Class.forName("com.mysql.cj.jdbc.Driver");
// // 2단계 디비 연결
// String dbUrl="jdbc:mysql://localhost:3306/jspdb?serverTimezone=Asia/Seoul";
// String dbUser="root";
// String dbPass="1234";
// Connection con=DriverManager.getConnection(dbUrl, dbUser, dbPass);

// // 3단계 문자열 -> sql구문 변경  select * from board where num=?
// String sql = "select * from board where num=?";
// PreparedStatement pstmt=con.prepareStatement(sql);
// pstmt.setInt(1, num);
// //4단계 sql구문 실행 => 실행결과 ResultSet 내장객체에 저장	
// ResultSet rs =pstmt.executeQuery();
// //5단계 : if  행 접근 -> 데이터 있으면 true 
// //     -> 글번호 글쓴이 글제목 글내용 출력
// if(rs.next()){
	BoardDTO boardDTO = (BoardDTO)request.getAttribute("boardDTO");
	%>
	<h1>글수정(로그인 : <%=id %>)</h1>
<form action="updatePro.bo" method="post">
<input type="hidden" name="num" value="<%=boardDTO.getNum()%>">
<table border="1">
<tr><td>글쓴이</td>
<td><input type="text" name="name" value="<%=boardDTO.getName()%>" readonly></td></tr>
<tr><td>글제목</td>
    <td><input type="text" name="subject" value="<%=boardDTO.getSubject()%>"></td></tr>
<tr><td>글내용</td>
<td><textarea name="content" rows="10" cols="20"><%=boardDTO.getContent() %></textarea></td></tr>
<tr><td colspan="2"><input type="submit" value="글수정"></td></tr>    
</table>
</form>
	
	
	<%
// }
%>

</body>
</html>