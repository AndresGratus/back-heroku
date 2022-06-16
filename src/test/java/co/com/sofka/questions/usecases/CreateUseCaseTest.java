package co.com.sofka.questions.usecases;

import co.com.sofka.questions.collections.Question;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class CreateUseCaseTest {

    @Mock
    QuestionRepository questionRepository;

    @Mock
    CreateUseCase createUseCase;

    MapperUtils mapperUtils = new MapperUtils();

    @BeforeEach
    public void setUp() {

        questionRepository = mock(QuestionRepository.class);
        createUseCase = new CreateUseCase(mapperUtils, questionRepository);

    }

    @Test
    void CreateTest() {
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

        StepVerifier.create(createUseCase.apply(questionDTO))

                .expectNextMatches(q -> {

                    assert questionDTO.getId().equals(question.getId());
                    assert questionDTO.getUserId().equals(question.getUserId());
                    assert questionDTO.getCategory().equals(question.getCategory());
                    assert questionDTO.getQuestion().equals(question.getQuestion());
                    assert questionDTO.getType().equals(question.getType());
                    return true;
                }).verifyComplete();

        verify(questionRepository).save(Mockito.any(Question.class));
    }
}