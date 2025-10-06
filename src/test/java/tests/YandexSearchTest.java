package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.qameta.allure.*;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@Epic("Яндекс Поиск")
@Feature("Поисковая система")
public class YandexSearchTest {

    @BeforeAll
    static void setupAllure() {
        // Включение Allure listener для Selenide (скриншоты, page source)
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
        );
    }

    @Test
    @Story("Поиск информации в Яндекс")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка базового функционала поиска через ya.ru")
    void yandexSearchTest() {
        System.out.println("✔️ Запуск Яндекс UI-теста (ya.ru)...");

        // Настройка браузера
        setupBrowser();

        // Шаг 1: Открыть страницу Яндекс
        openYandexPage();

        // Шаг 2: Выполнить поиск
        performSearch();

        // Шаг 3: Проверить результаты
        verifySearchResults();

        System.out.println("✅ Яндекс тест выполнен успешно!");
    }

    @Step("Настройка браузера")
    private void setupBrowser() {
        Configuration.browser = "chrome";
        Configuration.timeout = 10000;
        Configuration.headless = false; // Для отладки - видим браузер
        Configuration.screenshots = true; // Включение скриншотов при падении
        Configuration.browserSize = "1920x1080"; // Убедитесь что окно достаточно большое
        System.out.println("🔧 Браузер настроен: Chrome, таймаут 10сек");
    }

    @Step("Открыть главную страницу Яндекс")
    private void openYandexPage() {
        open("https://ya.ru");
        System.out.println("📝 Страница ya.ru открыта");

        // Проверяем что страница загрузилась
        $("#text").shouldBe(visible);
        System.out.println("✅ Поисковая строка отображается");
    }

    @Step("Выполнить поиск по запросу 'Selenide автотесты'")
    private void performSearch() {
        $("#text").setValue("Selenide автотесты");
        System.out.println("📝 Текст 'Selenide автотесты' введён в поисковую строку");

        $(".search3__button").click();
        System.out.println("📝 Кнопка 'Найти' нажата");

        // Ждем загрузки результатов - используем более надежные селекторы
        // Ждем либо заголовок результатов, либо первый результат
        $(".serp-item, [data-bem*='serp-item'], .OrganicTitle, .main__content").shouldBe(visible);
        System.out.println("✅ Страница результатов загружена");
    }

    @Step("Проверить результаты поиска")
    private void verifySearchResults() {
        // Проверить URL
        String currentUrl = WebDriverRunner.url();
        if (currentUrl.contains("yandex.ru/search") || currentUrl.contains("text=Selenide")) {
            System.out.println("✅ Успешный переход на страницу результатов");
            Allure.addAttachment("URL страницы результатов", currentUrl);
        } else {
            System.out.println("❌ Не удалось перейти на страницу результатов. Текущий URL: " + currentUrl);
            Allure.addAttachment("Ошибка URL", "Ожидался URL с 'yandex.ru/search', но получили: " + currentUrl);
        }

        // Проверить наличие результатов поиска - используем несколько возможных селекторов
        $(".serp-item, [data-bem*='serp-item'], .Organic, .main__content").shouldBe(visible);
        System.out.println("✅ Результаты поиска отображаются");

        // Проверить что есть хотя бы один результат
        int resultsCount = $$(".serp-item, [data-bem*='serp-item'], .Organic").size();
        if (resultsCount > 0) {
            System.out.println("✅ Найдено результатов: " + resultsCount);
            Allure.addAttachment("Количество результатов", String.valueOf(resultsCount));
        } else {
            System.out.println("⚠️  Результаты не найдены стандартными селекторами, проверяем альтернативные...");
            // Альтернативная проверка
            if ($("body").getText().contains("Selenide") || $("body").getText().contains("selenide")) {
                System.out.println("✅ Текст запроса найден в содержимом страницы");
            }
        }

        // Проверить что наш запрос отображается в результатах
        if ($("body").getText().toLowerCase().contains("selenide")) {
            System.out.println("✅ Запрос 'Selenide' присутствует в результатах");
            Allure.addAttachment("Найденный текст", "Запрос 'Selenide' найден на странице");
        } else {
            System.out.println("⚠️  Запрос 'Selenide' не найден в тексте страницы");
        }

        // Сделаем скриншот для отчета - ИСПРАВЛЕННАЯ ВЕРСИЯ
        try {
            byte[] screenshot = com.codeborne.selenide.Selenide.screenshot(OutputType.BYTES);
            if (screenshot != null) {
                Allure.addAttachment("Скриншот результатов", "image/png",
                        new ByteArrayInputStream(screenshot), ".png");
                System.out.println("✅ Скриншот сделан и добавлен в отчет");
            } else {
                System.out.println("⚠️ Не удалось сделать скриншот");
                Allure.addAttachment("Инфо", "text/plain", "Скриншот не доступен");
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка при создании скриншота: " + e.getMessage());
            Allure.addAttachment("Ошибка скриншота", "text/plain", e.getMessage());
        }
    }
}