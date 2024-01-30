package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DbUserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(DbUserExtension.class);

    static String userAuthKey = "userAuth";
    static String userdataKey = "userdata";

    private UserRepository userRepository = new UserRepositoryJdbc();

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {

        Optional<DbUser> dbUser = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                DbUser.class
        );

        UserAuthEntity userAuth;
        UserEntity user;

        userAuth = new UserAuthEntity();
        userAuth.setUsername(dbUser.get().username());
        userAuth.setPassword(dbUser.get().password());
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

        user = new UserEntity();
        user.setUsername(dbUser.get().username());
        user.setCurrency(CurrencyValues.USD);

        userRepository.createInAuth(userAuth);
        userRepository.createInUserdata(user);

        Map<String, Object> userEntities = new HashMap<>();
        userEntities.put(userAuthKey, userAuth);
        userEntities.put(userdataKey, user);

        extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), userEntities);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {

        Map userEntities = extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);

        UserAuthEntity userAuth = (UserAuthEntity) userEntities.get(userAuthKey);
        UserEntity user = (UserEntity) userEntities.get(userdataKey);

        userRepository.deleteInAuthById(userAuth.getId());
        userRepository.deleteInUserdataById(user.getId());
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserAuthEntity.class);
    }

    @Override
    public UserAuthEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map userEntities = extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);

        return (UserAuthEntity) userEntities.get(userAuthKey);
    }
}
