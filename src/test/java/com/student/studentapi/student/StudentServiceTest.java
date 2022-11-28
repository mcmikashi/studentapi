package com.student.studentapi.student;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock  StudentRepository  studentRepository;
    private StudentService underTest;
    public String defautEmail = "jhondoe@test.com";
    public Student defaulStudentWithId = new Student(1L,"jhon", "doe", LocalDate.of(1990,8,9), defautEmail);
    public Student defaulStudentWithoutId = new Student("jhon", "doe", LocalDate.of(1990,8,9), defautEmail);

    @BeforeEach
    void setUp(){
        underTest = new StudentService(studentRepository);
    }

    @Test
    void testAddNewStuddent() {
        underTest.addNewStuddent(defaulStudentWithoutId);
        ArgumentCaptor<String> emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).findStudentByEmail(emailArgumentCaptor.capture());
        assertTrue(defaulStudentWithoutId.getEmail().equals(emailArgumentCaptor.getValue()));
        verify(studentRepository).save(studentArgumentCaptor.capture());
        assertTrue(defaulStudentWithoutId.equals(studentArgumentCaptor.getValue()));
    }

    @Test
    void testAddNewStuddentAlreadyUsedEmail() {
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            when(studentRepository.findStudentByEmail(defautEmail)).thenReturn(Optional.of(defaulStudentWithId));
            underTest.addNewStuddent(defaulStudentWithoutId);
        });
 
        Assertions.assertEquals("This e-mail is already taken.", exception.getMessage());
    }

    @Test
    void testGetStudents() {
        underTest.getStudents();
        verify(studentRepository).findAll();
    }

    @Test
    void testUpdateStudentData() {
        Student newStudentData = new Student("jhon", "wick", LocalDate.of(1980, 10, 10), "jhon.wick@test.com");
        when(studentRepository.findById(defaulStudentWithId.getId())).thenReturn(Optional.of(defaulStudentWithId));
        underTest.updateStudentData(defaulStudentWithId.getId(), newStudentData);
        ArgumentCaptor<Student> updatedStudentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(updatedStudentArgumentCaptor.capture());
        assertTrue(updatedStudentArgumentCaptor.getValue().getId().equals(defaulStudentWithId.getId()));
        assertTrue(updatedStudentArgumentCaptor.getValue().getFirstName().equals(newStudentData.getFirstName()));
        assertTrue(updatedStudentArgumentCaptor.getValue().getLastName().equals(newStudentData.getLastName()));
        assertTrue(updatedStudentArgumentCaptor.getValue().getDateOfBirth().equals(newStudentData.getDateOfBirth()));
        assertTrue(updatedStudentArgumentCaptor.getValue().getEmail().equals(newStudentData.getEmail()));

    }

    @Test
    void testUpdateStudentDataBadId(){
        Student newStudentData = new Student("jhon", "wick", LocalDate.of(1980, 10, 10), "jhon.wick@test.com");
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            underTest.updateStudentData(defaulStudentWithId.getId(), newStudentData);
        });
        Assertions.assertEquals("student with id "+ defaulStudentWithId.getId() + " doesn't exist.", exception.getMessage());
    }

    @Test
    void testDeleteStudent() {
        when(studentRepository.findById(defaulStudentWithId.getId())).thenReturn(Optional.of(defaulStudentWithId));
        underTest.deleteStudent(defaulStudentWithId.getId());
        ArgumentCaptor<Long> studentIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(studentRepository).deleteById(studentIdArgumentCaptor.capture());
        assertTrue(studentIdArgumentCaptor.getValue().equals(defaulStudentWithId.getId()));
    }

    @Test
    void testDeleteStudentBadId() {        
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            underTest.deleteStudent(defaulStudentWithId.getId());
        });
        Assertions.assertEquals("student with id "+ defaulStudentWithId.getId() + " doesn't exist.", exception.getMessage());
    }
}
