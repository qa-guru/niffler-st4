package guru.qa.niffler.jupiter;

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

import static guru.qa.niffler.jupiter.User.UserType.*;

public class UsersQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE
          = ExtensionContext.Namespace.create(UsersQueueExtension.class);

  private static Map<User.UserType, Queue<UserJson>> USERS = new ConcurrentHashMap<>();

  static {
    Queue<UserJson> withFriendsQueue = new ConcurrentLinkedQueue<>();
    Queue<UserJson> invitationSendQueue = new ConcurrentLinkedQueue<>();
    Queue<UserJson> invitationReceivedQueue = new ConcurrentLinkedQueue<>();

    withFriendsQueue.add(user("dima", "12345", WITH_FRIENDS));
    withFriendsQueue.add(user("duck", "duck", WITH_FRIENDS));
    invitationSendQueue.add(user("bee", "12345", INVITATION_SEND));
    invitationReceivedQueue.add(user("barsik", "12345", INVITATION_RECEIVED));

    USERS.put(WITH_FRIENDS, withFriendsQueue);
    USERS.put(INVITATION_SEND, invitationSendQueue);
    USERS.put(INVITATION_RECEIVED, invitationReceivedQueue);
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    List<Parameter> parameters = getParameters(context);

    Map<User.UserType, UserJson> testCandidates = new HashMap<>();
    for (Parameter parameter : parameters) {
      User.UserType userType = parameter.getAnnotation(User.class).value();
      if(testCandidates.containsKey(userType)) {
        continue;
      }

      UserJson testCandidate = null;
      Queue<UserJson> queue = USERS.get(userType);
      while (testCandidate == null) {
        testCandidate = queue.poll();
      }

      testCandidates.put(userType, testCandidate);
    }

    context.getStore(NAMESPACE).put(context.getUniqueId(), testCandidates);
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    Map<User.UserType, UserJson> usersFromTest = context.getStore(NAMESPACE)
            .get(context.getUniqueId(), Map.class);

    for (User.UserType userType : usersFromTest.keySet()) {
      USERS.get(userType).add(usersFromTest.get(userType));
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

  private static List<Parameter> getParameters(ExtensionContext context) {
    List<Method> methods = new ArrayList<>();
    methods.add(context.getRequiredTestMethod());
    Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
            .filter(m -> m.isAnnotationPresent(BeforeEach.class))
            .forEach(methods::add);

    List<Parameter> parameters = methods.stream()
            .map(Executable::getParameters)
            .flatMap(Arrays::stream)
            .filter(parameter -> parameter.isAnnotationPresent(User.class))
            .filter(parameter -> parameter.getType().isAssignableFrom(UserJson.class))
            .toList();

    return parameters;
  }
}
