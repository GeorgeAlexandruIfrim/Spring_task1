package com.georgeifrim.Spring_task1.dao;

import com.georgeifrim.Spring_task1.entities.Trainee;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class TraineeDao {

    private final TreeMap<Integer, Trainee> trainees = new TreeMap<>();
    @Value("${trainee.csvFile}")
    private String csvFile;
    private final Logger logger = LogManager.getLogger(TraineeDao.class);
    private final UserDao userDao;

    public TraineeDao(UserDao userDao) {
        this.userDao = userDao;
    }
    @Transactional
    public Trainee createTrainee(Trainee trainee) {

        if(!userDao.getUsers().containsKey(trainee.getUserId())){
            throw new RuntimeException("User with id " + trainee.getUserId() + " does not exist");
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
        trainees.put(Trainee.traineeId, trainee);
        logger.info("Trainee " + Trainee.traineeId + " created");
        return trainee;
    }
    @Transactional
    public Trainee updateTrainee(int id, Trainee trainee) {
        if(!trainees.containsKey(id) || !userDao.getUsers().containsKey(trainee.getUserId())){
            throw new RuntimeException("Trainee with id " + id + " does not exist or User with id " + trainee.getUserId() + " does not exist");
        }
        trainees.put(id, trainee);
        logger.info("Trainee with ID " + id + " was updated");
        return trainee;
    }

    @Transactional
    public Trainee deleteTrainee(int id){
        if(!trainees.containsKey(id)){
            throw new RuntimeException("Trainee with id " + id + " does not exist");
        }
        Trainee deletedTrainee = trainees.get(id);
        trainees.remove(id);
        logger.info("Trainee with ID " + id + " was deleted");
        return deletedTrainee;
    }

    public Trainee getTraineeById(int id){
        if(trainees.get(id) == null){
            throw new RuntimeException("Trainee with id " + id + " does not exist");
        }
        return trainees.get(id);
    }
    public TreeMap<Integer,Trainee> getTrainees() {
        return trainees;
    }

    @PostConstruct
    public void init() {

        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFile)).build()) {
            List<String[]> data = csvReader.readAll();

            TreeMap<Integer, Trainee> newTrainees = new TreeMap<>();
            for (String[] row : data) {
                if(row.length == 4){
                    int traineeId = Integer.parseInt(row[0]);
                    LocalDate birthDay = LocalDate.parse(row[1]);
                    String address = row[2];
                    int userId = Integer.parseInt(row[3]);
                    Trainee trainee = new Trainee(birthDay, address, userId);
                    newTrainees.put(traineeId, trainee);
                }
            }
            trainees.clear();
            trainees.putAll(newTrainees);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void commitChanges() {

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {

            for (Map.Entry<Integer, Trainee> entry : trainees.entrySet()) {
                Integer key = entry.getKey();
                Trainee value = entry.getValue();
                String[] data = {key.toString(),
                        value.getBirthDate().toString(),
                        value.getAddress(),
                        String.valueOf(value.getUserId()),
                        };
                writer.writeNext(data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}