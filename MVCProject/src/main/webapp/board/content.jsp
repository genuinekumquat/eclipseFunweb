<%@page import="com.itwillbs.domain.BoardDTO"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jsp4/content.jsp</title>
</head>
<body>
<%
//세션에서 로그인정보 가져오기
String id=(String)session.getAttribute("id");
%>
<h1>글내용보기(로그인 : <%=id %>)</h1>
<%
BoardDTO boardDTO=(BoardDTO)request.getAttribute("boardDTO");
	%>
<table border="1">
<tr><td>글번호</td><td><%=boardDTO.getNum() %></td></tr>
<tr><td>글쓴이</td><td><%=boardDTO.getName() %></td></tr>
<tr><td>글쓴날짜</td><td><%=boardDTO.getDate() %></td></tr>
<tr><td>조회수</td><td><%=boardDTO.getReadcount() %></td></tr>
<tr><td>글제목</td><td><%=boardDTO.getSubject() %></td></tr>
<tr><td>글내용</td><td><%=boardDTO.getContent() %></td></tr>
<tr><td colspan="2">
<%
// 로그인 필수 
// 로그인, 글쓴이 일치하면 => 글수정 , 글삭제 버튼 보이기
if(id != null){
	if(id.equals(boardDTO.getName())){
		%>
<input type="button" value="글수정" onclick="location.href='update.bo?num=<%=boardDTO.getNum()%>'">
<input type="button" value="글삭제" onclick="location.href='delete.bo?num=<%=boardDTO.getNum()%>'">		
		<%
	}
}
%>
<input type="button" value="글목록" onclick="location.href='list.bo'">
</td></tr>
</table>	


</body>
</html>



