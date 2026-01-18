package api_tests;

import api_tests.client.ApiClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
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
    @DisplayName("Отправка не POST запроса")
    @Description("Пользователь отправляет не POST запрос и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testNonPostRequest() {
        Allure.step("Отправка GET запроса", () -> {
            Response response = apiClient.sendRequest("GET", Actions.LOGIN);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });

        Allure.step("Отправка PUT запроса", () -> {
            Response response = apiClient.sendRequest("PUT", Actions.LOGIN);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });

        Allure.step("Отправка DELETE запроса", () -> {
            Response response = apiClient.sendRequest("DELETE", Actions.LOGIN);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(2)
    @DisplayName("Отправка POST запроса на некорректный URL")
    @Description("Пользователь отправляет POST запрос на некорректный URL и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testIncorrectUrl() {
        Allure.step("Отправка POST запроса на некорректный URL", () -> {
            Response response = apiClient.sendPostRequestToUrl("http://localhost:8080/incorrect", Actions.LOGIN);
            response.then()
                    .assertThat()
                    .statusCode(StatusCodes.NOT_FOUND);
        });
    }

    @Test
    @Order(3)
    @DisplayName("Отправка POST запроса на пустой URL")
    @Description("Пользователь отправляет POST запрос на пустой URL и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testEmptyUrl() {
        Allure.step("Отправка POST запроса на пустой URL", () -> {
            Response response = apiClient.sendPostRequestToUrl("", Actions.LOGIN);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(4)
    @DisplayName("Отправка POST запроса с пустыми Headers")
    @Description("Пользователь отправляет POST запрос с пустыми Headers и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testEmptyHeaders() {
        Allure.step("Отправка POST запроса с пустыми Headers", () -> {
            Map<String, String> emptyHeaders = new HashMap<>();
            emptyHeaders.put(Headers.CONTENT_TYPE, "");
            emptyHeaders.put(Headers.API_KEY, "");
            emptyHeaders.put(Headers.ACCEPT, "");

            Response response = apiClient.sendPostRequestWithHeaders(Actions.LOGIN, emptyHeaders);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(5)
    @DisplayName("Отправка POST запроса с некорректными Headers")
    @Description("Пользователь отправляет POST запрос с некорректными Headers и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testIncorrectHeaders() {
        Allure.step("Отправка POST запроса с некорректными Headers", () -> {
            Map<String, String> incorrectHeaders = new HashMap<>();
            incorrectHeaders.put(Headers.CONTENT_TYPE, "text/plain");
            incorrectHeaders.put(Headers.API_KEY, "incorrect-key");
            incorrectHeaders.put(Headers.ACCEPT, "text/html");

            Response response = apiClient.sendPostRequestWithHeaders(Actions.LOGIN, incorrectHeaders);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(6)
    @DisplayName("Отправка POST запроса с пустым X-API-KEY")
    @Description("Пользователь отправляет POST запрос с пустым X-API-KEY и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testEmptyApiKey() {
        Allure.step("Отправка POST запроса с пустым X-API-KEY", () -> {
            Map<String, String> headers = new HashMap<>();
            headers.put(Headers.CONTENT_TYPE, Headers.APPLICATION_FORM_URLENCODED);
            headers.put(Headers.API_KEY, "");
            headers.put(Headers.ACCEPT, Headers.APPLICATION_JSON);

            Response response = apiClient.sendPostRequestWithHeaders(Actions.LOGIN, headers);
            response.then()
                    .assertThat()
                    .statusCode(StatusCodes.UNAUTHORIZED);
        });
    }

    @Test
    @Order(7)
    @DisplayName("Отправка POST запроса с некорректным X-API-KEY")
    @Description("Пользователь отправляет POST запрос с некорректным X-API-KEY и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testIncorrectApiKey() {
        Allure.step("Отправка POST запроса с некорректным X-API-KEY", () -> {
            Map<String, String> headers = new HashMap<>();
            headers.put(Headers.CONTENT_TYPE, Headers.APPLICATION_FORM_URLENCODED);
            headers.put(Headers.API_KEY, "incorrect-key");
            headers.put(Headers.ACCEPT, Headers.APPLICATION_JSON);

            Response response = apiClient.sendPostRequestWithHeaders(Actions.LOGIN, headers);
            response.then()
                    .assertThat()
                    .statusCode(StatusCodes.UNAUTHORIZED);
        });
    }

    @Test
    @Order(8)
    @DisplayName("Отправка POST запроса с пустым token но корректным action")
    @Description("Пользователь отправляет POST запрос с пустым token но корректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testEmptyTokenWithCorrectAction() {
        Allure.step("Отправка POST запроса с пустым token но корректным action", () -> {
            Response response = apiClient.sendPostRequestWithBodyParams("", Actions.LOGIN);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(9)
    @DisplayName("Отправка POST запроса с корректным token но пустым action")
    @Description("Пользователь отправляет POST запрос с корректным token но пустым action и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testCorrectTokenWithEmptyAction() {
        Allure.step("Отправка POST запроса с корректным token но пустым action", () -> {
            Response response = apiClient.sendPostRequestWithBodyParams("AAAABBBBCCCCDDDDEEEEFFFF12345678", "");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(10)
    @DisplayName("Отправка POST запроса с пустым token и пустым action")
    @Description("Пользователь отправляет POST запрос с пустым token и пустым action и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testEmptyTokenAndEmptyAction() {
        Allure.step("Отправка POST запроса с пустым token и пустым action", () -> {
            Response response = apiClient.sendPostRequestWithBodyParams("", "");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(11)
    @DisplayName("Отправка POST запроса с некорректным token и корректным action")
    @Description("Пользователь отправляет POST запрос с некорректным token и корректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testIncorrectTokenWithCorrectAction() {
        Allure.step("Отправка POST запроса с некорректным token и корректным action", () -> {
            Response response = apiClient.sendPostRequestWithBodyParams("incorrect-token", Actions.LOGIN);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(12)
    @DisplayName("Отправка POST запроса с корректным token и некорректным action")
    @Description("Пользователь отправляет POST запрос с корректным token и некорректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testCorrectTokenWithIncorrectAction() {
        Allure.step("Отправка POST запроса с корректным token и некорректным action", () -> {
            Response response = apiClient.sendPostRequestWithBodyParams("AAAABBBBCCCCDDDDEEEEFFFF12345678", "INCORRECT");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(13)
    @DisplayName("Отправка POST запроса с некорректным token и некорректным action")
    @Description("Пользователь отправляет POST запрос с некорректным token и некорректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testIncorrectTokenAndIncorrectAction() {
        Allure.step("Отправка POST запроса с некорректным token и некорректным action", () -> {
            Response response = apiClient.sendPostRequestWithBodyParams("incorrect-token", "INCORRECT");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(14)
    @DisplayName("Отправка POST запроса с token больше 32 символов и корректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение больше 32 символов и корректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenLongerThan32CharsWithCorrectAction() {
        Allure.step("Отправка POST запроса с token больше 32 символов", () -> {
            String longToken = "AAAABBBBCCCCDDDDEEEEFFFF12345678EXTRA";
            Response response = apiClient.sendPostRequestWithBodyParams(longToken, Actions.LOGIN);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(15)
    @DisplayName("Отправка POST запроса с token больше 32 символов и некорректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение больше 32 символов и некорректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenLongerThan32CharsWithIncorrectAction() {
        Allure.step("Отправка POST запроса с token больше 32 символов и некорректным action", () -> {
            String longToken = "AAAABBBBCCCCDDDDEEEEFFFF12345678EXTRA";
            Response response = apiClient.sendPostRequestWithBodyParams(longToken, "INCORRECT");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(16)
    @DisplayName("Отправка POST запроса с token больше 32 символов и пустым action")
    @Description("Пользователь отправляет POST запрос с token у которого значение больше 32 символов и пустой action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenLongerThan32CharsWithEmptyAction() {
        Allure.step("Отправка POST запроса с token больше 32 символов и пустым action", () -> {
            String longToken = "AAAABBBBCCCCDDDDEEEEFFFF12345678EXTRA";
            Response response = apiClient.sendPostRequestWithBodyParams(longToken, "");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(17)
    @DisplayName("Отправка POST запроса с token меньше 32 символов и корректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение меньше 32 символов и корректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenShorterThan32CharsWithCorrectAction() {
        Allure.step("Отправка POST запроса с token меньше 32 символов", () -> {
            String shortToken = "AAAABBBBCCCCDDDDEEEEFFFF12345"; // 31 символ
            Response response = apiClient.sendPostRequestWithBodyParams(shortToken, Actions.LOGIN);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(18)
    @DisplayName("Отправка POST запроса с token меньше 32 символов и некорректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение меньше 32 символов и некорректным action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenShorterThan32CharsWithIncorrectAction() {
        Allure.step("Отправка POST запроса с token меньше 32 символов и некорректным action", () -> {
            String shortToken = "AAAABBBBCCCCDDDDEEEEFFFF12345"; // 31 символ
            Response response = apiClient.sendPostRequestWithBodyParams(shortToken, "INCORRECT");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(19)
    @DisplayName("Отправка POST запроса с token меньше 32 символов и пустым action")
    @Description("Пользователь отправляет POST запрос с token у которого значение меньше 32 символов и пустой action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenShorterThan32CharsWithEmptyAction() {
        Allure.step("Отправка POST запроса с token меньше 32 символов и пустым action", () -> {
            String shortToken = "AAAABBBBCCCCDDDDEEEEFFFF12345"; // 31 символ
            Response response = apiClient.sendPostRequestWithBodyParams(shortToken, "");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(20)
    @DisplayName("Отправка POST запроса с token только из заглавных букв и корректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из заглавных английских букв и корректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenOnlyUpperCaseLettersWithCorrectAction() {
        Allure.step("Отправка POST запроса с token только из заглавных букв", () -> {
            String upperCaseToken = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEF"; // 32 символа
            Response response = apiClient.sendPostRequestWithBodyParams(upperCaseToken, Actions.LOGIN);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(21)
    @DisplayName("Отправка POST запроса с token только из заглавных букв и некорректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из заглавных английских букв и некорректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenOnlyUpperCaseLettersWithIncorrectAction() {
        Allure.step("Отправка POST запроса с token только из заглавных букв и некорректным action", () -> {
            String upperCaseToken = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEF"; // 32 символа
            Response response = apiClient.sendPostRequestWithBodyParams(upperCaseToken, "INCORRECT");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(22)
    @DisplayName("Отправка POST запроса с token только из заглавных букв и пустым action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из заглавных английских букв и пустой action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenOnlyUpperCaseLettersWithEmptyAction() {
        Allure.step("Отправка POST запроса с token только из заглавных букв и пустым action", () -> {
            String upperCaseToken = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEF"; // 32 символа
            Response response = apiClient.sendPostRequestWithBodyParams(upperCaseToken, "");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(23)
    @DisplayName("Отправка POST запроса с token только из цифр и корректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из цифр и корректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenOnlyDigitsWithCorrectAction() {
        Allure.step("Отправка POST запроса с token только из цифр", () -> {
            String digitsToken = "12345678901234567890123456789012"; // 32 символа
            Response response = apiClient.sendPostRequestWithBodyParams(digitsToken, Actions.LOGIN);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(24)
    @DisplayName("Отправка POST запроса с token только из цифр и некорректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из цифр и некорректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenOnlyDigitsWithIncorrectAction() {
        Allure.step("Отправка POST запроса с token только из цифр и некорректным action", () -> {
            String digitsToken = "12345678901234567890123456789012"; // 32 символа
            Response response = apiClient.sendPostRequestWithBodyParams(digitsToken, "INCORRECT");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(25)
    @DisplayName("Отправка POST запроса с token только из цифр и пустым action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из цифр и пустой action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenOnlyDigitsWithEmptyAction() {
        Allure.step("Отправка POST запроса с token только из цифр и пустым action", () -> {
            String digitsToken = "12345678901234567890123456789012"; // 32 символа
            Response response = apiClient.sendPostRequestWithBodyParams(digitsToken, "");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(26)
    @DisplayName("Отправка POST запроса с token только из латинских букв и корректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из латинских букв и корректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenOnlyLatinLettersWithCorrectAction() {
        Allure.step("Отправка POST запроса с token только из латинских букв", () -> {
            String latinToken = "abcdefghijklmnopqrstuvwxyzabcdef"; // 32 символа, только строчные
            Response response = apiClient.sendPostRequestWithBodyParams(latinToken, Actions.LOGIN);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(27)
    @DisplayName("Отправка POST запроса с token только из латинских букв и некорректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из латинских букв и некорректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenOnlyLatinLettersWithIncorrectAction() {
        Allure.step("Отправка POST запроса с token только из латинских букв и некорректным action", () -> {
            String latinToken = "abcdefghijklmnopqrstuvwxyzabcdef"; // 32 символа, только строчные
            Response response = apiClient.sendPostRequestWithBodyParams(latinToken, "INCORRECT");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(28)
    @DisplayName("Отправка POST запроса с token только из латинских букв и пустым action")
    @Description("Пользователь отправляет POST запрос с token у которого значение только из латинских букв и пустой action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenOnlyLatinLettersWithEmptyAction() {
        Allure.step("Отправка POST запроса с token только из латинских букв и пустым action", () -> {
            String latinToken = "abcdefghijklmnopqrstuvwxyzabcdef"; // 32 символа, только строчные
            Response response = apiClient.sendPostRequestWithBodyParams(latinToken, "");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(29)
    @DisplayName("Отправка POST запроса с token символами разного регистра и корректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение символами разного регистра и корректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenMixedCaseWithCorrectAction() {
        Allure.step("Отправка POST запроса с token символами разного регистра", () -> {
            String mixedCaseToken = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPp"; // 32 символа, смешанный регистр
            Response response = apiClient.sendPostRequestWithBodyParams(mixedCaseToken, Actions.LOGIN);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(30)
    @DisplayName("Отправка POST запроса с token символами разного регистра и некорректным action")
    @Description("Пользователь отправляет POST запрос с token у которого значение символами разного регистра и некорректный action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenMixedCaseWithIncorrectAction() {
        Allure.step("Отправка POST запроса с token символами разного регистра и некорректным action", () -> {
            String mixedCaseToken = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPp"; // 32 символа, смешанный регистр
            Response response = apiClient.sendPostRequestWithBodyParams(mixedCaseToken, "INCORRECT");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(31)
    @DisplayName("Отправка POST запроса с token символами разного регистра и пустым action")
    @Description("Пользователь отправляет POST запрос с token у которого значение символами разного регистра и пустой action и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testTokenMixedCaseWithEmptyAction() {
        Allure.step("Отправка POST запроса с token символами разного регистра и пустым action", () -> {
            String mixedCaseToken = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPp"; // 32 символа, смешанный регистр
            Response response = apiClient.sendPostRequestWithBodyParams(mixedCaseToken, "");
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(32)
    @DisplayName("Отправка запроса ACTION без предварительного LOGIN")
    @Description("Пользователь отправляет запрос на выполнение действия без предварительного ACTION и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testActionWithoutLogin() {
        Allure.step("Отправка запроса ACTION без предварительного LOGIN", () -> {
            Response response = apiClient.sendPostRequest(Actions.ACTION);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(33)
    @DisplayName("Отправка запроса LOGOUT без предварительного LOGIN")
    @Description("Пользователь отправляет запрос на выполнение завершения сессии (LOGOUT) без предварительного ACTION и в ответ получает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void testLogoutWithoutLogin() {
        Allure.step("Отправка запроса LOGOUT без предварительного LOGIN", () -> {
            Response response = apiClient.sendPostRequest(Actions.LOGOUT);
            response.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(34)
    @DisplayName("Отправка нескольких запросов LOGIN подряд")
    @Description("Пользователь отправляет несколько раз запрос на аутентификацию ACTION и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testMultipleLoginRequests() {
        Allure.step("Отправка нескольких запросов LOGIN подряд", () -> {
            // Первый LOGIN должен быть успешным
            Response firstResponse = apiClient.sendPostRequest(Actions.LOGIN);
            firstResponse.then()
                    .assertThat()
                    .statusCode(StatusCodes.OK);

            // Второй LOGIN должен вернуть ошибку
            Response secondResponse = apiClient.sendPostRequest(Actions.LOGIN);
            secondResponse.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }

    @Test
    @Order(35)
    @DisplayName("Отправка нескольких запросов LOGOUT после LOGIN")
    @Description("Пользователь успешно аутентифицируется и несколько раз отправляет запрос на завершение сессии (LOGOUT) и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testMultipleLogoutRequests() {
        Allure.step("Выполнение LOGIN", () -> {
            Response loginResponse = apiClient.sendPostRequest(Actions.LOGIN);
            loginResponse.then()
                    .assertThat()
                    .statusCode(StatusCodes.OK);
        });

        Allure.step("Отправка первого LOGOUT", () -> {
            Response firstLogoutResponse = apiClient.sendPostRequest(Actions.LOGOUT);
            firstLogoutResponse.then()
                    .assertThat()
                    .statusCode(StatusCodes.OK);
        });

        Allure.step("Отправка второго LOGOUT (должен вернуть ошибку)", () -> {
            Response secondLogoutResponse = apiClient.sendPostRequest(Actions.LOGOUT);
            secondLogoutResponse.then()
                    .assertThat()
                    .statusCode(not(StatusCodes.OK));
        });
    }
}
