package engine.dto;

import engine.domain.entity.Quiz;

import java.util.List;

public class QuizForUser {
    private Integer id;
    private String title;
    private String text;
    private List<String> options;

    public QuizForUser(Quiz quiz) {
        this.id = quiz.getId();
        this.options = quiz.getOptions();
        this.text = quiz.getText();
        this.title = quiz.getTitle();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
