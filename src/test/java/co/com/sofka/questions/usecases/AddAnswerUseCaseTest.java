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

class AddAnswerUseCaseTest {

    QuestionRepository questionRepository;
    AnswerRepository answerRepository;
    AddAnswerUseCase addAnswerUseCase;
    GetUseCase getUseCase;
    MapperUtils mapperUtils;

    @BeforeEach
    public void setup() {
        mapperUtils = new MapperUtils();
        questionRepository = mock(QuestionRepository.class);
        answerRepository = mock(AnswerRepository.class);
        getUseCase = new GetUseCase(mapperUtils, questionRepository, answerRepository);
        addAnswerUseCase = new AddAnswerUseCase(mapperUtils, getUseCase, answerRepository);
    }

    @Test
    void addAnswerTest() {
        MapperUtils mapper = new MapperUtils();
        var answer = new Answer();
        answer.setId("id");
        answer.setUserId("userId");
        answer.setQuestionId("questionId");
        answer.setAnswer("answer");
        answer.setPosition(3);

        var answered = mapper.mapEntityToAnswer().apply(answer);

        var question = new Question();
        question.setId("id");
        question.setUserId("userId");
        question.setQuestion("question");
        question.setType("type");
        question.setCategory("category");

        when(questionRepository.findById(any(String.class))).thenReturn(Mono.just(question));
        when(answerRepository.save(any(Answer.class))).thenReturn(Mono.just(answer));
        when(answerRepository.findAllByQuestionId(any(String.class))).thenReturn(Flux.empty());

        StepVerifier.create(addAnswerUseCase.apply(answered))
                .expectNextMatches(questiondto -> {
                    assert questiondto.getId().equals(question.getId());
                    assert questiondto.getUserId().equals(question.getUserId());
                    assert questiondto.getQuestion().equals(question.getQuestion());
                    assert questiondto.getType().equals(question.getType());
                    assert questiondto.getCategory().equals(question.getCategory());
                    assert questiondto.getAnswers().contains(answered);
                    return true;
                })
                .verifyComplete();

        verify(questionRepository).findById("id");
    }
}