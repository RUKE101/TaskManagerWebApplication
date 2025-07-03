package su.taskmanager.data.workspace.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="task_id")
    private Long id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="objective_id")
    private Objective objective;
    @Column
    private String nameOfTask;
    @Column
    private String description;
}
