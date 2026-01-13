package api_tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("API Тесты")
@Feature("Позитивные тесты")
@DisplayName("Позитивные тесты API")

public class PositiveTests {

    @Test
    @DisplayName("Успешная аутентификация пользователя")
    @Description("Пользователь отправляет 'Post-запрос' с указанием корректных параметров в теле (Body) " +
            "и заголовках (Headers). В ответ возвращается json c телом успешного ответа.")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("Аутентификация")
    public void authUser() {
        Allure.step("1. Подготовка тестовых данных", () -> {
            Allure.parameter("Token", "AAAABBBBCCCCDDDDEEEEFFFF12345678");
            Allure.parameter("Action", "LOGIN");
        });

        Map<String, String> bodyParams = Allure.step("2. Формирование тела запроса", () -> {
            Map<String, String> params = new HashMap<>();
            params.put("token", "AAAABBBBCCCCDDDDEEEEFFFF12345678");
            params.put("action", "LOGIN");
            return params;
        });

        Allure.step("3. Отправка POST запроса", () -> {
            given()
                    .baseUri("http://localhost:8080")
                    .basePath("/endpoint")
                    .headers(
                            "Content-Type", "application/x-www-form-urlencoded",
                            "X-Api-Key", "qazWSXedc",
                            "Accept", "application/json"
                    )
                    .formParams(bodyParams)
                    .log().all()
                    .when()
                    .post()
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("result", equalTo("OK"));
        });

        Allure.step("4. Валидация успешного выполнения", () -> {
            Allure.addAttachment("Статус теста", "text/plain", "Тест пройден успешно");
        });
    }
}


//    @Test
//    @DisplayName("Выполнение действия")
//    @Description("Пользователь успешно аутентифицируется и посылает Post-запрос на выполнение действия. " +
//            "В ответ возвращается json c телом успешного ответа.")
//    @Severity(SeverityLevel.CRITICAL)
//    @Tag("Позитивный")
//
//    public void doAction () {
//
//    }
//
//
//
//    @Test
//    @DisplayName("Завершение сессии пользователя")
//    @Description("Пользователь успешно аутентифицируется и посылает Post-запрос на завершение сессии." +
//            "В ответ возвращается json c телом успешного ответа. ")
//    @Severity(SeverityLevel.CRITICAL)
//    @Tag("Позитивный")
//
//    public void logoutUser () {
//
//    }






