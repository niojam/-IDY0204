package ee.taltech.publicapplication.game.service;

import ee.taltech.publicapplication.game.handler.room.RoomHandler;
import ee.taltech.publicapplication.game.model.Room;
import ee.taltech.publicapplication.game.model.dto.QuizDto;
import ee.taltech.publicapplication.game.security.user_details.PublicAppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

    private final RoomService roomService;
    private final QuizService quizService;
    private final RoomHandler roomHandler;

    public Mono<QuizDto> getQuiz(PublicAppUserDetails publicAppUserDetails) {
        return roomService
                .findById(publicAppUserDetails.getRoomId())
                .map(Room::getQuizId)
                .flatMap(quizService::findById);
    }

}
