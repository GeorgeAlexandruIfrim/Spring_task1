package com.georgeifrim.Spring_task1.controller;

import com.georgeifrim.Spring_task1.dao.TraineeDao;
import com.georgeifrim.Spring_task1.entities.Trainee;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class TraineeController {

    private final TraineeDao traineeDao;

    public TraineeController(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @GetMapping("/trainees")
    public HashMap<Integer,Trainee> getAllTrainees(){
        return traineeDao.getAll();
    }
    @GetMapping("/trainees/{id}")
    public Trainee getTrainee(@PathVariable int id){
        return traineeDao.getTraineeById(id);
    }

    @PostMapping("/trainees")
    public Trainee createTrainee(@RequestBody Trainee trainee){
        return traineeDao.create(trainee);
    }

    @PutMapping("/trainees/{id}")
    public Trainee updateTrainee(@PathVariable int id,
                                 @RequestBody Trainee trainee){
        return traineeDao.update(id, trainee);
    }

    @DeleteMapping("/trainees/{id}")
    public Trainee deleteTrainee(@PathVariable int id){
        return traineeDao.delete(id);
    }
}
