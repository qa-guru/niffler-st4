package guru.qa.niffler.elements;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class ButtonElement {

    @Step("Click button by text {buttonText}")
    public void clickButtonByText(String buttonText){
        $(byText(buttonText))
                .click();
    }
}
