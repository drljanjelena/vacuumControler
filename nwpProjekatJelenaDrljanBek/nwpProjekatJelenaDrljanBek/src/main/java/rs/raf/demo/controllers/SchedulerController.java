package rs.raf.demo.controllers;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.ScheduledOperation;
import rs.raf.demo.model.User;
import rs.raf.demo.requests.ScheduledOperationRequest;
import rs.raf.demo.services.ScheduledOperationService;
import rs.raf.demo.services.UserService;


import javax.validation.Valid;

@CrossOrigin

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {
    private ScheduledOperationService scheduledOperationService;
    private UserService userService;

    public SchedulerController(ScheduledOperationService scheduledOperationService, UserService userService) {
        this.scheduledOperationService = scheduledOperationService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ScheduledOperation addSchedule(@RequestBody @Valid ScheduledOperationRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByEmail(email);

       ScheduledOperation scheduledOperation = new ScheduledOperation();
        scheduledOperation.setOperation(request.getOperation());
        scheduledOperation.setVacuumId(request.getVacuumId());
        scheduledOperation.setScheduledDateTime(request.getScheduledDateTime());
        scheduledOperation.setStatus(request.getStatus());
        scheduledOperation.setUserId(currentUser.getUserId());

        return scheduledOperationService.save(scheduledOperation);

    }
}

