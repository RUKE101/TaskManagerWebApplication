package su.taskmanager.data.workspace.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import su.taskmanager.data.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="workspace")
public class Workspace {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name="workspace_id")
        private Long id;
        @ManyToOne
        @JsonBackReference
        @JoinColumn(name="user_id")
        private User author;
        @JsonManagedReference
        @Column
        private String nameOfWorkspace;
        @Column
        private String description;
        @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Project> projects = new ArrayList<>();


}
