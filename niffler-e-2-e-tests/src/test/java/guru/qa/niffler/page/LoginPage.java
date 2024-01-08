package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class LoginPage {
    public MainPage doLogin(String username, String password) {
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(username);
        $("input[name='password']").setValue(password);
        $("button[type='submit']").click();
        return page(MainPage.class);
    }
}
