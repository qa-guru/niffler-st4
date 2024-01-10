package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.FriendsPage;
import guru.qa.niffler.pages.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.WITH_FRIENDS;

@ExtendWith(UsersQueueExtension.class)
public class WithFriendsTest {

    @BeforeEach()
    void openFriendsPage(@User(WITH_FRIENDS) UserJson user) {
        new WelcomePage().open().clickLoginButton()
                .authorize(user.username(), user.testData().password())
                .clickFriendsButton();
    }

    @Test
    @DisplayName("Проверка отображения текста You are friends в таблице друзей")
    void textYouAareFriends() {
        new FriendsPage().checkFriendsTextVisible();
    }

    @Test
    @DisplayName("Проверка отображения кнопки Remove friend в таблице друзей")
    void removeFriendButtonVisible() {
        new FriendsPage().checkRemoveFriendButtonVisible();
    }

    @Test
    @DisplayName("Проверка отображения аватар друга в таблице друзей")
    void avatarVisible() {
        new FriendsPage().checkAvatarVisible();
    }
}
