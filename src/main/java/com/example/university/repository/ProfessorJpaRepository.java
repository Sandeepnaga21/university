package com.example.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.university.model.*;
import java.util.*;

@Repository
public interface ProfessorJpaRepository extends JpaRepository<Professor, Integer> {

}
