package global.goit.edu.todolist.controller.repository;

import global.goit.edu.todolist.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(int id);
    boolean existsByUsername(String username);
    boolean existsById(int id);

}
