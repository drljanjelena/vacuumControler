package rs.raf.demo.requests;

import lombok.Data;
import rs.raf.demo.model.enums.Status;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
public class ScheduledOperationRequest {

    private String operation;

    private Status status;

    private LocalDateTime scheduledDateTime;

    private Long vacuumId;

}
