package co.com.sofka.questions.service;

import co.com.sofka.questions.collections.Question;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.repositories.AnswerRepository;
import co.com.sofka.questions.repositories.QuestionRepository;
import co.com.sofka.questions.usecases.MapperUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class QuestionService {
    private MapperUtils mapperUtils;
    private AnswerRepository aRepository;
    private QuestionRepository qRepository;


    public QuestionService(AnswerRepository aRepository, QuestionRepository qRepository, MapperUtils mapperUtils){
        this.aRepository = aRepository;
        this.qRepository = qRepository;
        this.mapperUtils = mapperUtils;
    }
    ///FluxGetAllUseCase
    public Flux<QuestionDTO> getAll(){
        return qRepository.findAll()
                .map(mapperUtils.mapEntityToQuestion());
    }

    public Flux<QuestionDTO> getOwnerAll(String userId){
        return qRepository.findByUserId(userId)
                .map(mapperUtils.mapEntityToQuestion());
    }

    ///UpdateQuestionUseCase
    public Mono<String> updateQ(QuestionDTO dto){
        Objects.requireNonNull(dto.getId(), "Id of the question is required");
        return qRepository
                .save(mapperUtils.mapperToQuestion(dto.getId()).apply(dto))
                .map(Question::getId);
    }
    public Mono<String> createQuestion(QuestionDTO newQuestion){
        return qRepository
                .save(mapperUtils.mapperToQuestion(null).apply(newQuestion))
                .map(Question::getId);
    }

    public Mono<Void> delete(String id){
        Objects.requireNonNull(id, "Id is required");
        return qRepository.deleteById(id)
                .switchIfEmpty(Mono.defer(() -> aRepository.deleteByQuestionId(id)));
    }
}
