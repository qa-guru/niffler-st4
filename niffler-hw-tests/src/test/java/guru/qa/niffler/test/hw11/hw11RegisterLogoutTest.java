package guru.qa.niffler.test.hw11;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class hw11RegisterLogoutTest extends BaseWebTest {

    private final static String tester1 = "tester1";

//    @Test
//    void registerUser() {
//
//        String username = new Faker().name().username();
//
//        Selenide.open("http://127.0.0.1:3000/main");
//        welcomePage.clickRegisterLink();
//        registerPage.register(username, "12345");
//
//        Selenide.open("http://127.0.0.1:3000/main");
//        welcomePage.clickLoginLink();
//        loginPage.login(username, "12345");
//
//        assertThat(topMenu.logoutIsDisplayed()).isTrue();
//
//    }

    @Test
    void logout() {

        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
        loginPage.login(tester1, "12345");

        topMenu.clickLogoutButton();

        welcomePage.loginLinkShouldBeDisplayed();
    }

}
