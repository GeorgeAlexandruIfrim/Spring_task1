package com.georgeifrim.Spring_task1.dao;

import com.georgeifrim.Spring_task1.entities.Trainee;
import com.georgeifrim.Spring_task1.entities.Trainer;
import com.georgeifrim.Spring_task1.entities.TrainingType;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

@Repository
public class TrainerDao {

    private final TreeMap<Integer, Trainer> trainers = new TreeMap<>();

    @Value("${trainer.csvFile}")
    private String csvFile;

    private final Logger logger = LogManager.getLogger(TrainerDao.class);

    private final UserDao userDao;

    public TrainerDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public Trainer createTrainer(Trainer trainer) {

        if (!userDao.getUsers().containsKey(trainer.getUserId())) {
            throw new RuntimeException("User with id " + trainer.getUserId() + " does not exist");
        }
        List<String[]> existingData;
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFile)).build()) {
            existingData = csvReader.readAll();
            if(existingData.size() != 0 && !existingData.get(existingData.size() - 1)[0].equals("")){
                Trainee.traineeId = Integer.parseInt(existingData.get(existingData.size() - 1)[0])+1;
            }
        }catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
        trainers.put(Trainer.trainerId, trainer);
        logger.info("Trainer " + Trainer.trainerId + " created");
        return trainer;
    }
    public Trainer updateTrainer(int id, Trainer trainer){
        if(!trainers.containsKey(id) || !userDao.getUsers().containsKey(trainer.getUserId())){
            throw new RuntimeException("Trainer with id " + id + " does not exist or User with id " + trainer.getUserId() + " does not exist");
        }
        trainers.put(id, trainer);
        logger.info("Trainer " + id + " updated");
        return trainer;
    }

    public Trainer getTrainerById(int id) {
        if (!trainers.containsKey(id)) {
            throw new RuntimeException("Trainer with id " + id + " does not exist");
        }
        return trainers.get(id);
    }

}
