package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        List<UserMealWithExcess> mealsTo2 = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo2.forEach(System.out::println);

        List<UserMealWithExcess> mealsTo3 = filteredByStreams2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo3.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesPerDayMap.merge(meal.getDateTime().toLocalDate(),  meal.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        for (UserMeal meal : meals) {
            LocalDateTime dateTime = meal.getDateTime();
            if (isBetweenHalfOpen(dateTime.toLocalTime(), startTime, endTime)) {
                mealsWithExcess.add(convertToMealWithExcess(meal,
                    caloriesPerDayMap.get(dateTime.toLocalDate()) > caloriesPerDay));
            }
        }

        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = meals.stream()
            .collect(groupingBy(meal -> meal.getDateTime().toLocalDate(), summingInt(UserMeal::getCalories)));

        return meals.stream()
            .filter(meal -> isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
            .map(meal -> convertToMealWithExcess(meal, caloriesPerDayMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
            .collect(toList());
    }

    public static List<UserMealWithExcess> filteredByStreams2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        class MealsWithExcessCollector implements Collector<UserMeal, Map<LocalDate, List<UserMeal>>, List<UserMealWithExcess>> {
            private Map<LocalDate, Integer> caloriesMap;

            private MealsWithExcessCollector() {
                this.caloriesMap = new ConcurrentHashMap<>();
            }

            @Override
            public Supplier<Map<LocalDate, List<UserMeal>>> supplier() {
                return HashMap::new;
            }

            @Override
            public BiConsumer<Map<LocalDate, List<UserMeal>>, UserMeal> accumulator() {
                return (map, meal) -> {
                    LocalDate date = meal.getDateTime().toLocalDate();
                    map.computeIfAbsent(date, key -> new ArrayList<>());
                    if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                        map.get(date).add(meal);
                    }
                    caloriesMap.merge(date, meal.getCalories(), Integer::sum);
                };
            }

            @Override
            public BinaryOperator<Map<LocalDate, List<UserMeal>>> combiner() {
                return (map1, map2) -> Stream.concat(map1.entrySet().stream(), map2.entrySet().stream())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (v1, v2) -> Stream.concat(v1.stream(), v2.stream())
                            .collect(toList())));
            }

            @Override
            public Function<Map<LocalDate, List<UserMeal>>, List<UserMealWithExcess>> finisher() {
                return map -> map.values()
                    .stream()
                    .flatMap(Collection::stream)
                    .map(meal -> convertToMealWithExcess(meal, caloriesMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                    .collect(toList());
            }

            @Override
            public Set<Characteristics> characteristics() {
                return EnumSet.of(Characteristics.CONCURRENT);
            }
        }

        return meals.stream()
            .collect(new MealsWithExcessCollector());
    }

    private static UserMealWithExcess convertToMealWithExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
