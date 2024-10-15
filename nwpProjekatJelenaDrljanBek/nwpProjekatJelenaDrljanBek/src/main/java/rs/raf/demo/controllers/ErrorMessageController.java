package rs.raf.demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.User;
import rs.raf.demo.services.ErrorMessageService;
import rs.raf.demo.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin

@RestController
@RequestMapping("/api/errors")
public class ErrorMessageController {
    private ErrorMessageService errorMessageService;

    private UserService userService;

    public ErrorMessageController(ErrorMessageService errorMessageService, UserService userService) {
        this.errorMessageService = errorMessageService;
        this.userService = userService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ErrorMessage> getAllByUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByEmail(email);
        return errorMessageService.getAllErrorsByUser(currentUser);
    };

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@RequestParam("userId") Long id){
        Optional<ErrorMessage> optionalStudent = errorMessageService.findById(id);
        if(optionalStudent.isPresent()) {
            return ResponseEntity.ok(optionalStudent.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage create(@Valid @RequestBody ErrorMessage error){
        System.out.println("adding errors");
        return errorMessageService.save(error);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage update(@Valid @RequestBody ErrorMessage error){
        return errorMessageService.save(error);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<ErrorMessage> optional = errorMessageService.findById(id);
        if(optional.isPresent()) {
            errorMessageService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}

