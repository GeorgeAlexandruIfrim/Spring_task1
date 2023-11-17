package com.georgeifrim.Spring_task1.dao;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Getter
public abstract class Dao<Integer, T> {

     public int id = 0;
     public final Logger logger = LogManager.getLogger(TrainerDao.class);
     public abstract T create(T t);
     public abstract T update(Integer id, T t);
     public abstract T delete(Integer id);
     public abstract HashMap<Integer, T> getAll();

     public HashMap<Integer, T> entities = new HashMap<>();

}
