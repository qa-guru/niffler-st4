package guru.qa.niffler.model.gql;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class GqlResponse<T extends GqlResponse> {
  protected T data;
  protected List<GqlError> errors;
  @JsonProperty("__typename")
  protected String typename;
}
