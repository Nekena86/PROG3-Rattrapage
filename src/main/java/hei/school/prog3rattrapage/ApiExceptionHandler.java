package hei.school.prog3rattrapage;
import hei.school.prog3rattrapage.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handle(Exception e) {
        return ResponseEntity
                .status(500)
                .body(new ErrorDto("INTERNAL_ERROR", e.getMessage()));
    }
}