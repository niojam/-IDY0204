package ee.taltech.publicapplication.game.service;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.model.Room;
import ee.taltech.publicapplication.game.model.RoomStatus;
import ee.taltech.publicapplication.game.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Set;

import static ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest.Code.BUSINESS_LOGIC_EXCEPTION;
import static ee.taltech.publicapplication.game.model.RoomStatus.ANSWERING;
import static ee.taltech.publicapplication.game.model.RoomStatus.REVIEWING;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Mono<Room> findById(Long roomId) {
        return roomRepository
                .findById(roomId)
                .switchIfEmpty(Mono.error(
                        new BadRequest(
                                BUSINESS_LOGIC_EXCEPTION,
                                format("No room with id=%d found", roomId))));
    }

    @Transactional
    public Mono<Room> save(Room room) {
        return roomRepository.save(room);
    }

    public Room setStatus(Room room, RoomStatus nextStatus) {
        if (room.getStatus() == REVIEWING && nextStatus == ANSWERING && room.getCurrentQuestionId() == null) {
            throw new BadRequest(BUSINESS_LOGIC_EXCEPTION,
                    "You cannot set room to ANSWERING because all question are answered");
        }
        if (canChangeStatus(room.getStatus(), nextStatus)) {
            return room.setStatus(nextStatus);
        }
        throw new BadRequest(BUSINESS_LOGIC_EXCEPTION,
                format("Cannot set room status to %s", nextStatus));
    }

    public String generateRoomPin(Set<String> existingPins) {
        // use roomPin = "820379" if you make a load test
        String roomPin = generateRandomPin();
        while (existingPins.contains(roomPin)) {
            roomPin = generateRandomPin();
        }
        return roomPin;
    }

    private String generateRandomPin() {
        return RandomStringUtils.randomNumeric(6);
    }

    private Boolean canChangeStatus(RoomStatus current, RoomStatus next) {
        if (next == ANSWERING && current == REVIEWING) {
            return true;
        } else return next.getStage() == current.getStage() + 1;
    }

}
