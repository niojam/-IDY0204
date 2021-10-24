package ee.taltech.backoffice.game.service;

import ee.taltech.backoffice.game.controller.errorHandling.BadRequest;
import ee.taltech.backoffice.game.model.*;
import ee.taltech.backoffice.game.model.dto.*;
import ee.taltech.backoffice.game.repository.*;
import ee.taltech.backoffice.game.service.pdf.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final RoomRepository roomRepository;
    private final AnswerFrequencyRepository answerFrequencyRepository;
    private final PlayedAnswerRepository playedAnswerRepository;
    private final PlayedQuestionRepository playedQuestionRepository;
    private final PlayerRepository playerRepository;
    private final ScoreRepository scoreRepository;
    private final PdfGeneratorService pdfGeneratorService;
    private final PlayedQuizRepository quizRepository;

    public Room getRoom(Long id) {
        return null;
    }

    public RoomStatistics getStatisticsForRoom(Long id) {
        return null;
    }

    public PlayerStatistics getStatisticsForPlayer(Long roomId, Long playerId) {
        return null;
    }

    public List<AnswerFrequencyDto> getAnswerStatistics(Long roomId, Long questionId) {
        return null;
    }

    public List<PlayerStatistics> getStatisticsForAllPlayers(Long roomId) {
        return null;
    }

    public void generatePdf(Long roomId, ServletOutputStream outputStream) {
    }

    public List<QuestionDetails> getQuestionDetails(Long quizId) {
        return null;    }

    private List<QuestionStatisticsDto> getQuestionStatistics(RoomStatistics roomStatistics) {
        return null;
    }

}
