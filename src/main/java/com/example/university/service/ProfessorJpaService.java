package com.example.university.service;

import com.example.university.repository.*;
import com.example.university.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfessorJpaService implements ProfessorRepository {
    @Autowired
    private ProfessorJpaRepository professorJpaRepository;

    @Override
    public ArrayList<Professor> getProfessors() {
        List<Professor> professorsList = professorJpaRepository.findAll();
        ArrayList<Professor> professors = new ArrayList<>(professorsList);
        return professors;
    }

    @Override
    public Professor getProfessorById(int professorId) {
        try {
            Professor professor = professorJpaRepository.findById(professorId).get();
            return professor;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Professor addProfessor(Professor professor) {
        professorJpaRepository.save(professor);
        return professor;
    }

    @Override
    public Professor updateProfessor(int professorId, Professor professor) {
        Professor newProfessor = professorJpaRepository.findById(professorId).get();
        try {
            if (professor.getProfessorName() != null) {
                newProfessor.setProfessorName(professor.getProfessorName());
            }
            if (professor.getDepartment() != null) {
                newProfessor.setDepartment(professor.getDepartment());
            }
            professorJpaRepository.save(newProfessor);
            return newProfessor;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteProfessor(int professorId) {
        try {
            professorJpaRepository.deleteById(professorId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Course> getProfessorCourses(int professorId) {
        try {
            Professor professor = professorJpaRepository.findById(professorId).get();
            return ((Object) professor).findByCourse();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
