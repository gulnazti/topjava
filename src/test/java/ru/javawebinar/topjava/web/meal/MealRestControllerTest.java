package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.MealTestData.MEALTO_MATCHER;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.MealTestData.meal1;
import static ru.javawebinar.topjava.MealTestData.meal2;
import static ru.javawebinar.topjava.MealTestData.meal3;
import static ru.javawebinar.topjava.MealTestData.meal4;
import static ru.javawebinar.topjava.MealTestData.meal5;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.user;
import static ru.javawebinar.topjava.util.MealsUtil.createTo;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;

/**
 * @author gulnaz
 */
class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + "/";

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID))
            .andDo(print())
            .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = MealTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(newMeal)))
            .andDo(print())
            .andExpect(status().isCreated());

        Meal created = TestUtil.readFromJson(action, Meal.class);
        int id = created.id();
        newMeal.setId(id);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(id, USER_ID), newMeal);
    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(updated)))
            .andDo(print())
            .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MEALTO_MATCHER.contentJsonArray(getTos(meals, user.getCaloriesPerDay())));
    }

    @Test
    void getFiltered() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
            .param("startDate", "2020-01-30")
            .param("startTime", "10:00")
            .param("endDate", "2020-01-30")
            .param("endTime", "21:00"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MEALTO_MATCHER.contentJsonOfArray(
                createTo(meal3, false),
                createTo(meal2, false),
                createTo(meal1, false)
            ));
    }

    @Test
    void getFilteredByStartDateEndTime() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
            .param("startDate", "2020-01-30")
            .param("endTime", "11:00"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MEALTO_MATCHER.contentJsonOfArray(
                createTo(meal5, true),
                createTo(meal4, true),
                createTo(meal1, false)
            ));
    }

    @Test
    void getFilteredByNullValues() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
            .param("startDate", "")
            .param("startTime", "")
            .param("endDate", "")
            .param("endTime", ""))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MEALTO_MATCHER.contentJsonArray(getTos(meals, user.getCaloriesPerDay())));
    }
}