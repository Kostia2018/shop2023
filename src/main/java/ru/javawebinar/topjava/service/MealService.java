package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {


    private final MealRepository repository;


    public MealService(MealRepository repository) {
        this.repository = repository;


    }

  public   Meal save(Meal meal, int userId) {

        return repository.save(meal, userId);

    }


  public   boolean delete(int id, int userId) {

        return repository.delete(id, userId);

    }


  public   Meal get(int id, int userId) {

        ValidationUtil.checkNotFoundWithId(repository.get(id, userId), id);

        return repository.get(id, userId);

    }


  public   List<Meal> getAll(int userId) {

        return repository.getAll(userId);


    }


  public   void update(Meal meal, int userId) {

        ValidationUtil.checkNotFoundWithId(repository.save(meal, userId), meal.getId());


    }

}