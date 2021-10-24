package ee.taltech.backoffice.game.service;

import ee.taltech.backoffice.game.model.Player;
import ee.taltech.backoffice.game.model.dto.PlayerDto;
import ee.taltech.backoffice.game.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerDto getPlayer(long id) {
        return null;

    }

    public void deletePlayer(long id) {
    }

    public PlayerDto savePlayer(PlayerDto dto) {
        return null;

    }

}