package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.raf.demo.model.ScheduledOperation;
import rs.raf.demo.model.User;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.model.enums.Status;

import java.time.LocalDate;
import java.util.List;

public interface ScheduledOperationRepository extends JpaRepository<ScheduledOperation, Long> {

}
