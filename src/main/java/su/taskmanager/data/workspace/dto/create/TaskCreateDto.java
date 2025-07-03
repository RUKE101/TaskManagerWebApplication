package su.taskmanager.data.workspace.dto.create;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDto {
    private Long objectiveId;
    private String nameOfTask;
    private String description;
}
