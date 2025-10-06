# PZ10 Autotest Project 🚀

![Java](https://img.shields.io/badge/Java-11-orange)
![Selenide](https://img.shields.io/badge/Selenide-7.0.4-blue)
![JUnit5](https://img.shields.io/badge/JUnit5-5.10.0-green)
![Allure](https://img.shields.io/badge/Allure-2.24.0-red)

Проект автоматизации тестирования веб-приложений с использованием современных инструментов и лучших практик.

## 📋 О проекте

Этот проект содержит автоматизированные тесты для:
- **DemoQA Practice Form** - тестирование формы регистрации
- **Яндекс Поиск** - тестирование поисковых функций
- Поддержка UI и API тестирования

## 🛠 Технологии

- **Java 11** - основной язык программирования
- **Selenide** - фреймворк для UI тестирования
- **JUnit 5** - фреймворк для запуска тестов
- **Allure Report** - система отчетности
- **Owner** - работа с конфигурационными файлами
- **Java Faker** - генерация тестовых данных
- **WebDriverManager** - управление веб-драйверами

## 📁 Структура проекта
PZ10_Autotest/
├── src/test/java/
│ ├── config/ # Конфигурационные классы
│ ├── pages/ # Page Object модели
│ └── tests/ # Тестовые классы
├── src/test/resources/ # Ресурсы и тестовые данные
├── allure-results/ # Результаты тестов для Allure
├── allure-report/ # Сгенерированные отчеты Allure
└── target/ # Скомпилированные классы


## 🚀 Быстрый старт

### Предварительные требования
- Java 11 или выше
- Maven 3.6+
- Internet connection

### Установка и запуск

1. **Клонировать репозиторий**
   ```bash
   git clone [url-репозитория]
   cd PZ10_Autotest

Запустить все тесты

bash
mvn clean test
Сгенерировать Allure отчет

bash
mvn allure:serve

Команды Maven
Команда	Описание
mvn clean test	Запустить все тесты
mvn allure:serve	Показать отчет Allure
mvn clean compile	Скомпилировать проект
📊 Отчетность
Проект использует Allure Framework для детальной отчетности:

Скриншоты на каждом шаге

Логи тестовых шагов

Информация о тестовых данных

Графики и статистика

После запуска тестов откройте отчет:

bash
mvn allure:serve
🧪 Примеры тестов
UI тест формы регистрации
java
@Test
@DisplayName("Полное заполнение формы со всеми полями")
void shouldFillCompleteFormWithAllFields() {
// Генерация тестовых данных
// Заполнение всех полей формы
// Проверка успешной отправки
}
Конфигурация
Настройки проекта в src/test/resources/test.properties:

properties
base.url=https://demoqa.com
browser=chrome
timeout=30000
👥 Разработка
Code Style
Page Object Pattern

Fluent Interface

Step-by-step аннотации Allure

Четкое разделение тестовых данных и логики

Добавление новых тестов
Создать Page Object в pages/

Добавить тестовый класс в tests/

Наследоваться от BaseTest

Добавить Allure аннотации

📞 Контакты
Если у вас есть вопросы или предложения по проекту - создавайте issue!

⭐ Не забудьте поставить звезду, если проект был полезен!