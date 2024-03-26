package guru.qa.niffler.config;

import java.util.List;

public interface Config {

  static Config getInstance() {
    return "docker".equals(System.getProperty("test.env"))
        ? DockerConfig.instance
        : LocalConfig.instance;
  }

  String frontUrl();

  String authUrl();

  String spendUrl();

  String userdataUrl();

  String gatewayUrl();

  String jdbcHost();

  String currencyGrpcHost();

  String kafkaAddress();

  default String jdbcUser() {
    return "postgres";
  }

  default String jdbcPassword() {
    return "secret";
  }

  default int jdbcPort() {
    return 5432;
  }

  default int currencyGrpcPort() {
    return 8092;
  }

  default List<String> kafkaTopics() {
    return List.of("users");
  }
}
