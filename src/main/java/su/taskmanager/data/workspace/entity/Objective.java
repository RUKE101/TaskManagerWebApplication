package su.taskmanager.data.workspace.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import su.taskmanager.data.user.entity.User;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Objective {
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @JsonBackReference(value = "user-objectives")
    @ManyToOne
    @JoinColumn(name="user_id")
    private User taskHangsOn;

    @Column
    private LocalDateTime expiryDate;

    @Column
    private Boolean is_done;

    @JsonBackReference(value = "workspace-objectives")
    @ManyToOne
    @JoinColumn(name="workspace_id")
    private Workspace workspace;



}
