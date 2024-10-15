package rs.raf.demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.enums.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;


    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllStudents(){
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());

        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            String role = authority.getAuthority();
            if(role.equalsIgnoreCase(String.valueOf(Permission.CAN_READ_USERS)))
                return userService.findAll();

        }
        throw new AccessDeniedException("Reading denied. User lacks required authority.");
    };

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStudentById(@RequestParam("userId") Long id){
        Optional<User> optionalStudent = userService.findById(id);
        if(optionalStudent.isPresent()) {
            return ResponseEntity.ok(optionalStudent.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User createStudent(@Valid @RequestBody User student){
        System.out.println("adding");
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            String role = authority.getAuthority();
            if(role.equalsIgnoreCase(String.valueOf(Permission.CAN_CREATE_USERS)))
                return userService.save(student);
        }
        throw new AccessDeniedException("Creating denied. User lacks required authority.");
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateStudent(@Valid @RequestBody User user){
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            String role = authority.getAuthority();
            if(role.equalsIgnoreCase(String.valueOf(Permission.CAN_UPDATE_USERS)))
                return userService.save(user);

        }
        throw new AccessDeniedException("Updating denied. User lacks required authority.");
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        Optional<User> optionalStudent = userService.findById(id);
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            String role = authority.getAuthority();
            if(role.equalsIgnoreCase(String.valueOf(Permission.CAN_DELETE_USERS))){
                if(optionalStudent.isPresent()) {
                    userService.deleteById(id);
                    return ResponseEntity.ok().build();
                }
                return ResponseEntity.notFound().build();
            }
        }
        throw new AccessDeniedException("Deleting denied. User lacks required authority.");
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> searchStudent(@RequestParam String firstName, @RequestParam String lastName){
//        return studentService.findByFirstNameAndLastName(firstName, lastName);
        return null;
    }

}
