package engine.quiz;


import engine.forms.Answer;
import engine.forms.Reply;
import engine.user.UserDetailsImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/quizzes")
public class QuizController {

    private final static Reply SUCCESS = new Reply(true, "Congratulations, you're right!");
    private final static Reply UNSUCCESS = new Reply(false, "Wrong answer! Please, try again.");

    private QuizService service;


    QuizController(QuizService service) {
        this.service = service;
    }


    @GetMapping("{id}")
    ResponseEntity<?> get(@PathVariable int id) {
        return service.get(id).map(value -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(value, headers, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(new HttpHeaders(), HttpStatus.NOT_FOUND));
    }

    @GetMapping()
    ResponseEntity<?> getAll(@RequestParam("page") int pageNo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(service.getAll(pageNo), headers, HttpStatus.OK);
    }

    @GetMapping("completed")
    ResponseEntity<?> getCompleted(@RequestParam("page") int pageNo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
       return new ResponseEntity<>(service.getAllCompletedForUser(pageNo, getCurrentUser().getId()), headers, HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<?> add(@Valid @RequestBody Quiz quiz) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        quiz.setAuthor(getCurrentUser().getUsername());
        return new ResponseEntity<>(service.add(quiz), headers, HttpStatus.OK);
    }

    @PostMapping("{id}/solve")
    ResponseEntity<?> reply(@PathVariable int id, @RequestBody Answer answer) {
        return service.get(id).map(value -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (answer.getAnswer() == null) {
                service.update(id, getCurrentUser().getId());
                return new ResponseEntity<>(SUCCESS, headers, HttpStatus.OK);
            }

            if (Arrays.equals(Arrays.stream(answer.getAnswer()).sorted().toArray(), value.getAnswer())) {
                service.update(id, getCurrentUser().getId());
                return new ResponseEntity<>(SUCCESS, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(UNSUCCESS, headers, HttpStatus.OK);
            }
        }).orElseGet(() -> new ResponseEntity<>(new HttpHeaders(), HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> delete(@PathVariable int id) {
        return service.get(id).map(value -> {
            if (value.getAuthor().equals(getCurrentUser().getUsername())) {
                service.delete(id);
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }).orElseGet(() -> new ResponseEntity<>(new HttpHeaders(), HttpStatus.NOT_FOUND));
    }

    private UserDetailsImpl getCurrentUser() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
