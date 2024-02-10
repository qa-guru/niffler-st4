package guru.qa.niffler.jupiter.extension;


import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECIEVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SEND;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;
import static guru.qa.niffler.model.UserJson.createUser;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.User.UserType;
import guru.qa.niffler.model.UserJson;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class UsersQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(UsersQueueExtension.class);

  private static Map<User.UserType, Queue<UserJson>> users = new ConcurrentHashMap<>();

  static {
    Queue<UserJson> friendsQueue = new ConcurrentLinkedQueue<>();
    friendsQueue.add(createUser("dima", "12345", "duck"));
    friendsQueue.add(createUser("duck", "12345", "dima"));

    Queue<UserJson> invitationSendedQueue = new ConcurrentLinkedQueue<>();
    invitationSendedQueue.add(
        createUser("bee", "12345", "barsik"));

    Queue<UserJson> invitationReceivedQueue = new ConcurrentLinkedQueue<>();
    invitationReceivedQueue.add(
        createUser("barsik", "12345", "bee"));
    invitationReceivedQueue.add(
        createUser("rabbit", "12345", "bee"));

    users.put(WITH_FRIENDS, friendsQueue);
    users.put(INVITATION_SEND, invitationSendedQueue);
    users.put(INVITATION_RECIEVED, invitationReceivedQueue);
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    List<Method> methods = new ArrayList<>();
    Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
        .filter(m -> m.isAnnotationPresent(BeforeEach.class))
        .forEach(methods::add);
    methods.add(context.getRequiredTestMethod());

    List<Parameter> parameters = methods.stream().map(m -> m.getParameters())
        .flatMap(Arrays::stream)
        .filter(parameter -> parameter.isAnnotationPresent(User.class))
        .filter(parameter -> parameter.getType().isAssignableFrom(UserJson.class))
        .toList();

    Map<User.UserType, UserJson> testUsers = new HashMap<>();

    for (Parameter parameter : parameters) {
      User annotation = parameter.getAnnotation(User.class);
      if (testUsers.containsKey(annotation.value())) {
        continue;
      }
      Queue<UserJson> queue = users.get(annotation.value());
      UserJson testCandidate = null;
      while (testCandidate == null) {
        testCandidate = queue.poll();
      }
      testUsers.put(annotation.value(), testCandidate);

    }
    context.getStore(NAMESPACE).put(context.getUniqueId(), testUsers);
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    Map<User.UserType, UserJson> usersFromTest = context.getStore(NAMESPACE)
        .get(context.getUniqueId(), Map.class);
    for (UserType userType : usersFromTest.keySet()) {
      users.get(userType).add(usersFromTest.get(userType));
    }

  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter()
        .getType()
        .isAssignableFrom(UserJson.class) &&
        parameterContext.getParameter().isAnnotationPresent(User.class);
  }

  @Override
  public UserJson resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return (UserJson) extensionContext.getStore(NAMESPACE)
        .get(extensionContext.getUniqueId(), Map.class)
        .get(parameterContext.findAnnotation(User.class).get().value());
  }

}
