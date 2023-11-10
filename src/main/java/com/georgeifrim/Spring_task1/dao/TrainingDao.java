package com.georgeifrim.Spring_task1.dao;

import com.georgeifrim.Spring_task1.entities.Training;
import com.georgeifrim.Spring_task1.entities.TrainingType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.TreeMap;

@Repository
public class TrainingDao {

    private final TreeMap<Integer, Training> trainings = new TreeMap<>();

    @Value("${training.csvFile}")
    private String csvFile;

    private final Logger logger = LogManager.getLogger(TrainingDao.class);

    private final UserDao userDao;

    private final TrainerDao trainerDao;

    private final TrainingType trainingType;

    public TrainingDao(UserDao userDao, TrainerDao trainerDao, TrainingType trainingType) {
        this.userDao = userDao;
        this.trainerDao = trainerDao;
        this.trainingType = trainingType;
    }

}
