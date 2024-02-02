package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class DbUserCRUDExtension implements ParameterResolver, BeforeEachCallback,
    AfterTestExecutionCallback{

  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(DbUserCRUDExtension.class);

  private final UserRepository userRepository = new UserRepositoryJdbc();

  private final String fakeUserPassword = "12345";

  static String userAuthKey = "userAuthDB";
  static String userdataKey = "userdataDB";

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    List<Method> methods = new ArrayList<>();
    Arrays.stream(extensionContext.getRequiredTestClass().getDeclaredMethods())
        .filter(m -> m.isAnnotationPresent(BeforeEach.class))
        .forEach(methods::add);
    methods.add(extensionContext.getRequiredTestMethod());

    Optional<Method> methodAnnotatedByUser = methods
        .stream()
        .filter(method -> method.isAnnotationPresent(DbUser.class))
        .findFirst();

    if(methodAnnotatedByUser.isEmpty()) {
      return;
    }

    UserAuthEntity userAuthEntity = new UserAuthEntity();
    UserEntity userEntity = new UserEntity();

    DbUser dbUser = methodAnnotatedByUser.get().getAnnotation(DbUser.class);

    String fakeRandomUser = UUID.randomUUID().toString();

    userEntity.setUsername(dbUser.username().equals("") ? fakeRandomUser : dbUser.username());
    userEntity.setCurrency(CurrencyValues.RUB);
    userRepository.createInUserdata(userEntity);

    userAuthEntity.setUsername(dbUser.username().equals("") ? fakeRandomUser : dbUser.username());
    userAuthEntity.setPassword(dbUser.password().equals("") ? fakeUserPassword : dbUser.password());
    userAuthEntity.setEnabled(true);
    userAuthEntity.setAccountNonExpired(true);
    userAuthEntity.setAccountNonLocked(true);
    userAuthEntity.setCredentialsNonExpired(true);
    userAuthEntity.setAuthorities(Arrays.stream(Authority.values())
        .map(e -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setAuthority(e);
          return ae;
        }).toList()
    );


    userRepository.createInAuth(userAuthEntity);

    Map<String, Object> userData = new HashMap<>();
    userData.put(userAuthKey, userAuthEntity);
    userData.put(userdataKey, userEntity);
    extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), userData);
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) {
    Map userDataMap = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
    UserEntity userEntity = (UserEntity) userDataMap.get(userdataKey);
    UserAuthEntity userAuthEntity = (UserAuthEntity) userDataMap.get(userAuthKey);

    userRepository.deleteInAuthById(userAuthEntity.getId());
    userRepository.deleteInUserdataById(userEntity.getId());

  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), DbUser.class)
        .isPresent() &&
        parameterContext.getParameter().getType().isAssignableFrom(UserAuthEntity.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    Map users = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
    return users.get(userAuthKey);
  }

}
