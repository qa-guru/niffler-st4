package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserDbExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {
    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UserDbExtension.class);
    private static final String USER_AUTH_KEY = "userAuth";
    private static final String USER_KEY = "user";
    private static final String DELETE_KEY = "deleteAfterTest";
    private final Faker faker = new Faker();
    private final UserRepository userRepository = new UserRepositoryJdbc();

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<UserDb> dbUser = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                UserDb.class
        );

        String userName = null;
        String password = null;
        boolean deleteAfterTest = false;

        if (dbUser.isPresent()) {
            UserDb dbUserData = dbUser.get();
            userName = dbUserData.username().isEmpty() ? faker.name().firstName() : dbUserData.username();
            password = dbUserData.password().isEmpty()
                    ? String.valueOf(faker.number().numberBetween(10000, 99999))
                    : dbUserData.password();
            deleteAfterTest = dbUserData.deleteAfterTest();
        }

        UserAuthEntity userAuth = new UserAuthEntity();
        UserEntity user = new UserEntity();

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

        user.setUsername(userName);
        user.setCurrency(CurrencyValues.RUB);

        userRepository.createInAuth(userAuth);
        userRepository.createInUserdata(user);

        Map<String, Object> userEntities = new HashMap<>();
        userEntities.put(USER_AUTH_KEY, userAuth);
        userEntities.put(USER_KEY, user);
        userEntities.put(DELETE_KEY, deleteAfterTest);

        extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), userEntities);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        Map<String, Object> userEntities = (Map<String, Object>) extensionContext
                .getStore(UserDbExtension.NAMESPACE).get(extensionContext.getUniqueId());

        boolean needToDeleteUser = (boolean) userEntities.get(DELETE_KEY);

        if (needToDeleteUser) {
            UserAuthEntity userAuth = (UserAuthEntity) userEntities.get(USER_AUTH_KEY);
            UserEntity user = (UserEntity) userEntities.get(USER_KEY);

            userRepository.deleteInAuthById(userAuth.getId());
            userRepository.deleteInUserdataById(user.getId());
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(UserAuthEntity.class);
    }

    @Override
    public UserAuthEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (UserAuthEntity) extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class)
                .get(USER_AUTH_KEY);
    }

}
