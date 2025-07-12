package su.taskmanager.data.user.entity;


import jakarta.persistence.*;
import lombok.*;
import su.taskmanager.data.workspace.entity.Workspace;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name="\"user\"")

public class User {
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_workspace",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="workspace_id")
    )
    private final List<Workspace> workspaces = new ArrayList<>();


    public Workspace addWorkspace(Workspace workspace) {
        this.workspaces.add(workspace);
        workspace.getUsers().add(this);
        return workspace;
    }


}

