// Проект 7 спринта
// Пантази Дмитрий Саввович

// Java версия 11
// JUnit версия 4.13.2
// Allure-maven версия 2.10
// Gson версия 2.11.0
// javafaker версия 1.0.2
// rest-assured версия 5.5.0


// mvn clean test - запустить все тесты с покрытием
// mvn allure:report - создание отчёта Allure
// mvn allure:serve - открытие отчёта Allure

// 1. Сделан отдельный класс RestClient для управления отправкой запросов через rest assured, в нём также
реализована сериализация объекта в JSON. Это было необходимо для исключения Null значений и гибким управлением
отправляемых полей в body.
// 2. Вся логика работы с API вынесена в отдельный класс, который называет в соответсвии с действием ручек и
имеет в конце названия API
// 3. В BaseTest реализованы общие вещи такие как, создание объекта RestClient для отправки запросов, настройка
URL для RestAssured а так же небольшая настройка для отчётов Allure, что бы даннвые запросов были в отчётах