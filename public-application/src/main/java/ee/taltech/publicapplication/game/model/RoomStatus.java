package ee.taltech.publicapplication.game.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoomStatus {

    REGISTERED(0), // room is created, joining is not possible
    OPEN(1),       // joining the room is available
    READY(2),      // joining the room has ended, game is ready to start
    ANSWERING(3),  // client can answer a question
    REVIEWING(4),  // answering last question has ended, teacher can start new question
    FINISHED(5),   // quiz has ended
    ABORTED(100);  // author has disconnected while game is not FINISHED

    private final int stage;

}