package com.georgeifrim.Spring_task1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Data
public class User {

    public static int userId = 0;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private boolean isActive;

    public User(String firstName, String lastName, boolean isActive) {
        userId++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = firstName + "." + lastName;
        this.password = passwordGenerator();
        this.isActive = isActive;
    }

    public String passwordGenerator(){
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int passwordLength = 10;
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(passwordLength);

        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }
}
