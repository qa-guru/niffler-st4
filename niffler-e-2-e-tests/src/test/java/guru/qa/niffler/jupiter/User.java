package guru.qa.niffler.jupiter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface User {

  UserType value();

  enum UserType {
    WITH_FRIENDS, INVITATION_SEND, INVITATION_RECIEVED
  }
}
