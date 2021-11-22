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
        Player player = playerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Player was not found"));
        return new PlayerDto(player);
    }

    public void deletePlayer(long id) {
        Player player = playerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Player was not found"));
        playerRepository.delete(player);
    }

    public PlayerDto savePlayer(PlayerDto dto) {
        Player player = new Player(dto);
        playerRepository.save(player);
        return dto;
    }
}