package global.goit.edu.todolist.model.service;

import global.goit.edu.todolist.model.entity.message.AuthMessage;
import global.goit.edu.todolist.model.entity.user.User;
import global.goit.edu.todolist.controller.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    /**
     * Обновление данных пользователя
     *
     * @return сохраненный пользователь
     */
    public User update(User user) {
        return repository.save(user);
    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public User create(User user) {

        if (repository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException(AuthMessage.userAlreadyExists.name());
        }
        if (user.getUsername().length() < 3) {
            throw new IllegalArgumentException(AuthMessage.invalidUsername.name());
        }
        if (user.getPassword().length() < 5) {
            throw new IllegalArgumentException(AuthMessage.invalidPassword.name());
        }
        return repository.save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(String username) {

        Optional<User> result = repository.findById(username);

        if (!result.isPresent()) {
            throw new UsernameNotFoundException("User with name - " + username + " is not found");
        }
        return result.get();
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

    public boolean isExistsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    public List<User> getAll() {
        return repository.findAll();
    }

}
