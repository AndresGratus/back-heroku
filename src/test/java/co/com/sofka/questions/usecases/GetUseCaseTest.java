package co.com.sofka.questions.usecases;

import co.com.sofka.questions.collections.Answer;
import co.com.sofka.questions.collections.Question;
import co.com.sofka.questions.repositories.AnswerRepository;
import co.com.sofka.questions.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class GetUseCaseTest {

    QuestionRepository questionRepository;
    AnswerRepository answerRepository;
    GetUseCase getUseCase;

    @BeforeEach
    public void setup() {
        MapperUtils mapperUtils = new MapperUtils();
        questionRepository = mock(QuestionRepository.class);
        answerRepository = mock(AnswerRepository.class);
        getUseCase = new GetUseCase(mapperUtils, questionRepository, answerRepository);
    }

    @Test
    void getValidationTest() {
        var question = new Question();
        question.setId("id");
        question.setUserId("UserId");
        question.setType("Type");
        question.setCategory("Category");
        question.setQuestion("Question");

        var answer = new Answer();
        answer.setId("id");
        answer.setUserId("userId");
        answer.setQuestionId("questionId");
        answer.setPosition(3);
        answer.setAnswer("answer");

        when(questionRepository.findById(question.getId())).thenReturn(Mono.just(question));
        when(answerRepository.findAllByQuestionId(question.getId())).thenReturn(Flux.just(answer));

        StepVerifier.create(getUseCase.apply(question.getId()))
                .expectNextMatches(q -> {
                    assert q.getId().equals(question.getId());
                    assert q.getUserId().equals(question.getUserId());
                    assert q.getCategory().equals(question.getCategory());
                    assert q.getQuestion().equals(question.getQuestion());
                    assert q.getType().equals(question.getType());
                    return true;
                }).verifyComplete();
        verify(questionRepository).findById(question.getId());
        verify(answerRepository).findAllByQuestionId(question.getId());
    }
}