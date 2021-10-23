package ee.taltech.backoffice.game.controller;

import ee.taltech.backoffice.game.model.dto.QuestionDto;
import ee.taltech.backoffice.game.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("question")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public QuestionDto getQuestion(@RequestParam Long id) {
        return questionService.getQuestion(id);
    }

    @PostMapping
    public QuestionDto addQuestion(@Valid @RequestBody QuestionDto question, @RequestParam Long quizId) {
        return questionService.addQuestion(question, quizId);
    }

    @PutMapping
    public QuestionDto updateQuestion(@Valid @RequestBody QuestionDto question) {
        return questionService.editQuestion(question);
    }

    @DeleteMapping
    public void deleteQuestion(@RequestParam Long id, @RequestParam Long quizId) {
        questionService.deleteQuestion(id, quizId);
    }

}
