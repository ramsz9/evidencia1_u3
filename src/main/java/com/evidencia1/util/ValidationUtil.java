package com.evidencia1.util;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import com.evidencia1.dto.StudentDto;
import com.evidencia1.exception.BusinessException;
import java.util.regex.Pattern;

@Component
public class ValidationUtil {

    public void validate(StudentDto studentDto) throws BusinessException {

        if (Strings.isBlank(studentDto.eMail())
                || !Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                        studentDto.eMail())) {
            throw new BusinessException("El email no tiene un formato válido");
        }

        if (Strings.isBlank(studentDto.name())) {
            throw new BusinessException("El nombre no debe estar vacío");
        }

    }

}
