package guru.qa.niffler.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("category")
        String category,
        @JsonProperty("username")
        String username
) {

}
