package com.student.studentapi.student;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class StudentConfig {
    
    @Bean
    CommandLineRunner commandLineRunner(
        StudentRepository repository){
            return args -> {
            Student zac = new Student(1l, "zac", "bob", LocalDate.of(2000, 10, 11), "zacbob@test.com");
            Student mundo = new Student(2l, "dr", "mundo", LocalDate.of(2006, 4, 6), "drmundo@test.com");
			Student lee = new Student(3l, "lee", "sin", LocalDate.of(1990, 04, 16), "lee-sin@test.com");
            Student ksante = new Student(4l, "ks", "sante", LocalDate.of(1995, 05, 10), "kssante@test.com");
            repository.saveAll(List.of(zac, mundo, lee, ksante));
            };
        }
}
