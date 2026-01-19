package api_tests;

import api_tests.client.ApiClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static api_tests.constants.ApiConstants.*;
import static org.hamcrest.Matchers.*;


@Epic("API-Тесты")
@Feature("Негативные тесты")
@DisplayName("Негативные API-тесты c проверкой разных сценариев")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NegativeTests extends BaseApiTest {

    private static final String TAG_NAME = "Negative_tests";
    private ApiClient apiClient;

    @BeforeEach
    void setUp() {
        apiClient = new ApiClient();
    }

    @Test
    @Order(1)
    @DisplayName("Отправка НЕ POST запроса")
    @Description("Пользователь отправляет НЕ POST запрос и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void nonPostRequest() {
        Allure.step("Отправка GET запроса", () -> {
            Response response = apiClient.sendRequest("GET", Actions.LOGIN);
            Allure.step("Валидация ответа на GET запрос", () -> {
                int statusCode = response.getStatusCode();
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });

        Allure.step("Отправка PUT запроса", () -> {
            Response response = apiClient.sendRequest("PUT", Actions.LOGIN);
            Allure.step("Валидация ответа на PUT запрос", () -> {
                int statusCode = response.getStatusCode();
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(2)
    @DisplayName("Отправка POST запроса на некорректный URL")
    @Description("Пользователь отправляет POST запрос на некорректный URL и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void incorrectUrl() {
        Allure.step("Отправка POST запроса на некорректный URL", () -> {
            Response response = apiClient.sendPostRequestToUrl("http://localhost:8080/qwerty", Actions.LOGIN);
            Allure.step("Валидация ответа при некорректном URL", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(Matchers.greaterThanOrEqualTo(400));
            });
        });
    }

    @Test
    @Order(3)
    @DisplayName("Отправка POST запроса с пустыми Headers")
    @Description("Пользователь отправляет POST запрос с пустыми Headers и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void emptyHeaders() {
        Allure.step("Отправка POST запроса с пустыми Headers (пустой HashMap)", () -> {
            Map<String, String> emptyHeaders = new HashMap<>();
            Response response = apiClient.sendPostRequestWithHeaders(Actions.LOGIN, emptyHeaders);
            Allure.step("Валидация ответа при пустых заголовках", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Заголовки запроса", "text/plain", emptyHeaders.toString());
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(4)
    @DisplayName("Отправка POST запроса с некорректными Headers")
    @Description("Пользователь отправляет POST запрос с некорректными Headers и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void incorrectHeaders() {
        Allure.step("Отправка POST запроса с некорректными Headers", () -> {
            Map<String, String> incorrectHeaders = new HashMap<>();
            incorrectHeaders.put(Headers.CONTENT_TYPE, "text/plain");
            incorrectHeaders.put(Headers.API_KEY, "incorrect-key");
            incorrectHeaders.put(Headers.ACCEPT, "text/html");
            Response response = apiClient.sendPostRequestWithHeaders(Actions.LOGIN, incorrectHeaders);
            Allure.step("Валидация ответа при некорректных заголовках", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Некорректные заголовки", "text/plain", incorrectHeaders.toString());
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(5)
    @DisplayName("Отправка POST запроса с пустым X-API-KEY")
    @Description("Пользователь отправляет POST запрос с пустым X-API-KEY и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void emptyApiKey() {
        Allure.step("Отправка POST запроса с пустым X-API-KEY", () -> {
            Map<String, String> headers = new HashMap<>();
            headers.put(Headers.CONTENT_TYPE, Headers.APPLICATION_FORM_URLENCODED);
            headers.put(Headers.API_KEY, "");
            headers.put(Headers.ACCEPT, Headers.APPLICATION_JSON);

            Response response = apiClient.sendPostRequestWithHeaders(Actions.LOGIN, headers);
            Allure.step("Валидация ответа при пустом API-ключе", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Заголовки запроса", "text/plain", headers.toString());
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(StatusCodes.UNAUTHORIZED);
            });
        });
    }

    @Test
    @Order(6)
    @DisplayName("Отправка POST запроса с некорректным X-API-KEY")
    @Description("Пользователь отправляет POST запрос с некорректным X-API-KEY и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void incorrectApiKey() {
        Allure.step("Отправка POST запроса с некорректным X-API-KEY", () -> {
            Map<String, String> headers = new HashMap<>();
            headers.put(Headers.CONTENT_TYPE, Headers.APPLICATION_FORM_URLENCODED);
            headers.put(Headers.API_KEY, "incorrect-key");
            headers.put(Headers.ACCEPT, Headers.APPLICATION_JSON);
            Response response = apiClient.sendPostRequestWithHeaders(Actions.LOGIN, headers);
            Allure.step("Валидация ответа при некорректном API-ключе", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Заголовки запроса", "text/plain", headers.toString());
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(StatusCodes.UNAUTHORIZED);
            });
        });
    }

    @Test
    @Order(7)
    @DisplayName("Отправка POST запроса с пустым token но корректным action")
    @Description("Пользователь отправляет POST запрос с пустым token но корректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void emptyTokenWithCorrectAction() {
        Allure.step("Отправка POST запроса с пустым token но корректным action", () -> {
            Response response = apiClient.sendPostRequestWithBodyParams("", Actions.LOGIN);
            Allure.step("Валидация ответа при пустом токене", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Параметры запроса", "text/plain", "token: '', action: " + Actions.LOGIN);
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(8)
    @DisplayName("Отправка POST запроса с корректным token но пустым action")
    @Description("Пользователь отправляет POST запрос с корректным token но пустым action и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void correctTokenWithEmptyAction() {
        Allure.step("Отправка POST запроса с корректным token но пустым action", () -> {
            Response response = apiClient.sendPostRequestWithBodyParams("AAAABBBBCCCCDDDDEEEEFFFF12345678", "");
            Allure.step("Валидация ответа при пустом action", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: AAAABBBBCCCCDDDDEEEEFFFF12345678, action: ''");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(9)
    @DisplayName("Отправка POST запроса с пустым token и пустым action")
    @Description("Пользователь отправляет POST запрос с пустым token и пустым action и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void emptyTokenAndEmptyAction() {
        Allure.step("Отправка POST запроса с пустым token и пустым action", () -> {
            Response response = apiClient.sendPostRequestWithBodyParams("", "");
            Allure.step("Валидация ответа при пустых параметрах", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Параметры запроса", "text/plain", "token: '', action: ''");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(10)
    @DisplayName("Отправка POST запроса с некорректным token и корректным action")
    @Description("Пользователь отправляет POST запрос с некорректным token и корректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void incorrectTokenWithCorrectAction() {
        Allure.step("Отправка POST запроса с некорректным token и корректным action", () -> {
            Response response = apiClient.sendPostRequestWithBodyParams("incorrect-token", Actions.LOGIN);
            Allure.step("Валидация ответа при некорректном токене", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: 'incorrect-token', action: " + Actions.LOGIN);
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(11)
    @DisplayName("Отправка POST запроса с корректным token и некорректным action")
    @Description("Пользователь отправляет POST запрос с корректным token и некорректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void correctTokenWithIncorrectAction() {
        Allure.step("Отправка POST запроса с корректным token и некорректным action", () -> {
            Response response = apiClient.sendPostRequestWithBodyParams("AAAABBBBCCCCDDDDEEEEFFFF12345678", "INCORRECT");
            Allure.step("Валидация ответа при некорректном action", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: AAAABBBBCCCCDDDDEEEEFFFF12345678, action: 'INCORRECT'");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(12)
    @DisplayName("Отправка POST запроса с некорректным token и некорректным action")
    @Description("Пользователь отправляет POST запрос с некорректным token и некорректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void incorrectTokenAndIncorrectAction() {
        Allure.step("Отправка POST запроса с некорректным token и некорректным action", () -> {
            Response response = apiClient.sendPostRequestWithBodyParams("incorrect-token", "INCORRECT");
            Allure.step("Валидация ответа при некорректных параметрах", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: 'incorrect-token', action: 'INCORRECT'");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(13)
    @DisplayName("Отправка POST запроса с token больше 32 символов и корректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение больше 32 символов и корректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenLongerThan32CharsWithCorrectAction() {
        Allure.step("Отправка POST запроса с token больше 32 символов", () -> {
            String longToken = "AAAABBBBCCCCDDDDEEEEFFFF12345678EXTRA";
            Response response = apiClient.sendPostRequestWithBodyParams(longToken, Actions.LOGIN);
            Allure.step("Валидация ответа при длинном токене", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Длина токена", "text/plain", String.valueOf(longToken.length()));
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + longToken + "', action: " + Actions.LOGIN);
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(14)
    @DisplayName("Отправка POST запроса с token больше 32 символов и некорректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение больше 32 символов и некорректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenLongerThan32CharsWithIncorrectAction() {
        Allure.step("Отправка POST запроса с token больше 32 символов и некорректным action", () -> {
            String longToken = "AAAABBBBCCCCDDDDEEEEFFFF12345678EXTRA";
            Response response = apiClient.sendPostRequestWithBodyParams(longToken, "INCORRECT");
            Allure.step("Валидация ответа при длинном токене и некорректном action", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Длина токена", "text/plain", String.valueOf(longToken.length()));
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + longToken + "', action: 'INCORRECT'");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(15)
    @DisplayName("Отправка POST запроса с token больше 32 символов и пустым action")
    @Description("Пользователь отправляет POST запрос с token у которого значение больше 32 символов и пустой action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenLongerThan32CharsWithEmptyAction() {
        Allure.step("Отправка POST запроса с token больше 32 символов и пустым action", () -> {
            String longToken = "AAAABBBBCCCCDDDDEEEEFFFF12345678EXTRA";
            Response response = apiClient.sendPostRequestWithBodyParams(longToken, "");
            Allure.step("Валидация ответа при длинном токене и пустом action", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Длина токена", "text/plain", String.valueOf(longToken.length()));
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + longToken + "', action: ''");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(16)
    @DisplayName("Отправка POST запроса с token меньше 32 символов и корректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение меньше 32 символов и корректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenShorterThan32CharsWithCorrectAction() {
        Allure.step("Отправка POST запроса с token меньше 32 символов", () -> {
            String shortToken = "AAAABBBBCCCCDDDDEEEEFFFF12345"; // 31 символ
            Response response = apiClient.sendPostRequestWithBodyParams(shortToken, Actions.LOGIN);
            Allure.step("Валидация ответа при коротком токене", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Длина токена", "text/plain", String.valueOf(shortToken.length()));
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + shortToken + "', action: " + Actions.LOGIN);
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(17)
    @DisplayName("Отправка POST запроса с token меньше 32 символов и некорректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение меньше 32 символов и некорректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenShorterThan32CharsWithIncorrectAction() {
        Allure.step("Отправка POST запроса с token меньше 32 символов и некорректным action", () -> {
            String shortToken = "AAAABBBBCCCCDDDDEEEEFFFF12345"; // 31 символ
            Response response = apiClient.sendPostRequestWithBodyParams(shortToken, "INCORRECT");
            Allure.step("Валидация ответа при коротком токене и некорректном action", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Длина токена", "text/plain", String.valueOf(shortToken.length()));
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + shortToken + "', action: 'INCORRECT'");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(18)
    @DisplayName("Отправка POST запроса с token меньше 32 символов и пустым action")
    @Description("Пользователь отправляет POST запрос с token у которого значение меньше 32 символов и пустой action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenShorterThan32CharsWithEmptyAction() {
        Allure.step("Отправка POST запроса с token меньше 32 символов и пустым action", () -> {
            String shortToken = "AAAABBBBCCCCDDDDEEEEFFFF12345"; // 31 символ
            Response response = apiClient.sendPostRequestWithBodyParams(shortToken, "");
            Allure.step("Валидация ответа при коротком токене и пустом action", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Длина токена", "text/plain", String.valueOf(shortToken.length()));
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + shortToken + "', action: ''");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(19)
    @DisplayName("Отправка POST запроса с token только из заглавных букв и корректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из заглавных английских букв и корректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenOnlyUpperCaseLettersWithCorrectAction() {
        Allure.step("Отправка POST запроса с token только из заглавных букв", () -> {
            String upperCaseToken = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEF"; // 32 символа
            Response response = apiClient.sendPostRequestWithBodyParams(upperCaseToken, Actions.LOGIN);
            Allure.step("Валидация ответа при токене из заглавных букв", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Тип токена", "text/plain", "Только заглавные буквы");
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + upperCaseToken + "', action: " + Actions.LOGIN);
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(20)
    @DisplayName("Отправка POST запроса с token только из заглавных букв и некорректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из заглавных английских букв и некорректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenOnlyUpperCaseLettersWithIncorrectAction() {
        Allure.step("Отправка POST запроса с token только из заглавных букв и некорректным action", () -> {
            String upperCaseToken = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEF"; // 32 символа
            Response response = apiClient.sendPostRequestWithBodyParams(upperCaseToken, "INCORRECT");
            Allure.step("Валидация ответа при токене из заглавных букв и некорректном action", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Тип токена", "text/plain", "Только заглавные буквы");
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + upperCaseToken + "', action: 'INCORRECT'");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(21)
    @DisplayName("Отправка POST запроса с token только из заглавных букв и пустым action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из заглавных английских букв и пустой action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenOnlyUpperCaseLettersWithEmptyAction() {
        Allure.step("Отправка POST запроса с token только из заглавных букв и пустым action", () -> {
            String upperCaseToken = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEF"; // 32 символа
            Response response = apiClient.sendPostRequestWithBodyParams(upperCaseToken, "");
            Allure.step("Валидация ответа при токене из заглавных букв и пустом action", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Тип токена", "text/plain", "Только заглавные буквы");
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + upperCaseToken + "', action: ''");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(22)
    @DisplayName("Отправка POST запроса с token только из цифр и корректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из цифр и корректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenOnlyDigitsWithCorrectAction() {
        Allure.step("Отправка POST запроса с token только из цифр", () -> {
            String digitsToken = "12345678901234567890123456789012"; // 32 символа
            Response response = apiClient.sendPostRequestWithBodyParams(digitsToken, Actions.LOGIN);
            Allure.step("Валидация ответа при токене из цифр", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Тип токена", "text/plain", "Только цифры");
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + digitsToken + "', action: " + Actions.LOGIN);
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(23)
    @DisplayName("Отправка POST запроса с token только из цифр и некорректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из цифр и некорректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenOnlyDigitsWithIncorrectAction() {
        Allure.step("Отправка POST запроса с token только из цифр и некорректным action", () -> {
            String digitsToken = "12345678901234567890123456789012"; // 32 символа
            Response response = apiClient.sendPostRequestWithBodyParams(digitsToken, "INCORRECT");
            Allure.step("Валидация ответа при токене из цифр и некорректном action", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Тип токена", "text/plain", "Только цифры");
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + digitsToken + "', action: 'INCORRECT'");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(24)
    @DisplayName("Отправка POST запроса с token только из цифр и пустым action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из цифр и пустой action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenOnlyDigitsWithEmptyAction() {
        Allure.step("Отправка POST запроса с token только из цифр и пустым action", () -> {
            String digitsToken = "12345678901234567890123456789012"; // 32 символа
            Response response = apiClient.sendPostRequestWithBodyParams(digitsToken, "");
            Allure.step("Валидация ответа при токене из цифр и пустом action", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Тип токена", "text/plain", "Только цифры");
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + digitsToken + "', action: ''");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(25)
    @DisplayName("Отправка POST запроса с token только из латинских букв и корректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из латинских букв и корректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenOnlyLatinLettersWithCorrectAction() {
        Allure.step("Отправка POST запроса с token только из латинских букв", () -> {
            String latinToken = "abcdefghijklmnopqrstuvwxyzabcdef"; // 32 символа, только строчные
            Response response = apiClient.sendPostRequestWithBodyParams(latinToken, Actions.LOGIN);
            Allure.step("Валидация ответа при токене из латинских букв", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Тип токена", "text/plain", "Только строчные латинские буквы");
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + latinToken + "', action: " + Actions.LOGIN);
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(26)
    @DisplayName("Отправка POST запроса с token только из латинских букв и некорректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из латинских букв и некорректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenOnlyLatinLettersWithIncorrectAction() {
        Allure.step("Отправка POST запроса с token только из латинских букв и некорректным action", () -> {
            String latinToken = "abcdefghijklmnopqrstuvwxyzabcdef"; // 32 символа, только строчные
            Response response = apiClient.sendPostRequestWithBodyParams(latinToken, "INCORRECT");
            Allure.step("Валидация ответа при токене из латинских букв и некорректном action", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Тип токена", "text/plain", "Только строчные латинские буквы");
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + latinToken + "', action: 'INCORRECT'");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(27)
    @DisplayName("Отправка POST запроса с token только из латинских букв и пустым action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из латинских букв и пустой action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenOnlyLatinLettersWithEmptyAction() {
        Allure.step("Отправка POST запроса с token только из латинских букв и пустым action", () -> {
            String latinToken = "abcdefghijklmnopqrstuvwxyzabcdef"; // 32 символа, только строчные
            Response response = apiClient.sendPostRequestWithBodyParams(latinToken, "");
            Allure.step("Валидация ответа при токене из латинских букв и пустом action", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Тип токена", "text/plain", "Только строчные латинские буквы");
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + latinToken + "', action: ''");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(28)
    @DisplayName("Отправка POST запроса с token символами разного регистра и корректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение символами разного регистра и корректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenMixedCaseWithCorrectAction() {
        Allure.step("Отправка POST запроса с token символами разного регистра", () -> {
            String mixedCaseToken = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPp"; // 32 символа, смешанный регистр
            Response response = apiClient.sendPostRequestWithBodyParams(mixedCaseToken, Actions.LOGIN);
            Allure.step("Валидация ответа при токене со смешанным регистром", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Тип токена", "text/plain", "Смешанный регистр (AaBb...)");
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + mixedCaseToken + "', action: " + Actions.LOGIN);
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(29)
    @DisplayName("Отправка POST запроса с token символами разного регистра и некорректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение символами разного регистра и некорректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenMixedCaseWithIncorrectAction() {
        Allure.step("Отправка POST запроса с token символами разного регистра и некорректным action", () -> {
            String mixedCaseToken = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPp"; // 32 символа, смешанный регистр
            Response response = apiClient.sendPostRequestWithBodyParams(mixedCaseToken, "INCORRECT");
            Allure.step("Валидация ответа при токене со смешанным регистром и некорректном action", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Тип токена", "text/plain", "Смешанный регистр (AaBb...)");
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + mixedCaseToken + "', action: 'INCORRECT'");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(30)
    @DisplayName("Отправка POST запроса с token символами разного регистра и пустым action")
    @Description("Пользователь отправляет POST запрос с token у которого значение символами разного регистра и пустой action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void tokenMixedCaseWithEmptyAction() {
        Allure.step("Отправка POST запроса с token символами разного регистра и пустым action", () -> {
            String mixedCaseToken = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPp"; // 32 символа, смешанный регистр
            Response response = apiClient.sendPostRequestWithBodyParams(mixedCaseToken, "");
            Allure.step("Валидация ответа при токене со смешанным регистром и пустом action", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Тип токена", "text/plain", "Смешанный регистр (AaBb...)");
                Allure.addAttachment("Параметры запроса", "text/plain",
                        "token: '" + mixedCaseToken + "', action: ''");
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }


    @Test
    @Order(31)
    @DisplayName("Отправка нескольких запросов LOGIN подряд с последующим LOGOUT")
    @Description("Пользователь отправляет несколько раз запрос на аутентификацию LOGIN, затем выполняет LOGOUT и проверяет корректность завершения сессии")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void multipleLoginRequestsWithLogout() {
        Allure.step("Отправка нескольких запросов LOGIN подряд с последующим LOGOUT", () -> {
            Response firstResponse = apiClient.sendPostRequest(Actions.LOGIN);
            Allure.step("Валидация первого LOGIN", () -> {
                int firstStatusCode = firstResponse.getStatusCode();
                String firstResponseBody = firstResponse.getBody().asString();
                Allure.addAttachment("Статус код первого LOGIN", "text/plain", String.valueOf(firstStatusCode));
                Allure.addAttachment("Тело ответа первого LOGIN", "text/plain", firstResponseBody);
                firstResponse.then()
                        .assertThat()
                        .statusCode(StatusCodes.OK);
            });

            Response secondResponse = apiClient.sendPostRequest(Actions.LOGIN);
            Allure.step("Валидация второго LOGIN", () -> {
                int secondStatusCode = secondResponse.getStatusCode();
                String secondResponseBody = secondResponse.getBody().asString();
                Allure.addAttachment("Статус код второго LOGIN", "text/plain", String.valueOf(secondStatusCode));
                Allure.addAttachment("Тело ответа второго LOGIN", "text/plain", secondResponseBody);
                secondResponse.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });

            Allure.step("Отправка LOGOUT после второго LOGIN", () -> {
                Response logoutResponse = apiClient.sendPostRequest(Actions.LOGOUT);
                Allure.step("Валидация LOGOUT", () -> {
                    int logoutStatusCode = logoutResponse.getStatusCode();
                    String logoutResponseBody = logoutResponse.getBody().asString();
                    Allure.addAttachment("Статус код LOGOUT", "text/plain", String.valueOf(logoutStatusCode));
                    Allure.addAttachment("Тело ответа LOGOUT", "text/plain", logoutResponseBody);

                    if (logoutStatusCode == StatusCodes.OK) {
                        Allure.addAttachment("Результат LOGOUT", "text/plain",
                                "Сессия успешно завершена");
                    } else if (logoutStatusCode == StatusCodes.UNAUTHORIZED ||
                            logoutStatusCode == StatusCodes.FORBIDDEN) {
                        Allure.addAttachment("Результат LOGOUT", "text/plain",
                                "Пользователь уже не авторизован (статус: " + logoutStatusCode + ")");
                    } else {
                        Allure.addAttachment("Результат LOGOUT", "text/plain",
                                "Неожиданный статус: " + logoutStatusCode);
                    }
                });
            });
        });
    }

    @Test
    @Order(32)
    @DisplayName("Отправка запроса ACTION без предварительного LOGIN")
    @Description("Пользователь отправляет запрос на выполнение действия без предварительного ACTION и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void actionWithoutLogin() {
        Allure.step("Отправка запроса ACTION без предварительного LOGIN", () -> {
            Response response = apiClient.sendPostRequest(Actions.ACTION);
            Allure.step("Валидация ответа ACTION без LOGIN", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(33)
    @DisplayName("Отправка запроса LOGOUT без предварительного LOGIN")
    @Description("Пользователь отправляет запрос на выполнение завершения сессии (LOGOUT) без предварительного ACTION и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void logoutWithoutLogin() {
        Allure.step("Отправка запроса LOGOUT без предварительного LOGIN", () -> {
            Response response = apiClient.sendPostRequest(Actions.LOGOUT);
            Allure.step("Валидация ответа LOGOUT без LOGIN", () -> {
                int statusCode = response.getStatusCode();
                String responseBody = response.getBody().asString();
                Allure.addAttachment("Статус код", "text/plain", String.valueOf(statusCode));
                Allure.addAttachment("Тело ответа", "text/plain", responseBody);
                response.then()
                        .assertThat()
                        .statusCode(not(StatusCodes.OK));
            });
        });
    }

    @Test
    @Order(34)
    @DisplayName("Отправка нескольких запросов LOGOUT после LOGIN")
    @Description("Пользователь успешно аутентифицируется и несколько раз отправляет запрос на завершение сессии (LOGOUT) и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void multipleLogoutRequests() {
        Allure.step("Выполнение LOGIN", () -> {
            Response loginResponse = apiClient.sendPostRequest(Actions.LOGIN);
            Allure.step("Валидация LOGIN", () -> {
                int loginStatusCode = loginResponse.getStatusCode();
                Allure.addAttachment("Статус код LOGIN", "text/plain", String.valueOf(loginStatusCode));
                loginResponse.then()
                        .assertThat()
                        .statusCode(StatusCodes.OK);
            });
        });

        Allure.step("Отправка первого LOGOUT", () -> {
            Response firstLogoutResponse = apiClient.sendPostRequest(Actions.LOGOUT);
            Allure.step("Валидация первого LOGOUT", () -> {
                int firstLogoutStatusCode = firstLogoutResponse.getStatusCode();
                Allure.addAttachment("Статус код первого LOGOUT", "text/plain", String.valueOf(firstLogoutStatusCode));
                firstLogoutResponse.then()
                        .assertThat()
                        .statusCode(StatusCodes.OK);
            });
        });

        Allure.step("Отправка второго LOGOUT (ожидается клиентская ошибка 4xx)", () -> {
            Response secondLogoutResponse = apiClient.sendPostRequest(Actions.LOGOUT);
            Allure.step("Валидация второго LOGOUT", () -> {
                int secondLogoutStatusCode = secondLogoutResponse.getStatusCode();
                String secondLogoutResponseBody = secondLogoutResponse.getBody().asString();
                Allure.addAttachment("Статус код второго LOGOUT", "text/plain", String.valueOf(secondLogoutStatusCode));
                Allure.addAttachment("Тело ответа второго LOGOUT", "text/plain", secondLogoutResponseBody);
                secondLogoutResponse.then()
                        .assertThat()
                        .statusCode(lessThan(500));
            });
        });
    }
}
