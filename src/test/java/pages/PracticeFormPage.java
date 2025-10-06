package pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;

public class PracticeFormPage {

    private SelenideElement
            firstName = $("#firstName"),
            lastName = $("#lastName"),
            userEmail = $("#userEmail"),
            genderMale = $("label[for='gender-radio-1']"),
            genderFemale = $("label[for='gender-radio-2']"),
            genderOther = $("label[for='gender-radio-3']"),
            userNumber = $("#userNumber"),
            currentAddress = $("#currentAddress"),
            uploadPicture = $("#uploadPicture"),
            hobbiesSports = $("label[for='hobbies-checkbox-1']"),
            hobbiesReading = $("label[for='hobbies-checkbox-2']"),
            hobbiesMusic = $("label[for='hobbies-checkbox-3']"),
            stateDropdown = $("#state"),
            cityDropdown = $("#city"),
            subjectsInput = $("#subjectsInput"),
            dateOfBirthInput = $("#dateOfBirthInput"), // ← ДОБАВЛЕНО поле Date of Birth
            submitButton = $("#submit");

    public PracticeFormPage setFirstName(String value) {
        firstName.setValue(value);
        return this;
    }

    public PracticeFormPage setLastName(String value) {
        lastName.setValue(value);
        return this;
    }

    public PracticeFormPage setEmail(String value) {
        userEmail.setValue(value);
        return this;
    }

    public PracticeFormPage selectGender(String gender) {
        switch (gender.toLowerCase()) {
            case "male": genderMale.click(); break;
            case "female": genderFemale.click(); break;
            case "other": genderOther.click(); break;
        }
        return this;
    }

    public PracticeFormPage setMobileNumber(String number) {
        userNumber.setValue(number);
        return this;
    }

    public PracticeFormPage setCurrentAddress(String address) {
        currentAddress.setValue(address);
        return this;
    }

    public PracticeFormPage uploadFile(String filePath) {
        uploadPicture.uploadFromClasspath(filePath);
        return this;
    }

    public PracticeFormPage uploadFileWithAbsolutePath(String absoluteFilePath) {
        uploadPicture.uploadFile(new java.io.File(absoluteFilePath));
        return this;
    }

    public PracticeFormPage selectHobby(String hobby) {
        switch (hobby.toLowerCase()) {
            case "sports": hobbiesSports.click(); break;
            case "reading": hobbiesReading.click(); break;
            case "music": hobbiesMusic.click(); break;
        }
        return this;
    }

    public PracticeFormPage selectHobbies(String... hobbies) {
        for (String hobby : hobbies) {
            selectHobby(hobby);
        }
        return this;
    }

    public PracticeFormPage selectState(String state) {
        stateDropdown.click();
        $x("//div[contains(@class, 'menu')]//div[contains(text(), '" + state + "')]").click();
        return this;
    }

    public PracticeFormPage selectCity(String city) {
        cityDropdown.click();
        $x("//div[contains(@class, 'menu')]//div[contains(text(), '" + city + "')]").click();
        return this;
    }

    public PracticeFormPage selectStateAndCity(String state, String city) {
        selectState(state);
        selectCity(city);
        return this;
    }

    public PracticeFormPage setSubjects(String... subjects) {
        for (String subject : subjects) {
            subjectsInput.setValue(subject).pressEnter();
        }
        return this;
    }

    // ДОБАВЛЕН метод для выбора даты из календаря
    public PracticeFormPage selectDateFromCalendar(int day, String month, int year) {
        dateOfBirthInput.click();

        // Выбор месяца
        $(".react-datepicker__month-select").selectOption(month);

        // Выбор года
        $(".react-datepicker__year-select").selectOption(String.valueOf(year));

        // Выбор дня (исключаем дни из других месяцев)
        $x("//div[contains(@class, 'react-datepicker__day') and not(contains(@class, 'outside-month')) and text()='" + day + "']")
                .click();

        return this;
    }

    // ДОБАВЛЕН метод для выбора даты с помощью клавиатуры
    public PracticeFormPage setDateOfBirth(String date) {
        dateOfBirthInput.setValue(date).pressEnter();
        return this;
    }

    // ДОБАВЛЕН метод для получения текущей даты из поля
    public String getSelectedDate() {
        return dateOfBirthInput.getValue();
    }

    public void submitForm() {
        submitButton.click();
    }

    public PracticeFormPage fillMinimalRequiredFields(
            String first, String last, String email,
            String gender, String mobile) {
        return this.setFirstName(first)
                .setLastName(last)
                .setEmail(email)
                .selectGender(gender)
                .setMobileNumber(mobile);
    }
}