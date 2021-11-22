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
        return roomRepository
                .findById(id)
                .orElseThrow(() -> new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION, "Room not found"));
    }

    public RoomStatistics getStatisticsForRoom(Long id) {
        Room room = roomRepository
                .findById(id)
                .orElseThrow(() -> new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION, "Room not found"));

        List<PlayedQuestion> playedQuestions = playedQuestionRepository.findByQuizId(room.getPlayedQuizId());

        List<AnswerFrequencyDto> answerFrequencies = playedQuestions.stream()
                .map(playedQuestion -> getAnswerStatistics(room.getId(), playedQuestion.getId()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        PlayedQuiz playedQuiz = quizRepository.findById(room.getPlayedQuizId()).orElseThrow();
        List<PlayerStatistics> playerStatistics = getStatisticsForAllPlayers(id);

        return new RoomStatistics()
                .setRoomId(room.getId())
                .setRoomName(room.getName())
                .setAuthorId(room.getAuthorId())
                .setQuizId(room.getQuizId())
                .setQuizName(playedQuiz.getName())
                .setAnswerFrequencies(answerFrequencies)
                .setPlayerStatistics(playerStatistics);
    }

    public PlayerStatistics getStatisticsForPlayer(Long roomId, Long playerId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION, "Room not found"));
        Score score = scoreRepository.findByRoomIdAndPlayerId(room.getId(), playerId)
                .orElseThrow(() -> new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION, "Score not found"));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION, "Player not found"));

        return new PlayerStatistics()
                .setRoomId(room.getId())
                .setUsername(player.getUsername())
                .setScore(score.getScore())
                .setCorrectAnswers(score.getCorrectAnswers())
                .setWrongAnswers(score.getWrongAnswers());
    }

    public List<AnswerFrequencyDto> getAnswerStatistics(Long roomId, Long questionId) {
        List<AnswerFrequency> answerFrequencies = answerFrequencyRepository.findByRoomIdAndQuestionId(roomId, questionId);
        return answerFrequencies.stream().map(answerFrequency -> {
            PlayedAnswer answer = playedAnswerRepository.findById(answerFrequency.getAnswerId())
                    .orElseThrow(() -> new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION, "Answer not found"));
            PlayedQuestion question = playedQuestionRepository.findById(questionId)
                    .orElseThrow(() -> new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION, "Question not found"));
            return new AnswerFrequencyDto()
                    .setQuestionId(questionId)
                    .setQuestionTitle(question.getTitle())
                    .setQuestionText(question.getText())
                    .setAnswerText(answer.getText())
                    .setCorrect(answer.getIsCorrect())
                    .setFrequency(answerFrequency.getFrequency());
        }).collect(Collectors.toList());
    }

    public List<PlayerStatistics> getStatisticsForAllPlayers(Long roomId) {
        List<Player> players = playerRepository.findByRoomId(roomId);
        return players.stream()
                .map(player -> getStatisticsForPlayer(roomId, player.getId()))
                .collect(Collectors.toList());
    }

    public void generatePdf(Long roomId, ServletOutputStream outputStream) {
        RoomStatistics roomStatistics = getStatisticsForRoom(roomId);
        List<QuestionStatisticsDto> questionStatisticsDtos = getQuestionStatistics(roomStatistics);
        pdfGeneratorService.generatePdf(roomStatistics, questionStatisticsDtos, outputStream);
    }

    public List<QuestionDetails> getQuestionDetails(Long quizId) {
        return playedQuestionRepository.findQuestionDetails(quizId);
    }

    private List<QuestionStatisticsDto> getQuestionStatistics(RoomStatistics roomStatistics) {
        List<QuestionStatisticsDto> questionStatistics = new ArrayList<>();
        roomStatistics.getAnswerFrequencies().forEach(answerFrequency -> {
            Optional<QuestionStatisticsDto> questionStatisticsDto = questionStatistics.stream()
                    .filter(q -> q.getQuestionId().equals(answerFrequency.getQuestionId()))
                    .findFirst();

            if (questionStatisticsDto.isPresent()) {
                questionStatisticsDto.get().getAnswerFrequencies().add(answerFrequency);
            } else {
                List<AnswerFrequencyDto> answerFrequencyDtos = new ArrayList<>();
                answerFrequencyDtos.add(answerFrequency);
                questionStatistics.add(new QuestionStatisticsDto()
                        .setQuestionId(answerFrequency.getQuestionId())
                        .setQuestionTitle(answerFrequency.getQuestionTitle())
                        .setQuestionText(answerFrequency.getQuestionText())
                        .setAnswerFrequencies(answerFrequencyDtos));
            }
        });
        return questionStatistics;
    }

}
