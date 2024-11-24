package global.goit.edu.todolist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/time")
@RequiredArgsConstructor
public class TimeController {
    @GetMapping("/current")
    public ModelAndView getCurrentTime() {
        ModelAndView result = new ModelAndView("time/current");
        result.addObject(
                "currentTime",
                LocalDateTime
                        .now()
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss")));

        return result;
    }
}
