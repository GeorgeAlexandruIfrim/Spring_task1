package com.georgeifrim.Spring_task1.controller;

import com.georgeifrim.Spring_task1.dao.TrainerDao;
import com.georgeifrim.Spring_task1.entities.Trainer;
import org.springframework.web.bind.annotation.*;

@RestController
public class TrainerController {

    private final TrainerDao trainerDao;

    public TrainerController(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @PutMapping("/trainer")
    public Trainer createTrainer(@RequestBody Trainer trainer){
        return trainerDao.createTrainer(trainer);
    }
    @GetMapping("/trainer/{id}")
    public Trainer getTrainer(@PathVariable int id){
        return trainerDao.getTrainerById(id);
    }

}
