package co.com.sofka.questions.controller;


import co.com.sofka.questions.model.AnswerDTO;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.service.AnswerService;
import co.com.sofka.questions.service.QuestionService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class QuestionController {

    private  AnswerService answerService;

    private  QuestionService questionService;

    public QuestionController(AnswerService answerService, QuestionService questionService) {
        this.answerService = answerService;
        this.questionService = questionService;
    }

    @GetMapping("/getAll")
    public Flux<QuestionDTO> getAll(){
        return questionService.getAll();
    }

    @GetMapping("/getOwnerAll/{userId}")
    public Flux<QuestionDTO> getOwnerAll(@PathVariable("userId") String userId){
        return questionService.getOwnerAll(userId);
    }

    @GetMapping("/get/{id}")
    public Mono<QuestionDTO> getQuestion(@PathVariable("id") String id){
        return answerService.getQuestion(id);
    }

    @PostMapping("/create")
    public Mono<String> create(@RequestBody QuestionDTO questionDTO){
        return questionService.createQuestion(questionDTO);
    }

    @PostMapping("/add")
    public Mono<QuestionDTO> addAnswer(@RequestBody AnswerDTO answerDTO){
        return answerService.addAnswer(answerDTO);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> delete(@PathVariable("id") String id){
        return questionService.delete(id);
    }
}
