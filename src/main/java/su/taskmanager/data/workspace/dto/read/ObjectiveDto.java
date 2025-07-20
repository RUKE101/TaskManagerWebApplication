package su.taskmanager.data.workspace.dto.read;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
public class ObjectiveDto {
    private Long id;
    private String name;
    private String description;
    private String hangsOn;
    private Boolean is_done;
    private LocalDateTime expiryDate;
    private Long workspaceId;

    public String getExpiryDate() {
        return expiryDate != null
                ? expiryDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                : null;
    }
}