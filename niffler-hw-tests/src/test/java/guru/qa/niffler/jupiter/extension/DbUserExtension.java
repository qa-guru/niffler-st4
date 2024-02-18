package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.logging.JsonAttachment;
import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.DbUser;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DbUserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(DbUserExtension.class);

    Faker faker = new Faker();

    static String userAuthKey = "userAuth";
    static String userdataKey = "userdata";

    private UserRepository userRepository = new UserRepositoryJdbc();

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {

        UserAuthEntity userAuth = new UserAuthEntity();
        UserEntity user = new UserEntity();

        Optional<DbUser> dbUser = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                DbUser.class
        );

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

        user.setUsername(dbUser.get().username());
        user.setCurrency(CurrencyValues.USD);
        user.setFirstname(faker.name().firstName());
        user.setSurname(faker.name().lastName());

        if (dbUser.get().username().isEmpty()){
            String randomUsername = faker.name().username();
            userAuth.setUsername(randomUsername);
            userAuth.setPassword("12345");
            user.setUsername(randomUsername);
        }

        userRepository.createInAuth(userAuth);
        userRepository.createInUserdata(user);

        Map<String, Object> userEntities = new HashMap<>();
        userEntities.put(userAuthKey, userAuth);
        userEntities.put(userdataKey, user);

        extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), userEntities);

        JsonAttachment.attachJson(user.toFormattedJson());
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
