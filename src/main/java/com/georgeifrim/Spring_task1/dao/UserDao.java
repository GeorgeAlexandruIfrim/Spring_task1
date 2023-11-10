package com.georgeifrim.Spring_task1.dao;

import com.georgeifrim.Spring_task1.entities.User;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

@Repository
@Getter
public class UserDao {

    private final TreeMap<Integer,User> users = new TreeMap<>();
    @Value("${user.csvFile}")
    private String csvFile;
    private final Logger logger = LogManager.getLogger(UserDao.class);
    @Transactional
    public User createUser(User user) {

        List<String[]> existingData;
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFile)).build()) {
            existingData = csvReader.readAll();
            if(existingData.size() != 0 && !existingData.get(existingData.size() - 1)[0].equals("")){
                User.userId = Integer.parseInt(existingData.get(existingData.size() - 1)[0])+1;
            }
            if(userExists(user)){
                user.setUserName(user.getUserName() + " " + new Random().nextInt(10));
            }

        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
        users.put(User.userId, user);

        logger.info("User " + user.getUserName() + " created");

        return user;
    }

    public User updateUser(int id, User user) {
        if(!users.containsKey(id)){
            throw new RuntimeException("User with id " + id + " does not exist");
        }
        users.put(id, user);
        logger.info("User with ID " + id + " was updated");
        return user;
    }
    public User deleteUser(int id){
        if(!users.containsKey(id)){
            throw new RuntimeException("User with id " + id + " does not exist");
        }
        User deletedUser = users.get(id);
        users.remove(id);
        logger.info("User with ID " + id + " was deleted");
        return deletedUser;
    }
    public TreeMap<Integer,User> getUsers() {
        return users;
    }

    public User getUserById(int id){
        if(users.get(id) == null){
            throw new RuntimeException("User with id " + id + " does not exist");
        }
        return users.get(id);
    }

    @PreDestroy
    public void commitChanges() throws IOException, CsvException {

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {

            // Append new data to the file
            for (Map.Entry<Integer, User> entry : users.entrySet()) {
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
            List<String[]> data = csvReader.readAll();

            TreeMap<Integer, User> newUsers = new TreeMap<>();
            for (String[] row : data) {
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
            // Update the users field with the new data
            users.clear();
            users.putAll(newUsers);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean userExists(User inputUser){
        for (User existingUser : users.values()) {
            if(existingUser.getUserName().equals(inputUser.getUserName())){
                return true;
            }
        }
        return false;
    }
}
