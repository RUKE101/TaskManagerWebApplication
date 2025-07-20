package su.taskmanager.data.workspace.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import su.taskmanager.data.user.entity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="workspace")
public class Workspace implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;
        @ManyToOne(fetch = FetchType.EAGER)
        @JsonBackReference
        @JoinColumn(name = "author_id", nullable = false)
        private User author;
        @ManyToMany(mappedBy = "workspaces")
        private final List<User> users = new ArrayList<>();

        @Column
        private String nameOfWorkspace;
        @Column
        private String description;
        @JsonManagedReference(value = "workspace-objectives")
        @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
        private final List<Objective> objectives = new ArrayList<>();

        @JsonManagedReference
        @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
        public final List<Invite> invites = new ArrayList<>();


        public User addUser(User user) {
                this.users.add(user);
                return user;
        }

        public void removeUser(User user) {
                this.users.remove(user);
        }

        public Invite addInvite(Invite invite) {
                this.invites.add(invite);
                return invite;
        }

        public void removeInvite(Invite invite) {
                this.invites.remove(invite);
        }
}
