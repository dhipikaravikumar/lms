<%@include file="include.html"%>
<%@page import="com.gcit.lms.entity.Book"%>
<%@page import="com.gcit.lms.service.AdminService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.entity.Author"%>
<%@page import="com.gcit.lms.entity.Borrower"%>
<%
	AdminService service = new AdminService();
Integer totalCount = service.getBorrowersCount();
	int numOfPages = 0;
	if(totalCount%10 > 0){
		numOfPages = totalCount/10 +1;
	}else{
		numOfPages = totalCount/10;
	}
	List<Borrower> borrowers = new ArrayList<>();
	if(request.getAttribute("borrowers")!=null){
		borrowers = (List<Borrower>)request.getAttribute("borrowers");
	}else{
		borrowers = service.readBorrowers(null, 1);
	}
%>
<%
	if (request.getAttribute("statusMessage") != null) {
		out.println(request.getAttribute("statusMessage"));
	}
%>
<div class="container">
	<h1>List of Borrowers in LMS&nbsp;&nbsp;&nbsp;&nbsp; Total Borrowers in LMS: <%=totalCount%> Borrower</h1>
	<nav aria-label="Page navigation example">
		<ul class="pagination">
			<li class="page-item"><a class="page-link" href="#"
				aria-label="Previous"> <span aria-hidden="true">&laquo;</span> <span
					class="sr-only">Previous</span>
			</a></li>
			<%for(int i=1; i<=numOfPages; i++){ %>
			<li class="page-item"><a class="page-link" href="pageBorrowers?pageNo=<%=i%>"><%=i%></a></li>
			<%} %>
			<li class="page-item"><a class="page-link" href="#"
				aria-label="Next"> <span aria-hidden="true">&raquo;</span> <span
					class="sr-only">Next</span>
			</a></li>
		</ul>
	</nav>
	<table class="table table-striped">
		<tr>
			<th>#</th>
			<th>Borrower Name</th>
			<th>Edit Borrower</th>
			<th>Delete Borrower</th>
		</tr>
		<%
			for (Borrower b : borrowers) {
		%>
		<tr>
			<td><%=borrowers.indexOf(b) + 1%></td>
			<td><%=b.getName()%></td>	 
			</td>
			<td><button type="button"
					onclick="javascript:location.href='editborrower.jsp?cardNo=<%=b.getCardNo()%>'"
					class="btn btn-primary btn-sm">Edit</button></td>
			<td><button type="button"
					onclick="javascript:location.href='deleteBorrower?cardNo=<%=b.getCardNo()%>'"
					class="btn btn-danger btn-sm">Delete</button></td>
		</tr>
		<%
			}
		%>
	</table>
</div>