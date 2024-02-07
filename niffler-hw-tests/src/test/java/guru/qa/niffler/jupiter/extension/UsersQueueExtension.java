package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.jupiter.annotation.User.UserType.*;

public class UsersQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    private static Map<User.UserType, Queue<UserJson>> users = new ConcurrentHashMap<>();

    static {
        Queue<UserJson> friendsQueue = new ConcurrentLinkedQueue<>();
        Queue<UserJson> commonQueue = new ConcurrentLinkedQueue<>();
        Queue<UserJson> sentQueue = new ConcurrentLinkedQueue<>();
        Queue<UserJson> receivedQueue = new ConcurrentLinkedQueue<>();
        friendsQueue.add(user("dima", "12345", WITH_FRIENDS));
        friendsQueue.add(user("duck", "12345", WITH_FRIENDS));
        commonQueue.add(user("bee", "12345", COMMON));
        commonQueue.add(user("barsik", "12345", COMMON));
        sentQueue.add(user("cat", "12345", REQUEST_SENT));
        receivedQueue.add(user("dog", "12345", REQUEST_RECEIVED));
        users.put(WITH_FRIENDS, friendsQueue);
        users.put(COMMON, commonQueue);
        users.put(REQUEST_SENT, sentQueue);
        users.put(REQUEST_RECEIVED, receivedQueue);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        List<Method> methods = new ArrayList<>();
        methods.add(context.getRequiredTestMethod());
        Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(BeforeEach.class))
                .forEach(methods::add);

        List<Parameter> allParameters = methods.stream()
                .map(Executable::getParameters)
                .flatMap(Arrays::stream)
                .filter(parameter -> parameter.isAnnotationPresent(User.class))
                .filter(parameter -> parameter.getType().isAssignableFrom(UserJson.class))
                .toList();

        putUserFromParameterToContext(context, allParameters);
    }

    private void putUserFromParameterToContext(ExtensionContext context, List<Parameter> parameters) {
        HashMap<User.UserType, UserJson> usersInParams = new HashMap<>();

        for (Parameter parameter : parameters) {
            User.UserType userType = parameter.getAnnotation(User.class).value();

            if (usersInParams.containsKey(userType)) {
                continue;
            }

            UserJson testCandidate = null;
            Queue<UserJson> queue = users.get(userType);
            while (testCandidate == null) {
                testCandidate = queue.poll();
            }
            usersInParams.put(userType, testCandidate);
        }

        context.getStore(NAMESPACE).put(context.getUniqueId(), usersInParams);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        HashMap<User.UserType, UserJson> usersInParams = new HashMap<>(context.getStore(NAMESPACE)
                .get(context.getUniqueId(), Map.class));
        for (UserJson user : usersInParams.values()) {
            users.get(user.testData().userType()).add(user);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(UserJson.class) &&
                parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext context) throws
            ParameterResolutionException {
        return (UserJson) context.getStore(NAMESPACE)
                .get(context.getUniqueId(), Map.class)
                .get(parameterContext.findAnnotation(User.class).get().value());
    }

    private static UserJson user(String username, String password, User.UserType userType) {
        return new UserJson(
                null,
                username,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                new TestData(
                        password,
                        userType
                )
        );
    }
}
