
<%@include file="include.html"%>

<div class="container">
	<table>
		<tr>
			<td><a href="updatelibrary.jsp">Update Details Of The Library </a></td>
		</tr>
	
			<td><a href="addcopies.jsp">Add Copies of Book To The Branch</a></td>
		</tr>
		 
	</table>
	<input type="hidden" name="branchId" value="<%=request.getParameter("branchIds")%>">
</div>
</head>
<body>

</body>
</html>