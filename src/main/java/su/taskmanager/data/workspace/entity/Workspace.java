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
public class Workspace  {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;
        @ManyToOne(fetch = FetchType.LAZY)
        @JsonBackReference
        @JoinColumn(name = "author_id", nullable = false)
        private User author;
        @ManyToMany(mappedBy = "workspaces")
        private List<User> users = new ArrayList<>();

        @Column
        private String nameOfWorkspace;
        @Column
        private String description;
        @JsonManagedReference(value = "workspace-objectives")
        @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Objective> objectives = new ArrayList<>();

        @JsonManagedReference
        @OneToMany
        @JoinColumn(name="workspace")
        public List<Invite> invites = new ArrayList<>();


        public User addUser(User user) {
                this.users.add(user);
                return user;
        }

        public Invite addInvite(Invite invite) {
                this.invites.add(invite);
                return invite;
        }
}
