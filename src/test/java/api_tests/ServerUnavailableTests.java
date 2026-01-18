package api_tests;
import api_tests.client.ApiClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import java.util.HashMap;
import java.util.Map;

import static api_tests.constants.ApiConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic("API-Тесты")
@Feature("Тесты недоступного сервера")
@DisplayName("Тесты недоступного сервера (отключены по умолчанию)")
@Disabled("Тесты отключены по умолчанию")
public class ServerUnavailableTests {

    private static final String TAG_NAME = "Server_unavailable_tests";
    private ApiClient apiClient;


    @BeforeEach
    void setUp() {
        apiClient = new ApiClient();
    }

    @Test
    @DisplayName("POST-запрос с корректными Headers и Body на недоступный сервер")
    @Description("Пользователь отправляет POST запрос с корректными Headers и Body на сервер который недоступен " +
            "и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testPostRequestWithCorrectHeadersAndBodyToUnavailableServer() {
        Allure.step("Отправка POST запроса с корректными Headers и Body на недоступный сервер", () -> {
            String unavailableUrl = "http://localhost:9999";
            try {
                Response response = apiClient.sendPostRequestToUrl(unavailableUrl, Actions.LOGIN);
                assertTrue(response.getStatusCode() >= 400 || response.getStatusCode() == 0,
                        "Ожидается ошибка при недоступном сервере");
            } catch (Exception e) {
                assertNotNull(e, "Должно быть исключение при недоступном сервере");
            }
        });
    }

    @Test
    @DisplayName("POST запрос с некорректными Headers и Body на недоступный сервер")
    @Description("Пользователь отправляет POST запрос с некорректными Headers и Body на сервер который недоступен " +
            "и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testPostRequestWithIncorrectHeadersAndBodyToUnavailableServer() {
        Allure.step("Отправка POST запроса с некорректными Headers и Body на недоступный сервер", () -> {
            String unavailableUrl = "http://localhost:9999";
            
            Map<String, String> incorrectHeaders = new HashMap<>();
            incorrectHeaders.put(Headers.CONTENT_TYPE, "text/plain");
            incorrectHeaders.put(Headers.API_KEY, "incorrect-key");
            incorrectHeaders.put(Headers.ACCEPT, "text/html");
            
            try {
                Response response = apiClient.sendPostRequestToUrlWithHeaders(unavailableUrl, Actions.LOGIN,
                        incorrectHeaders);
                assertTrue(response.getStatusCode() >= 400 || response.getStatusCode() == 0,
                        "Ожидается ошибка при недоступном сервере");
            } catch (Exception e) {
                assertNotNull(e, "Должно быть исключение при недоступном сервере");
            }
        });
    }

    @Test
    @DisplayName("POST запрос с пустыми Headers и Body на недоступный сервер")
    @Description("Пользователь отправляет POST запрос с пустыми Headers и Body на сервер " +
            "который недоступен и в ответ получает ошибку")
    @Severity(SeverityLevel.NORMAL)
    @Tag(TAG_NAME)
    void testPostRequestWithEmptyHeadersAndBodyToUnavailableServer() {
        Allure.step("Отправка POST запроса с пустыми Headers и Body на недоступный сервер", () -> {
            String unavailableUrl = "http://localhost:9999";
            
            Map<String, String> emptyHeaders = new HashMap<>();
            emptyHeaders.put(Headers.CONTENT_TYPE, "");
            emptyHeaders.put(Headers.API_KEY, "");
            emptyHeaders.put(Headers.ACCEPT, "");
            
            try {
                Response response = apiClient.sendPostRequestToUrlWithHeaders(unavailableUrl, Actions.LOGIN,
                        emptyHeaders);
                assertTrue(response.getStatusCode() >= 400 || response.getStatusCode() == 0,
                        "Ожидается ошибка при недоступном сервере");
            } catch (Exception e) {
                assertNotNull(e, "Должно быть исключение при недоступном сервере");
            }
        });
    }
}

