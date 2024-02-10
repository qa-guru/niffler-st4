package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.DatabaseSpendExtension;
import guru.qa.niffler.jupiter.extension.SpendResolverExtension;
import guru.qa.niffler.model.CurrencyValues;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({DatabaseSpendExtension.class, SpendResolverExtension.class})
public @interface MyGenerateSpend {

  String username();

  String description();

  String category();

  double amount();

  CurrencyValues currency();
}
