package co.com.sofka.questions.usecases;

import co.com.sofka.questions.collections.Question;
import co.com.sofka.questions.repositories.AnswerRepository;
import co.com.sofka.questions.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class DeleteUseCaseTest {

    QuestionRepository questionRepository;
    AnswerRepository answerRepository;
    DeleteUseCase deleteUseCase;

    @BeforeEach
    public void setup() {
        questionRepository = mock(QuestionRepository.class);
        answerRepository = mock(AnswerRepository.class);
        deleteUseCase = new DeleteUseCase(answerRepository, questionRepository);
    }

    @Test
    void DeleteTest() {
        var question = new Question();
        question.setId("id");
        question.setUserId("UserId");
        question.setType("type");
        question.setCategory("category");
        question.setQuestion("question");

        Mono.just(question).flatMap(questionRepository::save);

        when(questionRepository.deleteById(question.getId())).thenReturn(Mono.empty());

        StepVerifier.create(deleteUseCase.apply(question.getId()))
                .expectNextMatches(q->{
                    assert q.equals(question);
                    return true;
                }).expectComplete();

        verify(questionRepository).deleteById(question.getId());
    }
}