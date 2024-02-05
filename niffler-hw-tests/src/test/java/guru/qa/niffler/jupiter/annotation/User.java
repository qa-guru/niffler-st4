package guru.qa.niffler.jupiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface User {

  UserType value() default UserType.COMMON;

  enum UserType {
    WITH_FRIENDS, COMMON, REQUEST_SENT, REQUEST_RECEIVED
  }
}
