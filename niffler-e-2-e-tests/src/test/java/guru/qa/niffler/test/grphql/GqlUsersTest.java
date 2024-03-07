package guru.qa.niffler.test.grphql;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GqlRequestFile;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.gql.GqlUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GqlUsersTest extends BaseGraphQLTest {

  @Test
  @ApiLogin(
      user = @TestUser
  )
  void currentUserShouldBeReturned(@User UserJson testUser,
                                   @Token String bearerToken,
                                   @GqlRequestFile("gql/currentUserQuery.json") guru.qa.niffler.model.gql.GqlRequest request) throws Exception {

    final GqlUser response = gatewayGqlApiClient.currentUser(bearerToken, request);
    Assertions.assertEquals(
        testUser.username(),
        response.getData().getUser().getUsername()
    );
  }

}
