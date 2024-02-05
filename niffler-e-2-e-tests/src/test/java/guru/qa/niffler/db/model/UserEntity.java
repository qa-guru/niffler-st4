package guru.qa.niffler.db.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserEntity {
  private UUID id;
  private String username;
  private CurrencyValues currency;
  private String firstname;
  private String surname;
  private byte[] photo;
}