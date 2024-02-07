package guru.qa.niffler.jupiter;

import guru.qa.niffler.model.SpendJsonModel;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class SpendResolverExtension implements ParameterResolver {

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter()
        .getType()
        .isAssignableFrom(SpendJsonModel.class);
  }

  @Override
  public SpendJsonModel resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(SpendExtension.NAMESPACE)
        .get("spend", SpendJsonModel.class);
  }
}
