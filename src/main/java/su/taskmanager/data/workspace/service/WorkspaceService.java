package su.taskmanager.data.workspace.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.user.repository.UserRepository;
import su.taskmanager.data.workspace.dto.read.WorkspaceDto;
import su.taskmanager.data.workspace.entity.Invite;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.repository.WorkspaceRepository;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    @Transactional
    public Workspace createWorkspace(Workspace workspace) {

        Long authorId = workspace.getAuthor().getId();
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Not found user with id: " + authorId));
        workspace.addUser(author);
        workspace.setAuthor(author);
        author.addWorkspace(workspace);
        workspaceRepository.save(workspace);
        userRepository.save(author);
        return workspace;

    }

    @Transactional
    public User addUserToWorkspace(Workspace workspace, User user) {
        workspace.addUser(user);
        workspaceRepository.save(workspace);
        return user;
    }

    @Transactional
    public void deleteWorkspace(Workspace workspace) {
        for (User users : workspace.getUsers()) {
            users.getWorkspaces().remove(workspace);
        }
        workspace.getInvites().clear();
        workspace.getUsers().clear();
        workspaceRepository.delete(workspace);
    }

    @Transactional
    public Workspace updateWorkspaceAuthor(Workspace workspace, User user) {
        workspace.setAuthor(user);
        return workspaceRepository.save(workspace);
    }

    @Transactional
    public Workspace updateWorkspace(Workspace workspace, WorkspaceDto dto) {
        workspace.setNameOfWorkspace(dto.getName());
        workspace.setDescription(dto.getDescription());
        return workspaceRepository.save(workspace);
    }

    @Transactional
    Workspace addInvite(Workspace workspace, Invite invite) {
        workspace.addInvite(invite);
        return workspaceRepository.save(workspace);
    }

    public Workspace getWorkspace(Long workspaceId) {
        return findById(workspaceId)
                .orElseThrow(() -> new EntityNotFoundException("Workspace not found with id: " + workspaceId));
    }

    public List<Workspace> findAuthorById(Long id) {
        return workspaceRepository.findByAuthorId(id);
    }
    public Optional<Workspace> findById(Long id) {
        return workspaceRepository.findById(id);
    }

    public boolean isUserInWorkspace(Workspace workspace, User user) {
        return workspace.getUsers().stream()
                .anyMatch(users -> users.getUsername().equals(user.getUsername()));
    }

    public boolean isAuthor(Workspace workspace, User user) {
        return workspace.getAuthor().getUsername().equals(user.getUsername());
    }
}



