package su.taskmanager.data.workspace.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Objective {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="objective_id")
    private Long id;
    @Column(nullable = false)
    private String nameOfObjective;
    @Column
    private String description;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;
    @JsonManagedReference
    @OneToMany(mappedBy = "objective")
    private List<Task> tasks;
}
