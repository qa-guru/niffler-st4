package guru.qa.niffler.db.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityEntity {
  private UUID id;
  private UUID userId;
  private Authority authority;
}
