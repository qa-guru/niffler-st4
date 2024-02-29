package guru.qa.niffler.utils;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcConsoleInterceptor implements ClientInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(GrpcConsoleInterceptor.class);
  private static final JsonFormat.Printer JSON_PRINTER = JsonFormat.printer();

  @Override
  public <T, A> ClientCall<T, A> interceptCall(MethodDescriptor<T, A> method,
                                               CallOptions callOptions,
                                               Channel next) {

    return new ForwardingClientCall.SimpleForwardingClientCall<T, A>(
        next.newCall(method, callOptions.withoutWaitForReady())) {

      @Override
      public void sendMessage(T message) {
        LOGGER.info("Send gRPC request to "
            + next.authority()
            + trimGrpcMethodName(method.getFullMethodName()));
        try {
          LOGGER.info("Request body:\n "
              + JSON_PRINTER.print((MessageOrBuilder) message));
        } catch (InvalidProtocolBufferException e) {
          LOGGER.error("Unable to transform message -> json");
        }
        super.sendMessage(message);
      }

      @Override
      public void start(Listener<A> responseListener, Metadata headers) {
        final ClientCall.Listener<A> listener = new ForwardingClientCallListener<A>() {
          @Override
          protected Listener<A> delegate() {
            return responseListener;
          }

          @Override
          public void onClose(io.grpc.Status status, Metadata trailers) {
            super.onClose(status, trailers);
          }

          @Override
          public void onMessage(A message) {
            try {
              LOGGER.info("response body:\n "
                  + JSON_PRINTER.print((MessageOrBuilder) message));
            } catch (InvalidProtocolBufferException e) {
              LOGGER.warn("Can`t parse gRPC response", e);
            }
            super.onMessage(message);
          }
        };
        super.start(listener, headers);
      }

      private String trimGrpcMethodName(final String source) {
        return source.substring(source.lastIndexOf('/'));
      }
    };
  }
}
