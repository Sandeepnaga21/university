package com.example.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.example.university.repository.*;
import com.example.university.model.*;

@Service
public class StudentJpaService implements StudentRepository {
	@Autowired
	private StudentJpaRepository studentJpaReposistory;

	@Autowired
	private CourseJpaRepository courseJpaRepository;

	@Override
	public ArrayList<Student> getStudents() {
		List<Student> studentsList = studentJpaReposistory.findAll();
		ArrayList<Student> students = new ArrayList<>(studentsList);
		return students;
	}

	@Override
	public Student getStudentById(int studentId) {
		try {
			Student student = studentJpaReposistory.findById(studentId).get();
			return student;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public Student addStudent(Student student) {
		List<Integer> courseIds = new ArrayList<>();
		for (Course course : student.getCourses()) {
			courseIds.add(course.getCourseId());
		}
		List<Course> courses = courseJpaRepository.findAllById(courseIds);

		if (courseIds.size() != courses.size()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		student.setCourses(courses);
		return studentJpaReposistory.save(student);
	}

	@Override
	public Student updateStudent(int studentId, Student student) {
		try {
			Student newStudent = studentJpaReposistory.findById(studentId).get();
			if (student.getStudentName() != null) {
				newStudent.setStudentName(student.getStudentName());
			}
			if (student.getEmail() != null) {
				newStudent.setEmail(student.getEmail());
			}
			if (student.getCourses() != null) {
				List<Integer> courseIds = new ArrayList<>();
				for (Course course : student.getCourses()) {
					courseIds.add(course.getCourseId());
				}
				List<Course> courses = courseJpaRepository.findAllById(courseIds);

				if (courseIds.size() != courses.size()) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
				}
				newStudent.setCourses(courses);
			}
			return studentJpaReposistory.save(newStudent);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void deleteStudent(int studentId) {
		try {
			Student student = studentJpaReposistory.findById(studentId).get();
			List<Course> courses = student.getCourses();
			for (Course course : courses) {
				course.getStudents().remove(student);
			}
			courseJpaRepository.saveAll(courses);
			studentJpaReposistory.deleteById(studentId);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		throw new ResponseStatusException(HttpStatus.NO_CONTENT);
	}

	@Override
	public List<Course> getStudentCourses(int studentId) {
		try {
			Student student = studentJpaReposistory.findById(studentId).get();
			List<Course> courses = student.getCourses();
			return courses;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}
