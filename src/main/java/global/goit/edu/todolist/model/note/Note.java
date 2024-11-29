package global.goit.edu.todolist.model.note;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", length = 256)
    private String title;

    @Column(name = "content", columnDefinition = "CLOB")
    private String content;
}