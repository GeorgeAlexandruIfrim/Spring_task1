package com.georgeifrim.Spring_task1.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class TrainingType {

    private static int trainingTypeId = 0;
    private String trainingName;

    public TrainingType(String trainingName) {
        trainingTypeId++;
        this.trainingName = trainingName;
    }
}
