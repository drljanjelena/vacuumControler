package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import rs.raf.demo.model.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Entity
//@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class VacuumCleaner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vacuumId;

    @NotBlank(message = "Name is required")
    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "addedBy", referencedColumnName = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User addedBy;

    private boolean active;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Version
    private Integer version = 0;
}
