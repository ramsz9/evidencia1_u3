package com.evidencia1.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evidencia1.util.ValidationUtil;
import com.evidencia1.dto.StudentDto;
import com.evidencia1.dto.StudentRsDto;
import com.evidencia1.exception.BusinessException;
import com.evidencia1.service.StudentService;

@RestController
@RequestMapping(value = "/student")
public class StudentController {

    private final StudentService studentService;
    private final ValidationUtil validationUtil;

    public StudentController(StudentService studentService, ValidationUtil validationUtil) {
        this.studentService = studentService;
        this.validationUtil = validationUtil;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<StudentDto> studentDtos = studentService.findAll();
        return new ResponseEntity<>(studentDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody StudentDto studentDto) throws BusinessException {
        validationUtil.validate(studentDto);
        StudentRsDto rsDto = studentService.create(studentDto);
        return new ResponseEntity<>(rsDto, HttpStatus.OK);
    }

}
