<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Timestamp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jsp4/writePro.jsp</title>
</head>
<body>
<%
//사용자가 입력한 정보를 http가 들고와서 서버에 request 에 저장
//request 한글처리
request.setCharacterEncoding("utf-8");
//request name, subject,content 가져와서 -> 변수에 저장
String name = request.getParameter("name");
String subject = request.getParameter("subject");
String content = request.getParameter("content");
//조회수 
int readcount = 0;
// 글쓴날짜 -> 시스템날짜 java.sql.Timestamp
Timestamp date=
    new Timestamp(System.currentTimeMillis());
//게시판 글번호 
int num = 1;
//1단계 JDBC 프로그램 가져오기 
Class.forName("com.mysql.cj.jdbc.Driver");
// 2단계 디비 연결
String dbUrl="jdbc:mysql://localhost:3306/jspdb?serverTimezone=Asia/Seoul";
String dbUser="root";
String dbPass="1234";
Connection con=DriverManager.getConnection(dbUrl, dbUser, dbPass);

//3단계 -> sql select max(num) from board
String sql2 = "select max(num) from board";
PreparedStatement pstmt2=con.prepareStatement(sql2);
//4단계 실행 -> 결과 저장
ResultSet rs=pstmt2.executeQuery();
//5단계 다음행 접근 -> 데이터있음 true -> max(num) 열 가져와서 + 1
if(rs.next()){
	num=rs.getInt("max(num)")+1;
}

// 3단계 문자열 -> sql구문 변경 insert into board 
String sql = "insert into board(num,name,subject,content,readcount,date) values(?,?,?,?,?,?)";
PreparedStatement pstmt=con.prepareStatement(sql);
pstmt.setInt(1, num);      //(물음표 순서,값)
pstmt.setString(2, name); 
pstmt.setString(3, subject);
pstmt.setString(4, content);
pstmt.setInt(5, readcount);
pstmt.setTimestamp(6, date);
// 4단계 sql구문 실행
pstmt.executeUpdate();
// list.jsp이동
response.sendRedirect("list.jsp");
%>
</body>
</html>


