package guru.qa.niffler.api.converter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import okhttp3.RequestBody;
import org.w3c.dom.Document;
import retrofit2.Converter;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JaxbRequestConverter<T> implements Converter<T, RequestBody> {

  private static final String namespace_prefix = "tns";
  private final JAXBContext context;
  private final String namespace;

  public JaxbRequestConverter(JAXBContext context, String namespace) {
    this.context = context;
    this.namespace = namespace;
  }

  @Nullable
  @Override
  public RequestBody convert(T value) throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      SOAPMessage message = MessageFactory.newInstance().createMessage();
      Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

      context.createMarshaller().marshal(value, document);
      message.getSOAPBody().addDocument(document);
      SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
      envelope.addNamespaceDeclaration(namespace_prefix, namespace);

      message.writeTo(bos);

      return RequestBody.create(JaxbConverterFactory.XML, bos.toByteArray());
    } catch (SOAPException | ParserConfigurationException | JAXBException e) {
      throw new RuntimeException(e);
    }
  }
}
