package guru.qa.niffler.api;

import guru.qa.niffler.model.gql.GqlRequest;
import guru.qa.niffler.model.gql.GqlUser;

public class GatewayGqlApiClient extends RestClient {

  private final GraphQlGatewayApi graphQlGatewayApi;

  public GatewayGqlApiClient() {
    super(
        CFG.gatewayUrl()
    );
    graphQlGatewayApi = retrofit.create(GraphQlGatewayApi.class);
  }

  public GqlUser currentUser(String bearerToken, GqlRequest request) throws Exception {
    return graphQlGatewayApi.currentUser(bearerToken, request).execute()
        .body();
  }
}
