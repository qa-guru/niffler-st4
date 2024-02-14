package guru.qa.niffler.pages.message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SuccessMsg implements Msg {
  PROFILE_MSG("Profile successfully updated"),
  CATEGORY_MSG("New category added"),
  SPENDING_ADDED_MSG("Spending successfully added"),
  SPENDING_DELETED_MSG("Spendings deleted"),
  INVITATION_SENT_MSG("Invitation is sent"),
  INVITATION_ACCEPTED_MSG("Invitation is accepted"),
  INVITATION_DECLINED_MSG("Invitation is declined"),
  FRIEND_DELETED_MSG("Friend is deleted");

  private final String msg;

  @Override
  public String getMessage() {
    return msg;
  }
}
