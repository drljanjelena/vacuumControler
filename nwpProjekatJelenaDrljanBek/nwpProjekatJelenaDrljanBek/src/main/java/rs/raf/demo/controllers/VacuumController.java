package rs.raf.demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.User;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.model.enums.Permission;
import rs.raf.demo.model.enums.Status;
import rs.raf.demo.requests.VacuumRequest;
import rs.raf.demo.services.ErrorMessageService;
import rs.raf.demo.services.UserService;
import rs.raf.demo.services.VacuumService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@CrossOrigin

@RestController
@RequestMapping("/api/vacuums")
public class VacuumController {
    private VacuumService vacuumService;
    private UserService userService;
    private ErrorMessageService errorMessageService;


    public VacuumController(VacuumService vacuumService, UserService userService, ErrorMessageService errorMessageService) {
        this.vacuumService = vacuumService;
        this.userService = userService;
        this.errorMessageService = errorMessageService;
    }


    @GetMapping(value = "/{vacuumId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findVacuumById(@PathVariable("vacuumId") Long id){
        Optional<VacuumCleaner> vacuumCleaner = vacuumService.findById(id);
        if(vacuumCleaner.isPresent()) {
            System.out.println(vacuumCleaner);
            return ResponseEntity.ok(vacuumCleaner.get());
        } else {
            System.out.println(vacuumCleaner);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<VacuumCleaner> searchVacuumCleaners(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<Status> statuses,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo
    ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByEmail(email);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDateFrom = null;
        LocalDate localDateTo = null;
        if(dateFrom != null && !dateFrom.isEmpty() && dateTo != null && !dateTo.isEmpty()){
            localDateFrom = LocalDate.parse(dateFrom, formatter);
            localDateTo = LocalDate.parse(dateTo, formatter);
        }
        return vacuumService.searchVacuumCleaners(name, statuses, localDateFrom, localDateTo, currentUser);
    }



    @PostMapping("/start/{vacuumId}")
    public ResponseEntity<String> startVacuum(@PathVariable Long vacuumId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByEmail(email);

        if (checkPermission(Permission.CAN_START_VACUUM)) {
            Optional<VacuumCleaner> cleanerOptional = vacuumService.findById(vacuumId);
            if (cleanerOptional.isPresent()) {
                VacuumCleaner cleaner = cleanerOptional.get();
                if (cleaner.getStatus() == Status.OFF && cleaner.isActive()) {
                    CompletableFuture.runAsync(() -> vacuumService.updateStatus(vacuumId, Status.ON, "start"));
                    return ResponseEntity.ok("Vacuum start process initiated.");
                } else {
                    return ResponseEntity.ok("Vacuum is not OFF.");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        throw new AccessDeniedException("Starting vacuum denied. User lacks required authority.");
    }



    @PostMapping("/stop/{vacuumId}")
    public ResponseEntity<String> stopVacuum(@PathVariable Long vacuumId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByEmail(email);

        if (checkPermission(Permission.CAN_STOP_VACUUM)) {
            Optional<VacuumCleaner> cleanerOptional = vacuumService.findById(vacuumId);
            if(cleanerOptional.isPresent()) {
                VacuumCleaner cleaner = cleanerOptional.get();
                if (cleaner.getStatus() == Status.ON && cleaner.isActive()) {
                    CompletableFuture.runAsync(() -> vacuumService.updateStatus(vacuumId, Status.OFF, "stop"));
                    return ResponseEntity.ok("Vacuum stop process initiated.");
                } else {
                    return ResponseEntity.ok("Vacuum is not ON.");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        throw new AccessDeniedException("Stopping vacuum denied. User lacks required authority.");
    }

    @PostMapping("/discharge/{vacuumId}")
    public ResponseEntity<String> dischargeVacuum(@PathVariable Long vacuumId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByEmail(email);

        if (checkPermission(Permission.CAN_DISCHARGE_VACUUM)) {
            Optional<VacuumCleaner> cleanerOptional = vacuumService.findById(vacuumId);
            if(cleanerOptional.isPresent()) {
                VacuumCleaner cleaner = cleanerOptional.get();
                if (cleaner.getStatus() == Status.OFF) {
                    CompletableFuture.runAsync(() -> vacuumService.updateStatus(vacuumId, Status.DISCHARGING, "discharge"));
                    return ResponseEntity.ok("Vacuum discharge process initiated.");
                } else {
                    return ResponseEntity.ok("Vacuum is not OFF.");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        throw new AccessDeniedException("Discharging vacuum denied. User lacks required authority.");
    }

    @PostMapping("/add")
    public VacuumCleaner addVacuum(@RequestBody @Valid VacuumRequest vacuumRequest) {
        if (checkPermission(Permission.CAN_ADD_VACUUM)) {
            VacuumCleaner newVacuum = new VacuumCleaner();
            newVacuum.setName(vacuumRequest.getName());
            newVacuum.setActive(true);
            newVacuum.setStatus(Status.OFF);
            LocalDate date = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            newVacuum.setCreatedAt(date);

            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = userService.findByEmail(email);
            newVacuum.setAddedBy(currentUser);

            return vacuumService.save(newVacuum);
        } else {
            throw new AccessDeniedException("Adding vacuum denied. User lacks required authority.");

        }
    }

    @PostMapping("/remove/{vacuumId}")
    public ResponseEntity<String> removeVacuum(@PathVariable Long vacuumId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByEmail(email);

        if(checkPermission(Permission.CAN_REMOVE_VACUUM)){
            Optional<VacuumCleaner> cleanerOptional = vacuumService.findById(vacuumId);
            if(cleanerOptional.isPresent()) {
                VacuumCleaner cleaner = cleanerOptional.get();
                if (cleaner.getStatus() == Status.OFF){
                    vacuumService.removeVacuum(cleaner);
                    return ResponseEntity.ok("Vacuum remove process initiated.");
                }
            }
            return ResponseEntity.notFound().build();
        }
        throw new AccessDeniedException("Removing vacuum denied. User lacks required authority.");
    }

    public boolean checkPermission(Permission permission){
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            String role = authority.getAuthority();
            if(role.equalsIgnoreCase(String.valueOf(permission)))
                return true;
        }
        return false;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VacuumCleaner> getAllVacuums(){
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            String role = authority.getAuthority();
            if(role.equalsIgnoreCase(String.valueOf(Permission.CAN_SEARCH_VACUUM)))
                return vacuumService.findAll();

        }
        throw new AccessDeniedException("Reading denied. User lacks required authority.");
    };

}

