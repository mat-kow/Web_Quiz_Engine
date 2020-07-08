package engine.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size (min = 5)
    private String password;
    @Email @Pattern(regexp = ".+@.+\\..+")
    private String email;
    @OneToMany
    private List<CompletedQuiz> completedQuizzes;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CompletedQuiz> getCompletedQuizzes() {
        if (this.completedQuizzes == null) {
            this.completedQuizzes = new ArrayList<>(Collections.emptyList());
        }
            return completedQuizzes;
    }

    public void setCompletedQuizzes(List<CompletedQuiz> completedQuizzes) {
        this.completedQuizzes = completedQuizzes;
    }
}
