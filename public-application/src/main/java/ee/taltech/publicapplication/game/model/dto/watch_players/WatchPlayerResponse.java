package ee.taltech.publicapplication.game.model.dto.watch_players;

import ee.taltech.publicapplication.game.model.dto.common.GeneralResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WatchPlayerResponse extends GeneralResponse {

    private Long playerId;
    private String username;
    private Status status;

    public enum Status {
        REMOVED,
        ADDED
    }

}
