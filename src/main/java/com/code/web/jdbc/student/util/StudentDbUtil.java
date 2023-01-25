package com.code.web.jdbc.student.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.code.web.jdbc.student.model.Student;

public class StudentDbUtil {
	private DataSource dataSource;

	public StudentDbUtil(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List<Student> getStudents() throws Exception {
		List<Student> students = new ArrayList<>();
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRes = null;
		try {
			// get a connection
			myConn = dataSource.getConnection();
			String sql = "Select * from student order by last_name";
			// create sql statement
			myStmt = myConn.createStatement();
			// execute the query
			myRes = myStmt.executeQuery(sql);
			// process the result set
			while (myRes.next()) {
				// retrieve the data from result set row
				int id = myRes.getInt("id");
				String firstName = myRes.getString("first_name");
				String lastName = myRes.getString("last_name");
				String email = myRes.getString("email");
				// create new Student object and it to list
				students.add(new Student(id, firstName, lastName, email));

			}
			// close JDBC object
			return students;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			close(myConn, myStmt, myRes);
		}
		return students;
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRes) {
		try {
			if (myRes != null) {
				myRes.close();
			}
			if (myStmt != null) {
				myStmt.close();
			}
			if (myConn != null) {
				// doesn't really close it......just puts back to connection pool
				myConn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void addStudent(Student theStudent) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try {
			// get db connection
			myConn = dataSource.getConnection();
			// create sql for insert
			String sql = "insert into student" + " (first_name, last_name, email)" + "values(?,?,?)";
			myStmt = myConn.prepareStatement(sql);
			// set the param values for the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			// execute the sql insert
			myStmt.execute();
		} finally {
			close(myConn, myStmt, null);
		}

	}

	public Student getStudent(String theStudentId) throws Exception {
		Student theStudent = null;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRes = null;
		int studentId;
		try {
			// convert student id to int
			studentId = Integer.parseInt(theStudentId);
			// get connection to database
			myConn = dataSource.getConnection();
			// create sql to get selected student
			String sql = "Select * from student where id=?";
			// create prepared statement
			myStmt = myConn.prepareStatement(sql);
			// set params
			myStmt.setLong(1, studentId);
			// execute statement
			myRes = myStmt.executeQuery();
			// retrieve data from result set row
			if (myRes.next()) {
				theStudent = new Student(studentId, myRes.getString("first_name"), myRes.getString("last_name"),
						myRes.getString("email"));
			} else {
				throw new Exception("Could not find student id: " + studentId);
			}
			return theStudent;
		} finally {
			// clean up JDBC object
			close(myConn, myStmt, myRes);
		}
	}

	public void updateStudent(Student theStudent) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try {
			// get db connection
			myConn = dataSource.getConnection();
			// create sql update statement
			String sql = "update student " + "set first_name=?,last_name=?,email=? " + "where id=?";
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			// set params
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			// execute the sql statement
			myStmt.execute();
		} finally {
			close(myConn, myStmt, null);
		}

	}

	public void deleteStudent(int id) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try {
			// get db connection
			myConn = dataSource.getConnection();
			// create sql to delete statement
			String sql = "delete from student where id=?";
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			// set params
			myStmt.setInt(1, id);
			// execute the sql statement
			myStmt.execute();
		} finally {
			close(myConn, myStmt, null);
		}

	}

	public List<Student> getStudents(String theSearchName) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet res = null;
		List<Student> students = new ArrayList<>();
		try {
			// get db connection
			myConn = dataSource.getConnection();
			if (theSearchName != null && theSearchName.trim().length() > 0) {
				// create sql statement
				String sql = "select * from student where lower(first_name) like ? or lower(last_name) like ?";
				// prepare statement
				myStmt = myConn.prepareStatement(sql);
				// set params
				String theSearchNameLike = "%" + theSearchName.toLowerCase() + "%";
				myStmt.setString(1, theSearchNameLike);
				myStmt.setString(2, theSearchNameLike);
			} else {
				// create sql statement for all students
				String sql = "select * from student";
				// parepare statement
				myStmt = myConn.prepareStatement(sql);
			}
			// execute the statement
			res = myStmt.executeQuery();
			while (res.next()) {
				students.add(new Student(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("email")));
			}
			return students;
		} finally {
			close(myConn, myStmt, res);
		}
	}
}
