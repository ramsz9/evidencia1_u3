package com.evidencia1.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.evidencia1.dto.StudentDto;
import com.evidencia1.dto.StudentRsDto;
import com.evidencia1.model.StudentModel;
import com.evidencia1.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentDto> findAll() {
        return studentRepository.findAll().stream().map(StudentModel::toDto).toList();
    }

    public StudentRsDto create(StudentDto dto) {
        Integer id = studentRepository.save(dto.toModel()).getId();
        return new StudentRsDto("Student added", id);
    }

    public void deleteById(Integer id) {
        studentRepository.deleteById(id);
    }

}
