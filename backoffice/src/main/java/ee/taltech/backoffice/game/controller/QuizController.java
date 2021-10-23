package ee.taltech.backoffice.game.controller;

import ee.taltech.backoffice.game.model.dto.QuizDetails;
import ee.taltech.backoffice.game.model.dto.QuizDto;
import ee.taltech.backoffice.game.service.QuizService;
import ee.taltech.backoffice.security.filter.user_details.BackofficeUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping
    public QuizDto getQuiz(@RequestParam Long quizId, @AuthenticationPrincipal BackofficeUserDetails userDetails) {
        return quizService.getQuiz(quizId, userDetails.getUserId());
    }

    @GetMapping("quizzes")
    public List<QuizDetails> getAllQuizzes(@AuthenticationPrincipal BackofficeUserDetails userDetails) {
        return quizService.getQuizzes(userDetails.getUserId());
    }

    @DeleteMapping
    public void deleteQuiz(@RequestParam Long quizId, @AuthenticationPrincipal BackofficeUserDetails userDetails) {
        quizService.deleteQuiz(quizId, userDetails.getUserId());
    }

    @PostMapping
    public QuizDto createQuiz(@Valid @RequestBody QuizDto quizDto, @AuthenticationPrincipal BackofficeUserDetails userDetails) {
        return quizService.createQuiz(quizDto, userDetails.getUserId());
    }

}
