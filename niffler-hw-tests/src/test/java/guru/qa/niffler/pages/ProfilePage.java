package guru.qa.niffler.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.CurrencyValues;
import io.qameta.allure.Step;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage extends BasePage {

    private SelenideElement nameField = $("input[name='firstname']");
    private SelenideElement surnameField = $("input[name='surname']");
    private SelenideElement currencyField = $x("//input[contains(@id, 'react-select')]");
    private ElementsCollection currencyValues = $$x("//div[@aria-disabled='false' and contains(@id, 'react-select')]");
    private SelenideElement submitButton = $("button[type='submit']");

    private SelenideElement newCategoryField = $("input[name='category']");
    private SelenideElement createCategoryButton = $(byText("Create"));

    private ElementsCollection categories = $$(".categories__item");

    @Step("Set profile info {name} / {surname} / {currency}")
    public void setProfileInfo(String name, String surname, CurrencyValues currency) {
        nameField.setValue(name);
        surnameField.setValue(surname);

        currencyField.scrollTo();
        currencyField.click();
        currencyValues.findBy(Condition.text(currency.name())).click();

        submitButton.scrollTo();
        submitButton.click();
    }

    @Step("Create category '{category}'")
    public void createCategory(String category) {
        newCategoryField.setValue(category);
        createCategoryButton.click();
    }

    @Step("Get categories")
    public List<String> getCategories() {
        return categories.stream().map(x -> x.text()).collect(Collectors.toList());
    }


}
