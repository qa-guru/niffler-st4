package guru.qa.niffler.pages.message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SuccessMsg implements Msg {
  PROFILE_MSG("Profile successfully updated"),
  CATEGORY_MSG("New category added"),
  SPENDING_MSG("Spending successfully added"),
  INVITATION_SENT_MSG("Invitation is sent"),
  INVITATION_ACCEPTED_MSG("Invitation is accepted"),
  FRIEND_DELETED_MSG("Friend is deleted");

  private final String msg;

  @Override
  public String getMessage() {
    return msg;
  }
}
