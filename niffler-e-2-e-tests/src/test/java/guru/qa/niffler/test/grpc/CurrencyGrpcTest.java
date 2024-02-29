package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.grpc.niffler.grpc.CalculateRequest;
import guru.qa.grpc.niffler.grpc.CalculateResponse;
import guru.qa.grpc.niffler.grpc.Currency;
import guru.qa.grpc.niffler.grpc.CurrencyResponse;
import guru.qa.grpc.niffler.grpc.CurrencyValues;
import io.grpc.stub.StreamObserver;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class CurrencyGrpcTest extends BaseGrpcTest {

  private static final Empty empty = Empty.getDefaultInstance();

  @Test
  void allCurrenciesShouldBeReturnedFromNifflerCurrency() {
    final CurrencyResponse response = blockingStub.getAllCurrencies(empty);
    Allure.step("Check currencies size", () -> {
      Assertions.assertEquals(
          4,
          response.getAllCurrenciesList().size()
      );
    });
    final List<Currency> currenciesList = response.getAllCurrenciesList();
    Currency rubCurrency = currenciesList.get(0);
    Allure.step("Check currencies name", () -> {
      Assertions.assertEquals(
          CurrencyValues.RUB,
          rubCurrency.getCurrency()
      );
    });
  }


  @CsvSource(value = {
      "USD, RUB, 100.0, 6666.67",
      "USD, USD, 100.0, 100.0"
  })
  @ParameterizedTest
  void calculateShouldReturnCorrectResponsesForAllCurrencies(CurrencyValues spendCurrency,
                                                             CurrencyValues desiredCurrency,
                                                             double amount,
                                                             double expectedAmount) {
    final CalculateRequest calculateRequest = CalculateRequest.newBuilder()
        .setSpendCurrency(spendCurrency)
        .setDesiredCurrency(desiredCurrency)
        .setAmount(amount)
        .build();

    final CalculateResponse calculateResponse = blockingStub.calculateRate(calculateRequest);

    Allure.step("Check calculated amount", () -> {
      Assertions.assertEquals(
          expectedAmount,
          calculateResponse.getCalculatedAmount()
      );
    });
  }

  @Test
  void calculateRatesTest() throws InterruptedException {
    Queue<CalculateResponse> responses = new ConcurrentLinkedQueue<>();
    Queue<CalculateRequest> requests = new ConcurrentLinkedQueue<>(List.of(
        CalculateRequest.newBuilder()
            .setSpendCurrency(CurrencyValues.USD)
            .setDesiredCurrency(CurrencyValues.RUB)
            .setAmount(100.0)
            .build(),
        CalculateRequest.newBuilder()
            .setSpendCurrency(CurrencyValues.USD)
            .setDesiredCurrency(CurrencyValues.USD)
            .setAmount(100.0)
            .build()
    ));

    AtomicReference<Throwable> exceptionHolder = new AtomicReference<>();

    StreamObserver<CalculateResponse> responseObserver = new StreamObserver<CalculateResponse>() {
      @Override
      public void onNext(CalculateResponse calculateResponse) {
        responses.add(calculateResponse);
      }

      @Override
      public void onError(Throwable throwable) {
        exceptionHolder.set(throwable);
      }

      @Override
      public void onCompleted() {
      }
    };

    StreamObserver<CalculateRequest> reqObserver = stub.withDeadlineAfter(10, TimeUnit.SECONDS)
        .calculateRates(responseObserver);

    while (!requests.isEmpty()) {
      reqObserver.onNext(requests.poll());
      Thread.sleep(1000);
    }
    reqObserver.onCompleted();

    Assertions.assertNull(exceptionHolder.get());
    Assertions.assertEquals(
        2,
        responses.size()
    );
  }
}
