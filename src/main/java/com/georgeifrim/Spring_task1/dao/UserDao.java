package com.georgeifrim.Spring_task1.dao;

import com.georgeifrim.Spring_task1.entities.User;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Repository
@Getter
public class UserDao extends Dao<Integer, User> {

    @Value("${user.csvFile}")
    private String csvFile;

    @Override
    public User create(User user) {
            if(userExists(user))
                user.setUserName(user.getUserName() + " " + new Random().nextInt(10));

        entities.put(id, user);
        id++;
        logger.info("User " + user.getUserName() + " created");

        return user;
    }

    @Override
    public User update(Integer id, User user) {
        if(!entities.containsKey(id)){
            throw new RuntimeException("User with id " + id + " does not exist");
        }
        entities.put(id, user);
        logger.info("User with ID " + id + " was updated");
        return user;
    }
    @Override
    public User delete(Integer id){
        if(!entities.containsKey(id)){
            throw new RuntimeException("User with id " + id + " does not exist");
        }
        User deletedUser = entities.get(id);
        entities.remove(id);
        logger.info("User with ID " + id + " was deleted");
        return deletedUser;
    }
    @Override
    public HashMap<Integer,User> getAll() {
        return entities;
    }

    @PreDestroy
    public void commitChanges() throws IOException, CsvException {

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {

            for (Map.Entry<Integer, User> entry : entities.entrySet()) {
                Integer key = entry.getKey();
                User value = entry.getValue();
                String[] data = {key.toString(),
                        value.getFirstName(),
                        value.getLastName(),
                        value.getUserName(),
                        value.getPassword(),
                        value.isActive() ? "true" : "false"};
                writer.writeNext(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {

        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFile)).build()) {

            List<String[]> existingData = csvReader.readAll();

            if(existingData.size() != 0 && !existingData.get(existingData.size() - 1)[0].equals(""))
                id = Integer.parseInt(existingData.get(existingData.size() - 1)[0])+1;

            TreeMap<Integer, User> newUsers = new TreeMap<>();
            for (String[] row : existingData) {
                if(row.length == 6){
                    int userId = Integer.parseInt(row[0]);
                    String firstName = row[1];
                    String lastName = row[2];
                    boolean isActive = Boolean.parseBoolean(row[5]);
                    User user = new User(firstName, lastName, isActive);
                    user.setPassword(row[4]);
                    newUsers.put(userId, user);
                }
            }
            entities.clear();
            entities.putAll(newUsers);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean userExists(User inputUser){
        for (User existingUser : entities.values()) {
            if(existingUser.getUserName().equals(inputUser.getUserName())){
                return true;
            }
        }
        return false;
    }
}
