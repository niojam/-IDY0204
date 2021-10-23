package ee.taltech.backoffice.game.controller;

import ee.taltech.backoffice.game.model.dto.AnswerFrequencyDto;
import ee.taltech.backoffice.game.model.dto.PlayerStatistics;
import ee.taltech.backoffice.game.model.dto.QuestionDetails;
import ee.taltech.backoffice.game.model.dto.RoomStatistics;
import ee.taltech.backoffice.game.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("statistics")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticsService statisticsService;

    @GetMapping
    public RoomStatistics getStatistics(@RequestParam Long roomId) {
            return statisticsService.getStatisticsForRoom(roomId);
    }

    @GetMapping("player")
    public PlayerStatistics getPlayerStatistics(@RequestParam Long roomId, @RequestParam Long playerId) {
        return statisticsService.getStatisticsForPlayer(roomId, playerId);
    }

    @GetMapping("questions")
    public List<QuestionDetails> getQuestionDetails(@RequestParam Long quizId) {
        return statisticsService.getQuestionDetails(quizId);
    }

    @GetMapping("answer")
    public List<AnswerFrequencyDto> getAnswerStatistics(@RequestParam Long roomId, @RequestParam Long questionId){
        return statisticsService.getAnswerStatistics(roomId, questionId);
    }

    @GetMapping("players")
    public List<PlayerStatistics> getStatisticsForAllPlayers(@RequestParam Long roomId) {
        return statisticsService.getStatisticsForAllPlayers(roomId);
    }

    @GetMapping("pdf")
    public void downloadPdf(@RequestParam Long roomId, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader(CONTENT_DISPOSITION, "attachment; filename=\"test.pdf\"");
        statisticsService.generatePdf(roomId, response.getOutputStream());
    }

}
