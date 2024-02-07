package guru.qa.niffler.pages.message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorMsg implements Msg {
  CATEGORY_MSG("Can not add new category");

  private final String msg;

  @Override
  public String getMessage() {
    return msg;
  }
}
