package ee.taltech.backoffice.game.controller;

import ee.taltech.backoffice.game.model.dto.AnswerDto;
import ee.taltech.backoffice.game.service.AnswerService;
import ee.taltech.backoffice.security.filter.user_details.BackofficeUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping
    public AnswerDto getAnswer(@RequestParam Long answerId, @RequestParam Long quizId, @AuthenticationPrincipal BackofficeUserDetails authentication) {
        return answerService.getAnswer(answerId);
    }

    @PutMapping
    public AnswerDto updateAnswer(@Valid @RequestBody AnswerDto answer, @RequestParam Long quizId) {
        return answerService.updateAnswer(answer);
    }

    @DeleteMapping
    public void deleteAnswer(@RequestParam Long id, @RequestParam Long quizId) {
        answerService.deleteAnswer(id);
    }

    @PostMapping
    public AnswerDto addAnswer(@Valid @RequestBody AnswerDto answer, @RequestParam Long id, @RequestParam Long quizId) {
        return answerService.addAnswer(answer, id);
    }

}
