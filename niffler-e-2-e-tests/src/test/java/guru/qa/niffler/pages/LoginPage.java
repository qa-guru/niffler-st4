package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Selenide.$;
import guru.qa.niffler.page.WelcomePage;

public class LoginPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement signInButton = $("button[type='submit']");

    @Step("В поле username ввести [{username}]")
    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("В поле password ввести [{password}]")
    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Нажать кнопку [Sign in]")
    public void clickSignInButton() {
        signInButton.click();
    }

    @Step("Залогиниться")
    public void login(String username, String password) {
        WelcomePage welcomePage = new WelcomePage();
        welcomePage.clickLoginButton();
        setUsername(username);
        setPassword(password);
        clickSignInButton();
    }
}