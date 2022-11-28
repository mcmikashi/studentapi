package com.student.studentapi.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @Test
    void testFindStudentByEmailValidEmail() {
        String emailToTest = "jhon.doe@test.com";

        Student student = new Student("jhon", "doe", LocalDate.of(1990, 04, 16), emailToTest);

        underTest.save(student);

        assertTrue(underTest.findStudentByEmail(emailToTest).isPresent());
    }

    @Test
    void testFindStudentByEmailInvalidEmail() {
        String notRegistedEmail = "notregisted@gtest.com";

        assertFalse(underTest.findStudentByEmail(notRegistedEmail).isPresent());
    }
}
