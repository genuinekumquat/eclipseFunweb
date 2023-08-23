<%@page import="com.itwillbs.domain.MemberDTO"%>
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
<title>member/update.jsp</title>
</head>
<body>
<h1>회원정보수정</h1>
<%
// // 로그인 정보를 session에서 가져오기
// String id=(String)session.getAttribute("id");

// //1단계 JDBC 프로그램 가져오기 
// Class.forName("com.mysql.cj.jdbc.Driver");
// //2단계 디비 연결
// String dbUrl="jdbc:mysql://localhost:3306/jspdb?serverTimezone=Asia/Seoul";
// String dbUser="root";
// String dbPass="1234";
// Connection con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
// //3단계 문자열 -> sql구문 변경
// //select * from members where id=폼입력id
// String sql = "select * from members where id=?";
// PreparedStatement pstmt=con.prepareStatement(sql);
// pstmt.setString(1, id);
// //4단계 sql구문 실행 => 실행결과 ResultSet 내장객체에 저장
// ResultSet rs =pstmt.executeQuery();
// //5단계 : if  행 접근 -> 데이터 있으면 true 
// //-> form 아이디 이름 출력
// if(rs.next()){
MemberDTO memberDTO=(MemberDTO)request.getAttribute("memberDTO");
	%>
<form action="updatePro.me" method="post">
아이디 : <input type="text" name="id" 
        value="<%=memberDTO.getId() %>" readonly><br>
비밀번호 : <input type="password" name="pass"><br>
이름 : <input type="text" name="name" 
        value="<%=memberDTO.getName()%>"><br>
<input type="submit" value="회원정보수정">
</form>	
	<%
// }
%>

</body>
</html>


