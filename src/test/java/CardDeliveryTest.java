import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.Keys.BACK_SPACE;
import static org.openqa.selenium.Keys.DELETE;

public class CardDeliveryTest {
    String getLocalDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy",
                new Locale("ru")));
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
//        Configuration.holdBrowserOpen = true;
    }

    @Test
    void shouldWalkTheHappyPath() {
        String deliveryDate = getLocalDate(3);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        // $(By.className("notification__title")).shouldHave(Condition.exactText("Успешно!"), Duration.ofSeconds(40));
        // Будет использована строка ниже, потому что окно исчезает:
        $x("//*[contains(text(),'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").shouldBe(visible, Duration.ofSeconds(15)).
                should(exactText("Встреча успешно забронирована на " + deliveryDate));
    }

    @Test
    void shouldWalkTheHappyPathWhenTheCityContainsYoAndItUsed() {
        String deliveryDate = getLocalDate(4);
        $("[data-test-id='city'] input").setValue("Орёл");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $x("//*[contains(text(),'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").shouldBe(visible, Duration.ofSeconds(15)).
                should(exactText("Встреча успешно забронирована на " + deliveryDate));
    }

    @Test
    void shouldWalkTheHappyPathWhenTheCityContainsHyphen() {
        String deliveryDate = getLocalDate(4);
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $x("//*[contains(text(),'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").shouldBe(visible, Duration.ofSeconds(15)).
                should(exactText("Встреча успешно забронирована на " + deliveryDate));
    }

    @Test
    void shouldWalkTheHappyPathWhenTheCityContainsSpace() {
        String deliveryDate = getLocalDate(5);
        $("[data-test-id='city'] input").setValue("Нижний Новгород");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $x("//*[contains(text(),'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").shouldBe(visible, Duration.ofSeconds(15)).
                should(exactText("Встреча успешно забронирована на " + deliveryDate));
    }

    @Test
    void shouldWalkTheHappyPathWhenTheNameContainsYoAndItUsed() {
        String deliveryDate = getLocalDate(6);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Пётр Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $x("//*[contains(text(),'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").shouldBe(visible, Duration.ofSeconds(15)).
                should(exactText("Встреча успешно забронирована на " + deliveryDate));
    }

    @Test
    void shouldShowErrorMessageWhenTheCityIsInEnglish() {
        String deliveryDate = getLocalDate(7);
        $("[data-test-id='city'] input").setValue("city");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=city]").should(exist);
        $x("//*[contains(text(),'Доставка в выбранный город недоступна')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenTheCityIsOutOfList() {
        String deliveryDate = getLocalDate(8);
        $("[data-test-id='city'] input").setValue("Сосновый Бор");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=city]").should(exist);
        $x("//*[contains(text(),'Доставка в выбранный город недоступна')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenTheCityContainsYoButYeUsed() {
        String deliveryDate = getLocalDate(9);
        $("[data-test-id='city'] input").setValue("Орел");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=city]").should(exist);
        $x("//*[contains(text(),'Доставка в выбранный город недоступна')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenTheCityContainsNotValidSpecialSymbols() {
        String deliveryDate = getLocalDate(10);
        $("[data-test-id='city'] input").setValue("Санкт/Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=city]").should(exist);
        $x("//*[contains(text(),'Доставка в выбранный город недоступна')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenTheCityContainsFigures() {
        String deliveryDate = getLocalDate(11);
        $("[data-test-id='city'] input").setValue("Чита1");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=city]").should(exist);
        $x("//*[contains(text(),'Доставка в выбранный город недоступна')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenTheNameIsInEnglish() {
        String deliveryDate = getLocalDate(12);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Name");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=name]").should(exist);
        $x("//*[contains(text(),'Имя и Фамилия указаные неверно. Допустимы только русские буквы, " +
                "пробелы и дефисы.')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenTheNameContainsNotValidSpecialSymbols() {
        String deliveryDate = getLocalDate(13);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый/Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=name]").should(exist);
        $x("//*[contains(text(),'Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы " +
                "и дефисы.')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenTheNameContainsFigures() {
        String deliveryDate = getLocalDate(14);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий 1-2");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=name]").should(exist);
        $x("//*[contains(text(),'Имя и Фамилия указаные неверно. Допустимы только русские буквы, " +
                "пробелы и дефисы.')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenThePhoneNumberWithoutPlus() {
        String deliveryDate = getLocalDate(15);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=phone]").should(exist);
        $x("//*[contains(text(),'Телефон указан неверно. Должно быть 11 цифр, например, " +
                "+79012345678.')]").shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenThePhoneNumberContainsLessThanElevenFigures() {
        String deliveryDate = getLocalDate(16);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+7123456789");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=phone]").should(exist);
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenThePhoneNumberContainsMoreThanElevenFigures() {
        String deliveryDate = getLocalDate(17);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+712345678901");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=phone]").should(exist);
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenThePhoneNumberContainsSpecialSymbols() {
        String deliveryDate = getLocalDate(18);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+7(123)456-78-90");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=phone]").should(exist);
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenThePhoneNumberContainsOneFigure() {
        String deliveryDate = getLocalDate(19);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+71");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=phone]").should(exist);
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenThePhoneNumberContainsLetters() {
        String deliveryDate = getLocalDate(20);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("Letter");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=phone]").should(exist);
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenTheCheckboxIsEmpty() {
        String deliveryDate = getLocalDate(21);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=agreement]").should(exist);
    }

    @Test
    void shouldShowErrorMessageWhenAllFieldsAreEmpty() {
        String deliveryDate = getLocalDate(22);
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=city]").should(exist);
        $x("//*[contains(text(),'Поле обязательно для заполнения')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenCityFieldIsEmpty() {
        String deliveryDate = getLocalDate(23);
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='date'] input").setValue("01.09.2022");
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=city]").should(exist);
        $x("//*[contains(text(),'Поле обязательно для заполнения')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }


    @Test
    void shouldShowErrorMessageWhenDateFieldIsEmpty() {
        String deliveryDate = getLocalDate(24);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $("[data-test-id=date] .input_invalid").should(exist);
        $x("//*[contains(text(),'Неверно введена дата')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenNameFieldIsEmpty() {
        String deliveryDate = getLocalDate(25);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=name]").should(exist);
        $x("//*[contains(text(),'Поле обязательно для заполнения')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorMessageWhenPhoneNumberFieldIsEmpty() {
        String deliveryDate = getLocalDate(26);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $(".input_invalid[data-test-id=phone]").should(exist);
        $x("//*[contains(text(),'Поле обязательно для заполнения')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldNotAcceptCurrentDate() {
        String deliveryDate = getLocalDate(0);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $("[data-test-id=date] .input_invalid").should(exist);
        $x("//*[contains(text(),'Заказ на выбранную дату невозможен')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldNotAcceptTheNextDay() {
        String deliveryDate = getLocalDate(1);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $("[data-test-id=date] .input_invalid").should(exist);
        $x("//*[contains(text(),'Заказ на выбранную дату невозможен')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldNotAcceptTheDayAfterNext() {
        String deliveryDate = getLocalDate(2);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $("[data-test-id=date] .input_invalid").should(exist);
        $x("//*[contains(text(),'Заказ на выбранную дату невозможен')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldNotAcceptTheDateBeforeCurrentDate() {
        String deliveryDate = getLocalDate(-1);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, deliveryDate);
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $("[data-test-id=date] .input_invalid").should(exist);
        $x("//*[contains(text(),'Заказ на выбранную дату невозможен')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }

    @Test
    void shouldNotAcceptTheDateLongBeforeCurrentDate() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(DELETE, "01.01.2001");
        $("[data-test-id='name'] input").setValue("Василий Первый-Второй");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $(By.className("button__text")).click();
        $("[data-test-id=date] .input_invalid").should(exist);
        $x("//*[contains(text(),'Заказ на выбранную дату невозможен')]").shouldBe(visible,
                Duration.ofSeconds(15));
    }
}
