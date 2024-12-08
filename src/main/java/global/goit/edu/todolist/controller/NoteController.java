package global.goit.edu.todolist.controller;

import global.goit.edu.todolist.model.entity.note.Note;
import global.goit.edu.todolist.model.entity.user.Role;
import global.goit.edu.todolist.model.service.NoteService;
import global.goit.edu.todolist.model.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    @GetMapping("/list")
    public ModelAndView getList(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView result = new ModelAndView("note/list");

        SecurityContext context = SecurityContextHolder.getContext();

        Collection<? extends GrantedAuthority> authorities = context.getAuthentication().getAuthorities();

        boolean isAdmin = authorities.stream().anyMatch(a -> a.toString().equals(Role.ADMIN.toString()));

        List<Note> notes = noteService.getAll();
        result.addObject("role", isAdmin);
        result.addObject("notes", notes);
        return result;
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteById(@PathVariable long id) {
        Note note = noteService.getById(id);
        noteService.delete(note);

        RedirectView redirectView = new RedirectView("/note/list");
        return new ModelAndView(redirectView);
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editNotePage(@PathVariable long id) {
        ModelAndView result = new ModelAndView("note/edit");
        Note note = noteService.getById(id);

        result.addObject("note", note);
        return result;
    }

    @PostMapping("/edit")
    public ModelAndView editNote(@ModelAttribute("note") Note note) {

        if (noteService.getById(note.getId()) != null) {
            noteService.save(note);
        }

        RedirectView redirectView = new RedirectView("/note/list");

        return new ModelAndView(redirectView);
    }

    @PostMapping("/add")
    public ModelAndView addNote(@ModelAttribute("note") Note note) {

        if (note != null) {
            noteService.save(note);
        }

        RedirectView redirectView = new RedirectView("/note/list");

        return new ModelAndView(redirectView);
    }

    @PostMapping ("/logout")
    public ModelAndView logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

        logoutHandler.logout(request, response, authentication);

        RedirectView redirectView = new RedirectView("/login");

        return new ModelAndView(redirectView);

    }
}