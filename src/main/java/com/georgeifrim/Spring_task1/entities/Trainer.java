package com.georgeifrim.Spring_task1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
public class Trainer {

    private List<TrainingType> trainingType;
    private int userId;
}
