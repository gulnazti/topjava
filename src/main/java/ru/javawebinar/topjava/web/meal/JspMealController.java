package ru.javawebinar.topjava.web.meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;

/**
 * @author gulnaz
 */
@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping({"/create", "/update/{id}"})
    public String create(Model model, @PathVariable(required = false) Integer id) {
        Meal meal = id == null
                    ? new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000)
                    : super.get(id);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Meal meal) {
        if (meal.isNew()) {
            super.create(meal);
        } else {
            super.update(meal, meal.getId());
        }

        return "redirect:/meals";
    }

    @GetMapping("/delete/{id}")
    public String remove(@PathVariable("id") int id) {
        super.delete(id);
        return "redirect:/meals";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
        @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
        @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime endTime, Model model) {
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}
