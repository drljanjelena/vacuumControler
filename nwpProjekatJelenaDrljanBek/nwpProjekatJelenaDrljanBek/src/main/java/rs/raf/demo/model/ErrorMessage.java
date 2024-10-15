package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
public class ErrorMessage {

    @Id
    @Column(name = "error_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @NotNull
    private Long vacuumId;

    @NotBlank(message = "Operation is required")
    private String operation;

    @NotBlank(message = "Error is required")
    private String error;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

}
