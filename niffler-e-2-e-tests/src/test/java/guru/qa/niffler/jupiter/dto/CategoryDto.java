package guru.qa.niffler.jupiter.dto;

import java.util.UUID;

public record CategoryDto(
    UUID id,
    String userName,
    String category
) {

}
