<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>board/write.jsp</title>
</head>
<body>
<%
//세션에서 로그인정보 가져오기
String id=(String)session.getAttribute("id");
%>
<h1>글쓰기(로그인 : <%=id %>)</h1>
<form action="writePro.bo" method="post">
<table border="1">
<tr><td>글쓴이</td>
<td><input type="text" name="name" value="<%=id%>" readonly></td></tr>
<tr><td>글제목</td>
    <td><input type="text" name="subject"></td></tr>
<tr><td>글내용</td>
<td><textarea name="content" rows="10" cols="20"></textarea></td></tr>
<tr><td colspan="2"><input type="submit" value="글쓰기"></td></tr>    
</table>
</form>
</body>
</html>

