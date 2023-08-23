<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jsp4/updatePro.jsp</title>
</head>
<body>
<%
//http://localhost:8080/webProject/jsp4/
// updatePro.jsp?num=1&name=kim&subject=제목&content=내용
//사용자가 수정한 정보를 http가 들고와서 서버에 request 에 저장
// request 한글처리
request.setCharacterEncoding("utf-8");
//request num , name, subject, content 가져와서 -> 변수에 저장
int num= Integer.parseInt(request.getParameter("num"));
String name = request.getParameter("name");
String subject = request.getParameter("subject");
String content = request.getParameter("content");

//1단계 JDBC 프로그램 가져오기 
Class.forName("com.mysql.cj.jdbc.Driver");
// 2단계 디비 연결
String dbUrl="jdbc:mysql://localhost:3306/jspdb?serverTimezone=Asia/Seoul";
String dbUser="root";
String dbPass="1234";
Connection con=DriverManager.getConnection(dbUrl, dbUser, dbPass);

// 3단계 문자열 -> sql구문 변경 
//update board set subject=?, content=? where num=? 
String sql = "update board set subject=?, content=? where num=?";
PreparedStatement pstmt=con.prepareStatement(sql);
pstmt.setString(1, subject);
pstmt.setString(2, content);
pstmt.setInt(3, num);
// 4단계 sql구문 실행	
pstmt.executeUpdate();
// list.jsp이동
response.sendRedirect("list.jsp");
%>
</body>
</html>

