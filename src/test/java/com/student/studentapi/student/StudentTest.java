package com.student.studentapi.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Period;

import org.junit.jupiter.api.Test;

public class StudentTest {
    
    public Student student = new Student(1L, "jhon", "doe", LocalDate.of(2000, 01, 01), "jhon.doe@test.com");

    @Test
    void testGetAge() {
        assertEquals(Period.between(student.getDateOfBirth(), LocalDate.now()).getYears(), student.getAge());
    }

    @Test
    void testGetFullName() {
        assertTrue(student.getFullName().equals(student.getFirstName() + " " + student.getLastName()));
    }

    @Test
    void testToString() {
        assertTrue(student.toString().equals(
            "Student [id=" + student.getId() + ", firstName=" + student.getFirstName() + ", lastName=" + student.getLastName() + ", dateOfBirth="+ student.getDateOfBirth() + ", email=" + student.getEmail() + "]"
        ));
    }
}
