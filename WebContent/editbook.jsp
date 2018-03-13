<%@page import="com.gcit.lms.entity.Book"%>
<%@page import="com.gcit.lms.service.AdminService"%>
<%AdminService service = new AdminService();
 Book book = service.readBookByPK1(Integer.parseInt(request.getParameter("bookId")));
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>LMS</title>
</head>
<body>
	<h1>Welcome to GCIT Library Management System!</h1>
	<h2>Edit book</h2>
	<form method="post" action="editBook">
		${statusMessage}
		<br/>Enter book Name to Edit: <input type="text" name="title" value="<%=book.getTitle()%>"><br />
		<input type="hidden" name="bookId" value="<%=book.getBookId()%>">
		<button type="submit">Update book</button>
	</form>

</body>
</html>