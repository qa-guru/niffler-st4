package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.DatabaseSpendExtension;
import guru.qa.niffler.jupiter.extension.RestSpendExtension;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.jupiter.extension.SpendResolverExtension;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({DatabaseSpendExtension.class, SpendResolverExtension.class})
public @interface GenerateSpend {

  String username();

  String description();

  String category();

  double amount();

  CurrencyValues currency();
}
