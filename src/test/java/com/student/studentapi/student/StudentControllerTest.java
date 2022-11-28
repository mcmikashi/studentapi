package com.student.studentapi.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    public Student defaulStudentWithId = new Student(1L, "jhon", "doe", LocalDate.of(1990, 8, 9), "jhon.doe@test.com");
    public Student defaulStudentWitouthId = new Student("jhon", "doe", LocalDate.of(1990, 8, 9), "jhon.doe@test.com");
    public List<Student> studentsList = List.of(defaulStudentWithId);
    
    @Test
    void testGetStudents() throws Exception {

        final String expectedResponseContent = objectMapper.writeValueAsString(studentsList);

        when(this.studentService.getStudents())
                .thenReturn(studentsList);
        
        this.mvc.perform(get("/api/v1/students").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponseContent));
        verify(studentService).getStudents();
    }
    @Test
    void testAddNewStudent() throws Exception {

        final String dataToBePost = objectMapper.writeValueAsString(defaulStudentWitouthId);
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/students")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dataToBePost);
        this.mvc.perform(mockRequest)
                .andExpect(status().isOk());
        verify(studentService).addNewStuddent(studentArgumentCaptor.capture());
        assertTrue(studentArgumentCaptor.getValue().toString().equals(defaulStudentWitouthId.toString()));
    }

    @Test
    void testDeleteStudent() throws Exception{
        ArgumentCaptor<Long> studentIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        this.mvc.perform(delete("/api/v1/students/{id}", defaulStudentWithId.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(studentService).deleteStudent(studentIdArgumentCaptor.capture());
        assertEquals(defaulStudentWithId.getId(), studentIdArgumentCaptor.getValue());

    }

    @Test
    void testUpdateStudentData() throws Exception {

        Student updatedStudentData = new Student("jack", "wick", LocalDate.of(1990, 10, 25), "jackwick@test.com");
        final String dataToBePatch = objectMapper.writeValueAsString(updatedStudentData);
        ArgumentCaptor<Long> studentIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Student> studentNewDataArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/v1/students/{id}", defaulStudentWithId.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dataToBePatch);
    
        this.mvc.perform(mockRequest)
                .andExpect(status().isOk());
        verify(studentService).updateStudentData(studentIdArgumentCaptor.capture(), (studentNewDataArgumentCaptor.capture()));
        assertEquals(defaulStudentWithId.getId(), studentIdArgumentCaptor.getValue());
        assertEquals(defaulStudentWithId.getId(), studentIdArgumentCaptor.getValue());
        assertTrue(studentNewDataArgumentCaptor.getValue().toString().equals(updatedStudentData.toString()));

    }
}
