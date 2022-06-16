package co.com.sofka.questions.service;

import co.com.sofka.questions.model.AnswerDTO;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.repositories.AnswerRepository;
import co.com.sofka.questions.repositories.QuestionRepository;
import co.com.sofka.questions.usecases.MapperUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Function;


//Traemos los Use case y los implementamos en un servicio
@Service
public class AnswerService {
    private MapperUtils mapperUtils;
    private AnswerRepository  aRepository;
    private QuestionRepository qRepository;


    public AnswerService(AnswerRepository aRepository, QuestionRepository qRepository, MapperUtils mapperUtils){
        this.aRepository = aRepository;
        this.qRepository = qRepository;
        this.mapperUtils = mapperUtils;
    }

    public Mono<QuestionDTO> addAnswer(AnswerDTO answerDTO){
        Objects.requireNonNull(answerDTO.getQuestionId(), "Id of the answer is required");
        return getQuestion(answerDTO.getQuestionId())
                .flatMap(question ->
                        aRepository.save(mapperUtils.mapperToAnswer().apply(answerDTO))
                                .map(answer -> {
                                    question.getAnswers().add(answerDTO);
                                    return question;
                                })
                );
    }



    private Function<QuestionDTO, Mono<QuestionDTO>> mapQuestionAggregate() {
        return questionDTO ->
                Mono.just(questionDTO).zipWith(
                        aRepository.findAllByQuestionId(questionDTO.getId())
                                .map(mapperUtils.mapEntityToAnswer())
                                .collectList(),
                        (question, answers) -> {
                            question.setAnswers(answers);
                            return question;
                        }
                );
    }

    public Mono<QuestionDTO> getQuestion(String id) {
        Objects.requireNonNull(id, "Id is required");
        return qRepository.findById(id)
                .map(mapperUtils.mapEntityToQuestion())
                .flatMap(mapQuestionAggregate());
    }



}
