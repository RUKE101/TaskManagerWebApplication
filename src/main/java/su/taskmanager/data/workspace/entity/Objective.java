package su.taskmanager.data.workspace.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import su.taskmanager.data.user.entity.User;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="objective")
public class Objective implements Serializable {
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    private LocalDateTime expiryDate;

    @Column
    private Boolean is_done;

    @JsonBackReference(value = "workspace-objectives")
    @ManyToOne
    @JoinColumn(name="workspace_id")
    private Workspace workspace;



}
