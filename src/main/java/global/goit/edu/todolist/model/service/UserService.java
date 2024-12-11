package global.goit.edu.todolist.model.service;

import global.goit.edu.todolist.model.entity.user.User;
import global.goit.edu.todolist.controller.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            // Заменить на свои исключения
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found"));

    }

    /**
     * Получение пользователя по id пользователя
     *
     * @return пользователь
     */
    public User getByUserId(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found"));

    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        try {
            var username = SecurityContextHolder.getContext().getAuthentication().getName();
            return getByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAll() {
        return repository.findAll();
    }
}
