package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    //private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();


    private static Map<Integer, Map<Integer, Meal>> repositoryWhitUserId = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    {

        MealsUtil.meals.forEach(meal -> save(meal, SecurityUtil.authUserId()));


    }


    @Override
    public Meal save(Meal meal, int userId) {

        Map<Integer, Meal> mealRep = repositoryWhitUserId.computeIfAbsent(userId, a -> new ConcurrentHashMap<>());

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealRep.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return mealRep.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> mealRep = repositoryWhitUserId.get(userId);

        return mealRep != null && mealRep.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> mealRep = repositoryWhitUserId.get(userId);


        return mealRep == null ? null : mealRep.get(id);
    }

//    @Override
//    public List<Meal> getAll(int userId) {
//        Map<Integer, Meal> mealRep = repositoryWhitUserId.get(userId);
//
//        if (mealRep == null) {
//
//            return Collections.emptyList();
//
//        }
//
//        return mealRep.values().stream().sorted(Comparator.comparing(Meal::getDateTime).reversed()).toList();
//    }
//
//    @Override
//    public List<Meal> getBetweenHalfOpen(LocalDateTime start, LocalDateTime end, int userId) {
//        Map<Integer, Meal> mealMap = repositoryWhitUserId.get(userId);
//
//
//        return mealMap == null ? Collections.emptyList() : mealMap.values().stream()
//                .filter(meal -> Util.isBetweenHalfOpen(meal.getDateTime(), start, end))
//                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
//                .collect(Collectors.toList());
//
//
//    }


    public List<Meal> getAll(int userId) {
        return getFilter(userId, meal -> true);
    }


    public List<Meal> getBetweenHalfOpen(LocalDateTime start, LocalDateTime end, int userId) {

        return getFilter(userId, meal -> Util.isBetweenHalfOpen(meal.getDateTime(), start, end));

    }


    private List<Meal> getFilter(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> meals = repositoryWhitUserId.get(userId);

        return meals == null ? Collections.emptyList() : meals.values()
                .stream().filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());


    }

}

