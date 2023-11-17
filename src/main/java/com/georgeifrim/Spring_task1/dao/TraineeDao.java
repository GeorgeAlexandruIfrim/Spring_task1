package com.georgeifrim.Spring_task1.dao;

import com.georgeifrim.Spring_task1.entities.Trainee;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TraineeDao extends Dao<Integer, Trainee> {

    @Value("${trainee.csvFile}")
    private String csvFile;
    private final UserDao userDao;

    public TraineeDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Trainee create(Trainee trainee) {

        if(!userDao.getEntities().containsKey(trainee.getUserId())){
            throw new RuntimeException("User with id " + trainee.getUserId() + " does not exist");
        }
        entities.put(id, trainee);
        logger.info("Trainee " + id + " created");
        return trainee;
    }
    @Override
    public Trainee update(Integer id, Trainee trainee) {
        if(!entities.containsKey(id) || !userDao.getEntities().containsKey(trainee.getUserId())){
            throw new RuntimeException("Trainee with id " + id + " does not exist or User with id " + trainee.getUserId() + " does not exist");
        }
        entities.put(id, trainee);
        logger.info("Trainee with ID " + id + " was updated");
        return trainee;
    }

    @Override
    public Trainee delete(Integer id){
        if(!entities.containsKey(id)){
            throw new RuntimeException("Trainee with id " + id + " does not exist");
        }
        Trainee deletedTrainee = entities.get(id);
        entities.remove(id);
        logger.info("Trainee with ID " + id + " was deleted");
        return deletedTrainee;
    }

    public Trainee getTraineeById(int id){
        if(entities.get(id) == null){
            throw new RuntimeException("Trainee with id " + id + " does not exist");
        }
        return entities.get(id);
    }
    @Override
    public HashMap<Integer,Trainee> getAll() {
        return entities;
    }

    @PostConstruct
    public void init() {

        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFile)).build()) {
            List<String[]> data = csvReader.readAll();

                if(data.size() != 0 && !data.get(data.size() - 1)[0].equals(""))
                    id = Integer.parseInt(data.get(data.size() - 1)[0])+1;

            HashMap<Integer, Trainee> newTrainees = new HashMap<>();
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
            entities.clear();
            entities.putAll(newTrainees);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }
    @PreDestroy
    public void commitChanges() {

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {

            for (Map.Entry<Integer, Trainee> entry : entities.entrySet()) {
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