package su.taskmanager.data.workspace.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.user.repository.UserRepository;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.repository.WorkspaceRepository;

import java.util.List;
import java.util.Optional;


@Service
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository,
                            UserRepository userRepository) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
    }

    public List<Workspace> findAuthorById(Long id) {
        return workspaceRepository.findByAuthorId(id);
    }

    public Workspace createWorkspace(Workspace workspace) {

        Long authorId = workspace.getAuthor().getId();
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Not found user with id: "+ authorId));
        workspace.setAuthor(author);
        return workspaceRepository.save(workspace);

    }

    public Optional<Workspace> findWorkspaceById(Long id) {
        return workspaceRepository.findById(id);
    }

}


