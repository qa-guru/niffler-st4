package guru.qa.niffler.api.converter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlRootElement;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class JaxbConverterFactory extends Converter.Factory {

  static final MediaType XML = MediaType.get("application/xml; charset=utf-8");
  private final @Nonnull String targetNameSpace;

  public JaxbConverterFactory(@Nonnull String targetNameSpace) {
    this.targetNameSpace = targetNameSpace;
  }

  @Nullable
  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
    if (type instanceof Class<?> cls && cls.isAnnotationPresent(XmlRootElement.class)) {
      return new JaxbResponseConverter<>(context(cls), cls);
    }
    return null;
  }

  @Nullable
  @Override
  public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    if (type instanceof Class<?> cls && cls.isAnnotationPresent(XmlRootElement.class)) {
      return new JaxbRequestConverter<>(context(cls), targetNameSpace);
    }
    return null;
  }

  private JAXBContext context(Class<?> type) {
    try {
      return JAXBContext.newInstance(type);
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }
}
