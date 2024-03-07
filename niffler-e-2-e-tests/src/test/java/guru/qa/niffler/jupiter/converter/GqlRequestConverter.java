package guru.qa.niffler.jupiter.converter;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

public class GqlRequestConverter implements ArgumentConverter {
  @Override
  public Object convert(Object o, ParameterContext parameterContext) throws ArgumentConversionException {
    return null;
  }
}
