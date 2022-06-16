package co.com.sofka.questions.usecases;

import co.com.sofka.questions.collections.Question;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class UpdateUseCaseTest {

    MapperUtils mapperUtils;
    QuestionRepository questionRepository;
    UpdateUseCase updateUseCase;

    @BeforeEach
    public void setUp(){
        questionRepository = mock(QuestionRepository.class);
        mapperUtils = new MapperUtils();
        updateUseCase = new UpdateUseCase(mapperUtils,questionRepository);
    }

    @Test
    public void setUpdateUseCase() {
        var question = new Question();
        question.setId("id");
        question.setUserId("UserId");
        question.setQuestion("question");
        question.setType("type");
        question.setCategory("category");

        var questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setUserId(question.getUserId());
        questionDTO.setQuestion(question.getQuestion());
        questionDTO.setType(question.getType());
        questionDTO.setCategory(question.getCategory());

        when(questionRepository.save(Mockito.any(Question.class))).thenReturn(Mono.just(question));

        StepVerifier.create(updateUseCase.apply(questionDTO))

                .expectNextMatches(q -> {
                    assert questionDTO.getId().equalsIgnoreCase(question.getId());
                    assert questionDTO.getUserId().equalsIgnoreCase(question.getUserId());
                    assert questionDTO.getCategory().equalsIgnoreCase(question.getCategory());
                    assert questionDTO.getQuestion().equalsIgnoreCase(question.getQuestion());
                    assert questionDTO.getType().equalsIgnoreCase(question.getType());
                    return true;
                }).verifyComplete();

        verify(questionRepository).save(Mockito.any(Question.class));
    }
}