package ee.taltech.publicapplication.game.model.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum ResponseStatus {

    @JsonProperty("403")
    KICKED,

    @JsonProperty("500")
    ERROR,

    @JsonProperty("200")
    OK,

    @JsonProperty("204")
    COMPLETED

}
