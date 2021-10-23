package ee.taltech.publicapplication.game.service;

import ee.taltech.publicapplication.game.handler.answer_handler.AnswerHandler;
import ee.taltech.publicapplication.game.handler.playerScores.ScoreHandler;
import ee.taltech.publicapplication.game.handler.room.RoomHandler;
import ee.taltech.publicapplication.game.model.dto.AnswerDto;
import ee.taltech.publicapplication.game.model.dto.ScoreDto;
import ee.taltech.publicapplication.game.model.dto.common.GeneralResponse;
import ee.taltech.publicapplication.game.model.dto.give_answer.GiveAnswerRequest;
import ee.taltech.publicapplication.game.model.mapper.AnswerMapper;
import ee.taltech.publicapplication.game.model.mapper.ScoreMapper;
import ee.taltech.publicapplication.game.repository.AnswerRepository;
import ee.taltech.publicapplication.game.security.user_details.PublicAppUserDetails;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {

    private final RoomHandler roomHandler;
    private final AnswerHandler answerHandler;
    private final ScoreHandler scoreHandler;
    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper = Mappers.getMapper(AnswerMapper.class);
    private final ScoreMapper scoreMapper = Mappers.getMapper(ScoreMapper.class);

    public Flux<AnswerDto> findAnswersForQuestion(Long id) {
        return answerRepository.findAllByQuestionId(id)
                .map(answerMapper::toDto);
    }

    public Mono<GeneralResponse> registerPlayerAnswer(PublicAppUserDetails userDetails, GiveAnswerRequest answerRequest) {
        return scoreHandler.updatePlayerScore(userDetails, answerRequest)
                .map(any -> roomHandler.isCurrentQuestionId(userDetails.getRoomId(), answerRequest.getQuestionId()))
                .map(any -> {
                    answerHandler.registerAnswer(userDetails.getRoomId(), answerRequest.getAnswerIds());
                    return new GeneralResponse();
                });
    }

    public Flux<ScoreDto> getScore(Long roomId, Long playerId) {
        return scoreHandler
                .getProcessor(roomId)
                .map(event -> scoreHandler.getScore(roomId, playerId))
                .map(scoreMapper::toDto);
    }

    public Mono<Long> createPlayerScore(Long roomId, Long playerId, String username) {
        return Mono.just(scoreHandler.createPlayerScore(roomId, playerId, username));
    }

}
