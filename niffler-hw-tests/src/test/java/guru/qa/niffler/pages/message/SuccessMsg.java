package guru.qa.niffler.pages.message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SuccessMsg implements Msg {
  PROFILE_MSG("Profile successfully updated");

  private final String msg;

  @Override
  public String getMessage() {
    return msg;
  }
}
