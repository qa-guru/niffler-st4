package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryHibernate;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import guru.qa.niffler.db.repository.UserRepositorySJdbc;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

public class UserRepositoryExtension implements TestInstancePostProcessor {
    @Override
    public void postProcessTestInstance(Object o, ExtensionContext extensionContext) throws Exception {
        for (Field field : o.getClass().getDeclaredFields()) {
            if (field.getType().isAssignableFrom(UserRepository.class)) {
                field.setAccessible(true);

                UserRepository repository;
                switch (System.getProperty("repository")) {
                    case "jdbc" -> repository = new UserRepositoryJdbc();
                    case "sjdbc" -> repository = new UserRepositorySJdbc();
                    case "hibernate" -> repository = new UserRepositoryHibernate();
                    default -> throw new RuntimeException("Incorrect repository in parameter.");
                }
                field.set(o, repository);
            }
        }
    }
}
