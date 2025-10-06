package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.*;

public class BaseTest {

    @BeforeAll
    static void setUpAll() {
        // Настройки Selenide с увеличенными таймаутами
        Configuration.browserSize = "1920x1080";
        Configuration.baseUrl = "https://demoqa.com";
        Configuration.timeout = 30000; // ← УВЕЛИЧИЛ до 30 секунд
        Configuration.pageLoadTimeout = 60000; // ← ДОБАВИЛ таймаут загрузки страницы
        Configuration.screenshots = true;
        Configuration.savePageSource = true;

        // Отключение некоторых опций для стабильности
        Configuration.remoteReadTimeout = 60000;
        Configuration.remoteConnectionTimeout = 60000;

        // Подключение Allure listener
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
        );
    }

    @BeforeEach
    void openPracticeForm() {
        // Открываем страницу с повторными попытками
        try {
            open("/automation-practice-form");
        } catch (Exception e) {
            System.out.println("⚠️ Первая попытка открытия страницы не удалась, пробуем снова...");
            // Повторная попытка
            open("/automation-practice-form");
        }
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }
}