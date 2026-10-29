package com.example.labres;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.labres.mapper")
public class LabReservationApplication {
    public static void main(String[] args) {
        SpringApplication.run(LabReservationApplication.class, args);
    }
}
