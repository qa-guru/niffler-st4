package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

@ExtendWith(UsersQueueExtension.class)
public class WithFriendsTest extends BaseWebTest {

    @BeforeEach()
    void openFriendsPage(@User(WITH_FRIENDS) UserJson user) {
        welcomePage
                .open()
                .clickLoginButton()
                .authorize(user.username(), user.testData().password())
                .clickFriendsButton();
    }

    @Test
    @DisplayName("Проверка отображения текста You are friends в таблице друзей")
    void textYouAareFriends() {
        friendsPage
                .checkFriendsTextVisible();
    }

    @Test
    @DisplayName("Проверка отображения кнопки Remove friend в таблице друзей")
    void removeFriendButtonVisible() {
        friendsPage
                .checkRemoveFriendButtonVisible();
    }

    @Test
    @DisplayName("Проверка отображения аватар друга в таблице друзей")
    void avatarVisible() {
        friendsPage
                .checkAvatarVisible();
    }
}
