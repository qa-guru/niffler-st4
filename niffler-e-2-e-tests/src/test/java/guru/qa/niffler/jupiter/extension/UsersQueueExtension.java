package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

import static guru.qa.niffler.jupiter.annotation.User.UserType.*;

public class UsersQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    private static Map<User.UserType, Queue<UserJson>> users = new ConcurrentHashMap<>();

    static {
        Queue<UserJson> invitationSendQueue = new ConcurrentLinkedQueue<>();
        Queue<UserJson> invitationRecievedQueue = new ConcurrentLinkedQueue<>();
        Queue<UserJson> withFriendsQueue = new ConcurrentLinkedQueue<>();
        invitationSendQueue.add(user("duck", "12345", INVITATION_SENT));
        invitationSendQueue.add(user("bee", "12345", INVITATION_SENT));
        invitationRecievedQueue.add(user("barsik", "12345", INVITATION_RECEIVED));
        invitationRecievedQueue.add(user("sergey", "12345", INVITATION_RECEIVED));
        withFriendsQueue.add(user("stas", "12345", WITH_FRIENDS));
        withFriendsQueue.add(user("artem", "12345", WITH_FRIENDS));
        users.put(INVITATION_SENT, invitationSendQueue);
        users.put(INVITATION_RECEIVED, invitationRecievedQueue);
        users.put(WITH_FRIENDS, withFriendsQueue);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        List<Method> allMethods = new ArrayList<>();
        allMethods.add(context.getRequiredTestMethod());
        Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(BeforeEach.class))
                .forEach(allMethods::add);

        List<Parameter> parameters = allMethods.stream().map(m -> m.getParameters())
                .flatMap(Arrays::stream)
                .filter(parameter -> parameter.isAnnotationPresent(User.class))
                .filter(parameter -> parameter.getType().isAssignableFrom(UserJson.class))
                .toList();

        Map<User.UserType, UserJson> testCandidates = new HashMap<>();

        for (Parameter parameter : parameters) {
            User.UserType userType = parameter.getAnnotation(User.class).value();
            Queue<UserJson> queue = users.get(userType);
            UserJson testCandidate = null;
            while (testCandidate == null) {
                testCandidate = queue.poll();
            }
            testCandidates.put(userType, testCandidate);
        }
        context.getStore(NAMESPACE).put(context.getUniqueId(), testCandidates);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<User.UserType, UserJson> usersFromTest = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        for (User.UserType userType : usersFromTest.keySet()) {
            users.get(userType).add(usersFromTest.get(userType));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(UserJson.class) &&
                parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (UserJson) extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class)
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
