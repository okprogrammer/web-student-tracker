package com.code.web.jdbc.student.controller;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import com.code.web.jdbc.student.model.Student;
import com.code.web.jdbc.student.util.StudentDbUtil;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDbUtil studentDbUtil;
	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();
		// create our student db util ... and passs in the conn pool/datasource
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * Default constructor.
	 */
	public StudentControllerServlet() {
		// list the students .. in MVC fashion
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// list the students .. in MVC fashion
		try {
			// read the "command" parameter
			String theCommand = request.getParameter("command");
			// if the command is missing, then default to listing
			if (theCommand == null) {
				theCommand = "LIST";
			}
			// route to the appropriate method
			switch (theCommand) {
			case "LIST":
				listStudents(request, response);
				break;
			case "ADD":
				addStudent(request, response);
				break;
			case "LOAD":
				loadStudent(request, response);
				break;
			case "UPDATE":
				updateStudent(request, response);
				break;
			case "DELETE":
				deleteStudent(request, response);
				break;
			case "SEARCH":
				searchStudents(request, response);
				break;
			default:
				listStudents(request, response);
				break;
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

	private void searchStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read the search word
		String theSearchName = request.getParameter("theSearchName");
		// search students from db util
		List<Student> students = studentDbUtil.getStudents(theSearchName);
		// add students to the request
		request.setAttribute("STUDENT_LIST", students);
		// send to jsp page view
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student in from form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		// perform delete operation on database
		studentDbUtil.deleteStudent(id);
		// send back to "list students" page
		listStudents(request, response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		// create a new student object
		Student theStudent = new Student(id, firstName, lastName, email);
		// perform update on database
		studentDbUtil.updateStudent(theStudent);
		// send them backt to "list students" page
		listStudents(request, response);

	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student id from form data
		String theStudentId = request.getParameter("studentId");
		// get student from database (db util)
		Student theStudent = studentDbUtil.getStudent(theStudentId);
		// place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		// sent to jsp page: update-student-from.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read the student info form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		// create a new student object
		Student theStudent = new Student(firstName, lastName, email);

		// add the student to the database
		studentDbUtil.addStudent(theStudent);

		// send back to main page(the student list)
		listStudents(request, response);
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// get the students from db util
		List<Student> students = studentDbUtil.getStudents();
		// add student to the request
		request.setAttribute("STUDENT_LIST", students);
		// send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}

}
