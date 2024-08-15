package com.example.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.university.repository.*;
import com.example.university.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseJpaService implements CourseRepository {
    @Autowired
    private CourseJpaRepository courseJpaRepository;

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Autowired
    private ProfessorJpaRepository professorJpaRepository;

    @Override
    public ArrayList<Course> getCourses() {
        List<Course> coursesList = courseJpaRepository.findAll();
        ArrayList<Course> courses = new ArrayList<>(coursesList);
        return courses;
    }

    @Override
    public Course getCourseById(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            return course;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Course addCourse(Course course) {
        List<Integer> studentIds = new ArrayList<>();
        for (Student student : course.getStudents()) {
            studentIds.add(student.getStudentId());
        }
        List<Student> students = studentJpaRepository.findAllById(studentIds);
        if (students.size() != studentIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        course.setStudents(students);
        return courseJpaRepository.save(course);
    }

    @Override
    public Course updateCourse(int courseId, Course course) {
        try {
            Course newCourse = courseJpaRepository.findById(courseId).get();
            if (course.getCourseName() != null) {
                newCourse.setCourseName(course.getCourseName());
            }
            if (course.getCredits() != null) {
                newCourse.setCredits(course.getCredits());
            }
            if (course.getProfessor() != null) {
                newCourse.setProfessor(course.getProfessor());
            }
            if (course.getStudents() != null) {
                List<Integer> studentIds = new ArrayList<>();
                for (Student student : course.getStudents()) {
                    studentIds.add(student.getStudentId());
                }
                List<Student> students = studentJpaRepository.findAllById(studentIds);
                if (students.size() != studentIds.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                newCourse.setStudents(students);
            }
            return courseJpaRepository.save(newCourse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteCourse(int courseId) {
        try {
            courseJpaRepository.deleteById(courseId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Professor getCourseProfessor(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            return (Professor) professorJpaRepository.findByCourse(course);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Student> getCourseStudents(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            return course.getStudents();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
