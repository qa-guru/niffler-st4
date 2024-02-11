package guru.qa.niffler.page_element;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.$;

public class StatsElement {
    private final SelenideElement statisticsSection = $(".main-content__section-stats");

    public StatsElement statisticsSectionShouldExist() {
        statisticsSection.should(appear);
        return this;
    }
}
