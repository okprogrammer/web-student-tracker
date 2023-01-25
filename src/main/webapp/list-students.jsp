<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.*, com.code.web.jdbc.student.model.Student"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Student Tracker App</title>
<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<body>
	<div id="wrapper">
		<div id="header">
			<h2>FooBar University</h2>
		</div>
	</div>
	<div id="container">
		<div id="content">
			<!-- put new button: Add Student -->
			<input type="button" value="Add Student"
				onclick="window.location.href='add-student-form.jsp'; return false;"
				class="add-student-button" />
			<form action="StudentControllerServlet" method="GET">
				<input type="hidden" name="command" value="SEARCH" /> Search
				Student: <input type="text" name="theSearchName" /> <input
					type="submit" value="Search" class="add-student-button" />
			</form>
			<table>
				<tr>
					<th>Fist Name</th>
					<th>Last Name</th>
					<th>Email</th>
					<th>Action</th>
				</tr>
				<c:forEach var="stud" items="${STUDENT_LIST}">
					<!-- set up a link for each student -->
					<c:url var="tempLink" value="StudentControllerServlet">
						<c:param name="command" value="LOAD" />
						<c:param name="studentId" value="${stud.id}" />
					</c:url>
					<!-- set up a link to delete a student -->
					<c:url var="delLink" value="StudentControllerServlet">
						<c:param name="command" value="DELETE" />
						<c:param name="studentId" value="${stud.id}" />
					</c:url>
					<tr>
						<td>${stud.firstName}</td>
						<td>${stud.lastName}</td>
						<td>${stud.email}</td>
						<td><a href="${tempLink}">Update</a> | <a href="${delLink}"
							onclick="if (!(confirm('Are you sure you want to delete this student?'))) return false">Delete</a></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
</body>
</html>