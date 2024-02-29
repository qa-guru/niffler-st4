package guru.qa.niffler.test.grpc;

import guru.qa.grpc.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.GrpcTest;
import guru.qa.niffler.utils.GrpcConsoleInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

@GrpcTest
public abstract class BaseGrpcTest {

  protected static final Config CFG = Config.getInstance();

  protected static Channel channel;
  protected static NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub blockingStub;
  protected static NifflerCurrencyServiceGrpc.NifflerCurrencyServiceStub stub;

  static {
    channel = ManagedChannelBuilder.forAddress(CFG.currencyGrpcHost(), CFG.currencyGrpcPort())
        .intercept(new AllureGrpc(), new GrpcConsoleInterceptor())
        .usePlaintext()
        .build();

    blockingStub = NifflerCurrencyServiceGrpc.newBlockingStub(channel);
    stub = NifflerCurrencyServiceGrpc.newStub(channel);
  }
}
