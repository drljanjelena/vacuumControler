package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.raf.demo.model.User;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.model.enums.Status;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface VacuumRepository extends JpaRepository<VacuumCleaner, Long> {
    List<VacuumCleaner> findByAddedBy(User user);
    List<VacuumCleaner> findByAddedByAndActiveTrue(User user);

    List<VacuumCleaner> findByNameContainingIgnoreCaseAndAddedByAndActiveTrue(String name, User user);

    List<VacuumCleaner> findByStatusInAndAddedByAndActiveTrue(List<Status> statuses, User user);

    List<VacuumCleaner> findByCreatedAtBetweenAndAddedByAndActiveTrue(LocalDate dateFrom, LocalDate dateTo, User user);

    @Modifying
    @Query("UPDATE VacuumCleaner v SET v.status = :status WHERE v.vacuumId = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") Status status);
}
