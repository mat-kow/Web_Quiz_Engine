package engine.service;

import engine.app.ResourceNotFoundException;
import engine.domain.entity.CompletedQuiz;
import engine.domain.entity.Quiz;
import engine.domain.entity.User;
import engine.domain.repo.CompletedQuizRepository;
import engine.domain.repo.QuizRepository;
import engine.domain.repo.UserRepository;
import engine.dto.Answer;
import engine.dto.QuizForUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final CompletedQuizRepository completedQuizRepository;
    private final UserRepository userRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository, CompletedQuizRepository completedQuizRepository,
                       UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.completedQuizRepository = completedQuizRepository;
        this.userRepository = userRepository;
    }

    public Page<Quiz> getQuizzes(Integer pageNo, Integer pageSize, String orderBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(orderBy));
        Page<Quiz> page = quizRepository.findAll(paging);
        return page;
    }

    public Page <CompletedQuiz> getCompleted (Integer pageNo, Integer pageSize, String orderBy, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(ResourceNotFoundException::new);
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(orderBy).descending());
        Page<CompletedQuiz> page = completedQuizRepository.findAllByUserId(paging, user.getId());
        return page;
    }

    public void save(Quiz quiz, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(ResourceNotFoundException::new);
        quiz.setUser(user);
        quizRepository.save(quiz);
    }

    public QuizForUser getQuiz (int id) {
       return new QuizForUser(quizRepository.findById(id).orElseThrow(ResourceNotFoundException::new));
    }

    public boolean isAnswerCorrect (int quizId, Answer givenAnswer, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(ResourceNotFoundException::new);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(ResourceNotFoundException::new);
        List<Integer> rightAnswer = new ArrayList<>(quiz.getAnswer());
        List<Integer> givenAnswerList = givenAnswer.getAnswer();
        Collections.sort(givenAnswerList);
        if (rightAnswer.equals(givenAnswerList)) {
            CompletedQuiz completedQuiz = new CompletedQuiz(quiz.getId(), user.getId());
            completedQuizRepository.save(completedQuiz);
            return true;
        }
        return false;
    }

    public boolean deleteQuiz (int id, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(ResourceNotFoundException::new);
        Quiz quiz = quizRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        if (quiz.getUser().getId() == user.getId()) {
            quizRepository.delete(quiz);
            return true;
        }
        return false;
    }
}
