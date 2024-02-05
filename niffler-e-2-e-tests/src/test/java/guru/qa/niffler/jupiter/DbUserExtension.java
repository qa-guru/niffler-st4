package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.DbUser;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.*;

public class DbUserExtension implements ParameterResolver, BeforeEachCallback, AfterTestExecutionCallback {
    private Faker faker = new Faker();
    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(DbUserExtension.class);
    private final static String userAuthKey = "userAuth";
    private final static String userKey = "user";
    private final UserRepositoryJdbc userRepositoryJdbc = new UserRepositoryJdbc();

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        Optional<DbUser> dbUser = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                DbUser.class
        );

        Map userData = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);

        if (dbUser.isPresent()) {
            userRepositoryJdbc.deleteInAuthById(((UserAuthEntity) userData.get(userAuthKey)).getId());
            userRepositoryJdbc.deleteInUserdataById(((UserEntity) userData.get(userKey)).getId());
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        Optional<DbUser> dbUser = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                DbUser.class);

        Map<String, Object> userData = new HashMap<>();

        if (dbUser.isPresent()) {
            DbUser dbUserData = dbUser.get();
            String userName = dbUserData.username().isEmpty() ? faker.name().username() : dbUserData.username();
            String password = dbUserData.password().isEmpty() ? faker.internet().password() : dbUserData.password();

            UserAuthEntity userAuth = new UserAuthEntity();
            userAuth.setUsername(userName);
            userAuth.setPassword(password);
            userAuth.setEnabled(true);
            userAuth.setAccountNonExpired(true);
            userAuth.setAccountNonLocked(true);
            userAuth.setCredentialsNonExpired(true);
            userAuth.setAuthorities(Arrays.stream(Authority.values())
                    .map(e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(e);
                        return ae;
                    }).toList()
            );

            UserEntity user = new UserEntity();
            user.setUsername(userName);
            user.setUsername(password);
            user.setCurrency(CurrencyValues.RUB);

            userRepositoryJdbc.createInAuth(userAuth);
            userRepositoryJdbc.createInUserdata(user);

            userData.put(userAuthKey, userAuth);
            userData.put(userKey, user);
        }
        extensionContext.getStore(NAMESPACE)
                .put(extensionContext.getUniqueId(), userData);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(UserAuthEntity.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class)
                .get(userAuthKey);
    }
}
