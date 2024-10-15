package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.ErrorMessageRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ErrorMessageService implements IService<ErrorMessage, Long> {

    private ErrorMessageRepository errorMessageRepository;

    @Autowired
    public ErrorMessageService(ErrorMessageRepository errorMessage) {
        this.errorMessageRepository = errorMessage;
    }

    public List<ErrorMessage> getAllErrorsByUser(User user) {
        return errorMessageRepository.findByUser(user);
    }


    @Override
    public <S extends ErrorMessage> S save(S var1) {
        return errorMessageRepository.save(var1);
    }

    @Override
    public Optional<ErrorMessage> findById(Long var1) {
        return errorMessageRepository.findById(var1);
    }

    @Override
    public List<ErrorMessage> findAll() {
        return errorMessageRepository.findAll();
    }

    @Override
    public ResponseEntity<?> deleteById(Long var1) {
        errorMessageRepository.deleteById(var1);
        return null;
    }

    public void newError(Long vacuumId, String operation, String error, User user){
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setVacuumId(vacuumId);
        errorMessage.setDate(LocalDate.now());
        errorMessage.setOperation(operation);
        errorMessage.setUser(user);
        errorMessage.setError(error);
        errorMessageRepository.save(errorMessage);
    }

}
