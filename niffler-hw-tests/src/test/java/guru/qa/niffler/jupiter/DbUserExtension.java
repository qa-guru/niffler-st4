package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.Optional;

public class DbUserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(DbUserExtension.class);

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

        extensionContext.getStore(NAMESPACE).put("userAuth", userAuth);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserAuthEntity.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(DbUserExtension.NAMESPACE)
                .get("userAuth", UserAuthEntity.class);
    }
}
