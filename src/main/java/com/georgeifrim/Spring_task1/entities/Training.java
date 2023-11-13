package com.georgeifrim.Spring_task1.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
public class Training {

    public static int trainingId = 0;
    private int traineeId;
    private int trainerId;
    private int trainingTypeId;
    private String trainingName;
    private LocalDate trainingDate;
    private int durationInMinutes;

}
