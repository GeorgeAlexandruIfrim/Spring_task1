package com.georgeifrim.Spring_task1.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class Trainer {

    public static int trainerId = 0;
    private List<TrainingType> trainingType;
    private int userId;
}
