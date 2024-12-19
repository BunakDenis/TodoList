package global.goit.edu.todolist.model.entity.note;

import com.fasterxml.jackson.annotation.JsonIgnore;
import global.goit.edu.todolist.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    @JsonIgnore
    private User user;

    @Column(name = "title", length = 256)
    private String title;

    @Column(name = "content", columnDefinition = "CLOB")
    private String content;
}