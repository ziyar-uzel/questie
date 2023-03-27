package nl.tudelft.oopp.demo.datatransferobjects;

import java.util.Objects;

import nl.tudelft.oopp.demo.entities.Question;

public class QuestionRequest {

    private Question question;

    public QuestionRequest(Question question) {
        this.question = question;
    }

    public QuestionRequest() {
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("QuestionRequest{")
                .append("question=").append(question).append('}').toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionRequest)) {
            return false;
        }
        QuestionRequest that = (QuestionRequest) o;
        return question.equals(that.question);
    }

}