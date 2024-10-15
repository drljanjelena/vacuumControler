package rs.raf.demo.model;

import lombok.Data;
import rs.raf.demo.model.enums.Status;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class ScheduledOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private String operation;

    private Status status;

    private LocalDateTime scheduledDateTime;

    private Long userId;

    private Long vacuumId;


}
