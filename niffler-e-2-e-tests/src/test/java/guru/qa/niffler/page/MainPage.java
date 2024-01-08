package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.model.SpendJson;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    public MainPage selectSpend(SpendJson spend) {
        spendings()
                .find(text(spend.description()))
                .$$("td")
                .first()
                .scrollIntoView(true)
                .click();
        return this;
    }
    public MainPage pressDeleteSelected() {
        $(byText("Delete selected"))
                .click();
        return this;
    }

    public ElementsCollection spendings() {
        return $(".spendings-table tbody")
                .$$("tr");
    }

//    public ElementsCollection spendings() {
//        return $(".spendings-table tbody")
//                .$$("tr")
//                .find(text(spend.description()))
//                .$$("td");
//    }
}
