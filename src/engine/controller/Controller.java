package engine.controller;

import engine.domain.entity.CompletedQuiz;
import engine.domain.entity.Quiz;
import engine.domain.entity.User;
import engine.domain.repo.UserRepository;
import engine.dto.Answer;
import engine.dto.QuizForUser;
import engine.dto.QuizResult;
import engine.service.QuizService;
import engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class Controller {
    private final UserRepository userRepository;
    private final QuizService quizService;
    private final UserService userService;


    @Autowired
    public Controller( UserRepository userRepository, QuizService quizService, UserService userService) {
        this.userRepository = userRepository;
        this.quizService = quizService;
        this.userService = userService;
    }

    @PostMapping(value = "/api/quizzes", consumes = "application/json")
    public QuizForUser newQuiz (@Valid @RequestBody Quiz newQuiz, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid data");
        }
        quizService.save(newQuiz, principal.getName());
        return new QuizForUser(newQuiz);
    }

    @GetMapping ("/api/quizzes/{id}")
    public QuizForUser getQuiz (@PathVariable int id) {
        return quizService.getQuiz(id);
    }

    @GetMapping ("/api/quizzes")
    public Page<Quiz> getQuizzes (@RequestParam(defaultValue = "0", name = "page") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(defaultValue = "id") String orderBy) {
        Page<Quiz> page = quizService.getQuizzes(pageNo, pageSize, orderBy);
        return page;
    }

    @GetMapping ("/api/quizzes/completed")
    public Page<CompletedQuiz> getCompleted (@RequestParam(defaultValue = "0", name = "page") Integer pageNo,
                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                             @RequestParam(defaultValue = "completedAt") String orderBy,
                                             Principal principal) {
        Page<CompletedQuiz> page = quizService.getCompleted(pageNo, pageSize, orderBy, principal.getName());
        return page;
    }

    @PostMapping(value = "/api/quizzes/{id}/solve", consumes = "application/json")
    public QuizResult solve (@PathVariable int id, @RequestBody Answer givenAnswer, Principal principal) {
        if (quizService.isAnswerCorrect(id, givenAnswer, principal.getName())) {
            return new QuizResult(true, "Correct!");
        }
        return new QuizResult(false, "Wrong! Try again");
    }

    @PostMapping(value = "/api/register", consumes = "application/json")
    public void register (@RequestBody @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password too short or invalid email format");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicated email");
        }
        userService.save(user);
    }

    @DeleteMapping("/api/quizzes/{id}")
    public ResponseEntity<Object> deleteQuiz (@PathVariable int id, Principal principal) {
        if (!quizService.deleteQuiz(id, principal.getName())) { //quiz not belong to user
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete it");
        }
        return ResponseEntity.status(204).body(null);
    }



}
