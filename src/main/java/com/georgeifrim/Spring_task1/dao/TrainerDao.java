package com.georgeifrim.Spring_task1.dao;

import com.georgeifrim.Spring_task1.entities.Trainer;
import com.georgeifrim.Spring_task1.entities.TrainingType;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TrainerDao {

    private final HashMap<Integer, Trainer> trainers = new HashMap<>();

    public final Logger logger = LogManager.getLogger(TrainerDao.class);
    private int trainerId = 0;

    @Value("${trainer.csvFile}")
    private String csvFile;

    private final UserDao userDao;

    public TrainerDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public Trainer createTrainer(Trainer trainer) {

        if (!userDao.getEntities().containsKey(trainer.getUserId())) {
            throw new RuntimeException("User with id " + trainer.getUserId() + " does not exist");
        }

        trainers.put(trainerId, trainer);
        logger.info("Trainer " + trainerId + " created");
        return trainer;
    }
    public Trainer updateTrainer(int id, Trainer trainer){
        if(!trainers.containsKey(id) || !userDao.getEntities().containsKey(trainer.getUserId())){
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

    @PostConstruct
    public void init() {

        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFile)).build()) {
            List<String[]> data = csvReader.readAll();

            if(data.size() != 0 && !data.get(data.size() - 1)[0].equals(""))
                trainerId = Integer.parseInt(data.get(data.size() - 1)[0])+1;

            HashMap<Integer, Trainer> newTrainers = new HashMap<>();
            for (String[] row : data) {
                if(row.length == 4){
                    int trainerId = Integer.parseInt(row[0]);
                    String address = row[2];
                    int userId = Integer.parseInt(row[3]);
                    Trainer trainer = new Trainer(List.of(new TrainingType("HIIT")), userId);
                    newTrainers.put(trainerId, trainer);
                }
            }
            trainers.clear();
            trainers.putAll(newTrainers);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }
    @PreDestroy
    public void commitChanges() {

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {

            for (Map.Entry<Integer, Trainer> entry : trainers.entrySet()) {
                Integer key = entry.getKey();
                Trainer value = entry.getValue();
                String[] data = {key.toString(),
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
