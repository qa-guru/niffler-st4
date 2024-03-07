package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.jupiter.annotation.GqlRequestFile;
import guru.qa.niffler.model.gql.GqlRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.io.IOException;
import java.io.InputStream;

public class GqlRequestResolver implements ParameterResolver {

  private static final ObjectMapper om = new ObjectMapper();
  private final ClassLoader cl = GqlRequestResolver.class.getClassLoader();

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return AnnotationSupport.isAnnotated(parameterContext.getParameter(), GqlRequestFile.class)
        && parameterContext.getParameter().getType().isAssignableFrom(GqlRequest.class);
  }

  @Override
  public GqlRequest resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    GqlRequestFile annotation = AnnotationSupport.findAnnotation(parameterContext.getParameter(), GqlRequestFile.class).get();
    try (InputStream is = cl.getResourceAsStream(annotation.value())) {
      return om.readValue(is.readAllBytes(), GqlRequest.class);
    } catch (IOException e) {
      throw new ParameterResolutionException(e.getMessage());
    }
  }
}
