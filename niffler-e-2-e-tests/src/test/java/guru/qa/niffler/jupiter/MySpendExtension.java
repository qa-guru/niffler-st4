package guru.qa.niffler.jupiter;

import guru.qa.niffler.jupiter.annotation.MyGenerateSpend;
import guru.qa.niffler.jupiter.dto.CategoryDto;
import guru.qa.niffler.jupiter.dto.SpendDto;
import guru.qa.niffler.model.SpendJson;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class MySpendExtension implements BeforeEachCallback {

  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(MySpendExtension.class);

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    Optional<MyGenerateSpend> spend = AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        MyGenerateSpend.class
    );

    if (spend.isPresent()) {
      MyGenerateSpend myGenerateSpend = spend.get();

      CategoryDto createdCategory = createCategory(
          new CategoryDto(null,
              myGenerateSpend.username(),
              myGenerateSpend.category()));

      SpendDto spendCreated = createSpend(
          new SpendDto(null,
              myGenerateSpend.username(),
              myGenerateSpend.currency(),
              convertLocalDateToDate(LocalDate.now()),
              myGenerateSpend.amount(),
              myGenerateSpend.description(),
              createdCategory)
      );

      extensionContext.getStore(NAMESPACE)
          .put(extensionContext.getUniqueId(), spendDtoToJson(spendCreated));
    }
  }

  private Date convertLocalDateToDate(LocalDate localDate) {
    Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    return Date.from(instant);
  }

  private SpendJson spendDtoToJson(SpendDto spendDto) {
    return new SpendJson(spendDto.id(),
        spendDto.spendDate(),
        spendDto.category().category(),
        spendDto.currency(),
        spendDto.amount(),
        spendDto.description(),
        spendDto.username());
  }

  abstract SpendDto createSpend(SpendDto spendDto);

  abstract CategoryDto createCategory(CategoryDto categoryDto);


}
