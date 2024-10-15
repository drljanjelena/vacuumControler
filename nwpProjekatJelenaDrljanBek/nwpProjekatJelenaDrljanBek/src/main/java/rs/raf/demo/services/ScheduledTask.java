package rs.raf.demo.services;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.ScheduledOperation;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTask {

    private ScheduledOperationService scheduledOperationService;
    private VacuumService vacuumService;
    private ErrorMessageService errorMessageService;

    public ScheduledTask(ScheduledOperationService scheduledOperationService, VacuumService vacuumService, ErrorMessageService errorMessageService) {
        this.scheduledOperationService = scheduledOperationService;
        this.vacuumService = vacuumService;
        this.errorMessageService = errorMessageService;
    }

    @Scheduled(fixedRate = 60000) //na minut
    public void executeScheduledOperations() {
        List<ScheduledOperation> scheduledOperations = scheduledOperationService.findAll();
     //   System.out.println("USAO U SCHEDUER OP");
        for (ScheduledOperation operation : scheduledOperations) {
       //     System.out.println("FOR : USAO U SCHEDUER OP");
            try {
                if(operation.getScheduledDateTime().isAfter(LocalDateTime.now()) ||
                        operation.getScheduledDateTime().isEqual(LocalDateTime.now())){
           //         System.out.println("IF : USAO U SCHEDUER OP");
                    vacuumService.updateStatus(operation.getVacuumId(), operation.getStatus(), operation.getOperation());
                    scheduledOperationService.deleteById(operation.getTaskId());
                }
            } catch (Exception e) {
                recordErrorMessage(operation, e.getMessage());
            }
        }
    }

    private void recordErrorMessage(ScheduledOperation operation, String errorMessage) {
        ErrorMessage error = new ErrorMessage();
        error.setDate(LocalDate.now());
        error.setVacuumId(operation.getVacuumId());
        error.setOperation(operation.getOperation());
        error.setError(errorMessage);

        errorMessageService.save(error);
    }
}
