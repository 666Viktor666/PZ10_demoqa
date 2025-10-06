package tests;

import com.github.javafaker.Faker;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import pages.PracticeFormPage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@Epic("DemoQA Automation Practice Form")
@Feature("Заполнение формы регистрации")
public class PracticeFormTest extends BaseTest {

    private Faker faker = new Faker();
    private PracticeFormPage formPage = new PracticeFormPage();

    @Test
    @DisplayName("Полное заполнение формы со всеми полями")
    @Description("Этот тест проверяет заполнение ВСЕХ полей формы регистрации: личная информация, контакты, Subjects, файл, хобби, штат, город и дата рождения")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Пользователь может успешно заполнить все поля формы включая выбор даты из календаря")
    void shouldFillCompleteFormWithAllFields() {
        // Генерация тестовых данных
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String gender = "male";
        String mobile = faker.phoneNumber().subscriberNumber(10);
        String address = faker.address().fullAddress();
        String fileName = "test-image.jpg";
        String hobby = "Sports";
        String state = "Uttar Pradesh";
        String city = "Agra";
        String[] subjects = {"Maths", "Physics", "Computer Science"};

        // ДОБАВЛЕНО данные для даты рождения
        int birthDay = 15;
        String birthMonth = "June";
        int birthYear = 1995;
        String expectedDate = "15 June,1995"; // Формат, который отображается в форме

        Allure.addAttachment("Тестовые данные", "text/plain",
                "First Name: " + firstName + "\n" +
                        "Last Name: " + lastName + "\n" +
                        "Email: " + email + "\n" +
                        "Mobile: " + mobile + "\n" +
                        "Address: " + address + "\n" +
                        "File: " + fileName + "\n" +
                        "Hobby: " + hobby + "\n" +
                        "State: " + state + "\n" +
                        "City: " + city + "\n" +
                        "Subjects: " + String.join(", ", subjects) + "\n" +
                        "Date of Birth: " + birthDay + " " + birthMonth + " " + birthYear);

        // Заполнение формы с шагами
        openFormPage();
        fillPersonalInfo(firstName, lastName, email, gender);
        fillContactInfo(mobile, address);
        fillSubjects(subjects);
        selectDateOfBirth(birthDay, birthMonth, birthYear); // ← ДОБАВЛЕН шаг выбора даты
        uploadFile(fileName);
        selectHobby(hobby);
        selectStateAndCity(state, city);
        submitForm();
        verifyCompleteSuccessModal(firstName, lastName, email, gender, mobile, address, fileName, hobby, state, city, subjects, expectedDate);
        closeModal();
    }

    @Step("Открыть страницу с формой регистрации")
    private void openFormPage() {
        $(".practice-form-wrapper").shouldBe(visible);
        takeScreenshot("Форма загружена");
        System.out.println("✅ Страница с формой загружена");
    }

    @Step("Заполнить личную информацию: {firstName} {lastName}, {email}, пол: {gender}")
    private void fillPersonalInfo(String firstName, String lastName, String email, String gender) {
        formPage.setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .selectGender(gender);

        takeScreenshot("Личная информация заполнена");
        System.out.println("✅ Личная информация заполнена");
    }

    @Step("Заполнить контактную информацию: телефон {mobile}, адрес {address}")
    private void fillContactInfo(String mobile, String address) {
        formPage.setMobileNumber(mobile)
                .setCurrentAddress(address);

        takeScreenshot("Контактная информация заполнена");
        System.out.println("✅ Контактная информация заполнена");
    }

    @Step("Заполнить Subjects: {subjects}")
    private void fillSubjects(String[] subjects) {
        formPage.setSubjects(subjects);
        takeScreenshot("Subjects заполнены");
        System.out.println("✅ Subjects '" + String.join(", ", subjects) + "' заполнены");

        // Проверяем, что subjects действительно добавлены
        for (String subject : subjects) {
            $x("//div[contains(@class, 'subjects-auto-complete__multi-value')]//div[text()='" + subject + "']")
                    .shouldBe(visible);
        }
    }

    // ДОБАВЛЕН шаг для выбора даты рождения
    @Step("Выбрать дату рождения: {day} {month} {year}")
    private void selectDateOfBirth(int day, String month, int year) {
        formPage.selectDateFromCalendar(day, month, year);
        takeScreenshot("Дата рождения выбрана");

        // Проверяем, что дата установилась корректно
        String selectedDate = formPage.getSelectedDate();
        System.out.println("✅ Дата рождения выбрана: " + selectedDate);

        Allure.addAttachment("Выбранная дата", "text/plain",
                "День: " + day + "\n" +
                        "Месяц: " + month + "\n" +
                        "Год: " + year + "\n" +
                        "В поле: " + selectedDate);
    }

    @Step("Загрузить файл: {fileName}")
    private void uploadFile(String fileName) {
        try {
            java.io.InputStream fileStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (fileStream == null) {
                throw new RuntimeException("Файл " + fileName + " не найден в classpath. Поместите файл в src/test/resources/");
            }
            fileStream.close();

            formPage.uploadFile(fileName);
            takeScreenshot("Файл загружен");
            System.out.println("✅ Файл " + fileName + " успешно загружен");

            Allure.addAttachment("Загруженный файл", "text/plain",
                    "Имя файла: " + fileName + "\n" +
                            "Размер: " + getFileSizeFromClasspath(fileName) + " байт");

        } catch (Exception e) {
            System.out.println("❌ Ошибка при загрузке файла: " + e.getMessage());
            throw new RuntimeException("Не удалось загрузить файл: " + fileName, e);
        }
    }

    @Step("Выбрать хобби: {hobby}")
    private void selectHobby(String hobby) {
        formPage.selectHobby(hobby);
        takeScreenshot("Хобби выбрано: " + hobby);
        System.out.println("✅ Хобби '" + hobby + "' выбрано");

        switch (hobby.toLowerCase()) {
            case "sports":
                $("input[id='hobbies-checkbox-1']").shouldBe(checked);
                break;
            case "reading":
                $("input[id='hobbies-checkbox-2']").shouldBe(checked);
                break;
            case "music":
                $("input[id='hobbies-checkbox-3']").shouldBe(checked);
                break;
        }
    }

    @Step("Выбрать штат и город: {state} -> {city}")
    private void selectStateAndCity(String state, String city) {
        formPage.selectStateAndCity(state, city);
        takeScreenshot("Штат и город выбраны");
        System.out.println("✅ Штат '" + state + "' и город '" + city + "' выбраны");

        $("#state").shouldHave(text(state));
        $("#city").shouldHave(text(city));
    }

    @Step("Отправить форму")
    private void submitForm() {
        formPage.submitForm();
        takeScreenshot("Форма отправлена");
        System.out.println("✅ Форма отправлена");
    }

    @Step("Проверить полное модальное окно со всеми данными: {firstName} {lastName}")
    private void verifyCompleteSuccessModal(String firstName, String lastName, String email,
                                            String gender, String mobile, String address,
                                            String fileName, String hobby, String state, String city,
                                            String[] subjects, String expectedDate) {
        $(".modal-title").shouldHave(text("Thanks for submitting the form"));

        // Проверки ВСЕХ полей включая дату рождения
        verifyField("Student Name", firstName + " " + lastName);
        verifyField("Student Email", email);
        verifyField("Gender", gender);
        verifyField("Mobile", mobile);
        verifyField("Date of Birth", expectedDate); // ← ДОБАВЛЕНА проверка даты
        verifyField("Subjects", String.join(", ", subjects));
        verifyField("Hobbies", hobby);
        verifyField("Picture", fileName);
        verifyField("Address", address);
        verifyField("State and City", state + " " + city);

        takeScreenshot("Полное модальное окно со всеми данными");
        System.out.println("✅ Полное модальное окно со всеми данными проверено");
    }

    @Step("Проверить поле: {fieldName} = {expectedValue}")
    private void verifyField(String fieldName, String expectedValue) {
        if (expectedValue == null || expectedValue.trim().isEmpty()) {
            System.out.println("⚠️ Пропущена проверка поля '" + fieldName + "' - пустое значение");
            return;
        }

        $x("//td[text()='" + fieldName + "']/following-sibling::td")
                .shouldHave(text(expectedValue));
        Allure.addAttachment(fieldName, "text/plain", expectedValue);
    }

    @Step("Закрыть модальное окно")
    private void closeModal() {
        $("#closeLargeModal").click();
        $(".modal-content").shouldNotBe(visible);
        takeScreenshot("Модальное окно закрыто");
        System.out.println("✅ Модальное окно закрыто");
    }

    private String getFileSizeFromClasspath(String fileName) {
        try {
            java.net.URL resource = getClass().getClassLoader().getResource(fileName);
            if (resource != null) {
                Path path = Paths.get(resource.toURI());
                return String.valueOf(Files.size(path));
            }
        } catch (Exception e) {
            System.out.println("⚠️ Не удалось получить размер файла: " + e.getMessage());
        }
        return "неизвестно";
    }

    @Attachment(value = "{attachmentName}", type = "image/png")
    private byte[] takeScreenshot(String attachmentName) {
        try {
            return com.codeborne.selenide.Selenide.screenshot(OutputType.BYTES);
        } catch (Exception e) {
            System.out.println("⚠️ Не удалось сделать скриншот: " + e.getMessage());
            return new byte[0];
        }
    }
}