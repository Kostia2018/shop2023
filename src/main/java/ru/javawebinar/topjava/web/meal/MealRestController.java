package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {

    private final Logger log = LoggerFactory.getLogger(getClass());


    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal, int userId) {
        log.info("Create{} with userId={}", meal, userId);

        ValidationUtil.checkNew(meal);

        return service.save(meal, userId);


    }

    public Meal get(int id, int userId) {
        log.info("Get{} with userId={}", id, userId);
        Meal meal = service.get(id, userId);

        ValidationUtil.checkNotFoundWithId(meal, userId);

        return meal;


    }

    public void delete(int id, int userId) {
        log.info("Delete{} with userId={}", id, userId);

        service.delete(id, userId);


    }


    public List<MealTo> getAll(int userId) {
        log.info("getAll with userId = {}", userId);
        return MealsUtil.getTos(service.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);


    }


    public void update(Meal meal, int userId) {
        log.info("Update{} with userId = {}", meal, userId);

        ValidationUtil.assureIdConsistent(meal, userId);

        service.update(meal, userId);


    }

    public List<MealTo> getBetweenHalfOpen(@Nullable LocalDate startDate, @Nullable LocalTime startTime, @Nullable LocalDate endDate, @Nullable LocalTime endTime) {

        int userId = SecurityUtil.authUserId();

        List<Meal> filterByDate = service.getBetweenHalfOpen(startDate, endDate, userId);

        return MealsUtil.getFilteredTos(filterByDate, 2000, startTime, endTime);


    }


}