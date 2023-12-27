package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.ui.pages.LoginPage;
import guru.qa.niffler.ui.pages.MainPage;
import guru.qa.niffler.ui.pages.WelcomePage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    public MainPage mainPage = new MainPage();
    public LoginPage loginPage = new LoginPage();
    public WelcomePage welcomePage = new WelcomePage();

    @BeforeAll
    static void beforeAll() {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage
                .clickLoginButton()
                .authorize("duck", "12345");
    }
}
