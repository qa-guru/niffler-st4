package guru.qa.niffler.page_element;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ProfileInformationElement {
    public ProfileInformationElement verifyProfileName(String username) {
        profileName.shouldHave(text(username));
        return this;
    }

    private final SelenideElement
            container = $(".profile-content .main-content__section-avatar"),
            profileName = container.$("figcaption");
}
