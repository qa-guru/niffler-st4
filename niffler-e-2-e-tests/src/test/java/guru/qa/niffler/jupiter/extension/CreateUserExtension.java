package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryHibernate;
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.utils.DataUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class CreateUserExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace DB_CREATE_USER_NAMESPACE
      = ExtensionContext.Namespace.create(CreateUserExtension.class);

  private static UserRepository userRepository = new UserRepositoryHibernate();


  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    Optional<DbUser> dbUserAnnotation = AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        DbUser.class
    );

    if (dbUserAnnotation.isPresent()) {
      DbUser dbUser = dbUserAnnotation.get();
      String username = dbUser.username().isEmpty()
          ? DataUtils.generateRandomUsername()
          : dbUser.username();
      String password = dbUser.password().isEmpty()
          ? "12345"
          : dbUser.password();

      UserAuthEntity userAuth = new UserAuthEntity();
      userAuth.setUsername(username);
      userAuth.setPassword(password);
      userAuth.setEnabled(true);
      userAuth.setAccountNonExpired(true);
      userAuth.setAccountNonLocked(true);
      userAuth.setCredentialsNonExpired(true);
      AuthorityEntity[] authorities = Arrays.stream(Authority.values()).map(
          a -> {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setAuthority(a);
            return ae;
          }
      ).toArray(AuthorityEntity[]::new);

      userAuth.addAuthorities(authorities);

      UserEntity user = new UserEntity();
      user.setUsername(username);
      user.setCurrency(CurrencyValues.RUB);

      userRepository.createInAuth(userAuth);
      userRepository.createInUserdata(user);

      Map<String, Object> createdUser = Map.of(
          "auth", userAuth,
          "userdata", user
      );

      extensionContext.getStore(DB_CREATE_USER_NAMESPACE).put(extensionContext.getUniqueId(), createdUser);
    }
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    Map createdUser = extensionContext.getStore(DB_CREATE_USER_NAMESPACE)
        .get(extensionContext.getUniqueId(), Map.class);
    userRepository.deleteInAuthById(((UserAuthEntity) createdUser.get("auth")).getId());
    userRepository.deleteInUserdataById(((UserEntity) createdUser.get("userdata")).getId());
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), DbUser.class)
        .isPresent() &&
        parameterContext.getParameter().getType().isAssignableFrom(UserAuthEntity.class);
  }

  @Override
  public UserAuthEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return (UserAuthEntity) extensionContext.getStore(DB_CREATE_USER_NAMESPACE).get(extensionContext.getUniqueId(), Map.class)
        .get("auth");
  }
}
