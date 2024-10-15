package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.User;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.model.enums.Status;
import rs.raf.demo.repositories.ErrorMessageRepository;
import rs.raf.demo.repositories.VacuumRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class VacuumService implements IService<VacuumCleaner, Long>{

    private VacuumRepository vacuumRepository;

    private static final int CYCLE_LIMIT = 3;

    private Map<Long, AtomicInteger> cycleCountMap = new ConcurrentHashMap<>();

    @Autowired
    public VacuumService(VacuumRepository vacuumRepository) {
        this.vacuumRepository = vacuumRepository;
    }

    public List<VacuumCleaner> getAllVacuumsByUser(User user) {
        return vacuumRepository.findByAddedBy(user);
    }

    public List<VacuumCleaner> searchVacuumCleaners(String name, List<Status> statuses, LocalDate dateFrom, LocalDate dateTo, User user) {
        if (name != null && !name.isEmpty()) {
            return vacuumRepository.findByNameContainingIgnoreCaseAndAddedByAndActiveTrue(name, user);
        } else if (statuses != null && !statuses.isEmpty()) {
            return vacuumRepository.findByStatusInAndAddedByAndActiveTrue(statuses, user);
        } else if (dateFrom != null && dateTo != null) {
            return vacuumRepository.findByCreatedAtBetweenAndAddedByAndActiveTrue(dateFrom, dateTo, user);
        } else {
            return vacuumRepository.findByAddedByAndActiveTrue(user);
        }
    }

    @Override
    public <S extends VacuumCleaner> S save(S var1) {
        return vacuumRepository.save(var1);
    }

    @Override
    public Optional<VacuumCleaner> findById(Long var1) {
        return vacuumRepository.findById(var1);
    }

    @Override
    public List<VacuumCleaner> findAll() {
        return vacuumRepository.findAll();
    }

    @Override
    public ResponseEntity<?> deleteById(Long var1) {
        vacuumRepository.deleteById(var1);
        return null;
    }


    private void incrementCycleCount(Long vacuumId) {
        cycleCountMap.computeIfAbsent(vacuumId, k -> new AtomicInteger(0)).incrementAndGet();
    }


    private void resetCycleCount(Long vacuumId) {
        AtomicInteger count = cycleCountMap.get(vacuumId);
        if (count != null) {
            count.set(0);
        }
    }

    public void removeVacuum(VacuumCleaner vacuumCleaner){
        vacuumCleaner.setActive(false);
        this.vacuumRepository.save(vacuumCleaner);
    }

    public void updateStatus(Long vacuumId, Status status, String operation){
        try {
            if(operation.equals("discharge")){

                Thread.sleep(1500);
                vacuumRepository.updateStatusById(vacuumId, Status.DISCHARGING);

                System.out.println("STATUS DISCHARGING");
                System.out.println("USISIVAC STATUS" + findById(vacuumId));

                Thread.sleep(1500);
                vacuumRepository.updateStatusById(vacuumId, Status.OFF);

                resetCycleCount(vacuumId);

            } else {
                Thread.sleep(1500);
                vacuumRepository.updateStatusById(vacuumId, status);

                incrementCycleCount(vacuumId);
                if (cycleCountMap.getOrDefault(vacuumId, new AtomicInteger(0)).get() % 6 == 0) {
                    updateStatus(vacuumId, status, "discharge");
                }
            }
            System.out.println("STATUS UPDATED " + status + " COUNT = " + cycleCountMap.get(vacuumId));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ObjectOptimisticLockingFailureException exception) {
            this.updateStatus(vacuumId, status, operation);
        }
    }

}
