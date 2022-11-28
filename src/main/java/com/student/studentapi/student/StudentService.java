package com.student.studentapi.student;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

	private final StudentRepository studentRepository;

	@Autowired
	public StudentService(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	public List<Student> getStudents(){
		return studentRepository.findAll();
	}

	public void addNewStuddent(Student student) {
		Optional<Student> studentByEmail = studentRepository.findStudentByEmail(student.getEmail());
		if (studentByEmail.isPresent()) {
			throw new IllegalStateException("This e-mail is already taken.");
		}
		studentRepository.save(student);
	}

	public void deleteStudent(Long studentId) {
		Optional<Student> studentById = studentRepository.findById(studentId);
		if (studentById.isPresent()) {
			studentRepository.deleteById(studentId);
		}else{
			throw new IllegalStateException("student with id "+ studentId + " doesn't exist.");
		}
	}

	@Transactional
	public void updateStudentData(Long studentId, Student studentData) {
		Student studentToUpdate = (studentRepository.findById(studentId)).orElseThrow(() -> new IllegalStateException("student with id "+ studentId + " doesn't exist."));
		studentToUpdate.setEmail(studentData.getEmail());
		studentToUpdate.setFirstName(studentData.getFirstName());
		studentToUpdate.setLastName(studentData.getLastName());
		studentToUpdate.setDateOfBirth(studentData.getDateOfBirth());
		studentRepository.save(studentToUpdate);
		}

}
