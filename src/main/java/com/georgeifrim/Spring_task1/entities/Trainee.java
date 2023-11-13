package com.georgeifrim.Spring_task1.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
public class Trainee {

    public static int traineeId = 0;
    private LocalDate birthDate;
    private String address;
    private int userId;

    public Trainee(LocalDate birthDate, String address, int userId) {
        traineeId++;
        this.birthDate = birthDate;
        this.address = address;
        this.userId = userId;
    }


}
