package api_tests;

import api_tests.client.ApiClient;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;


@Epic("API-Тесты")
@Feature("Позитивные тесты")
@DisplayName("Позитивные API-тесты c проверкой разных action")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PositiveTests extends BaseApiTest {

    private static final String TAG_NAME = "Positive_tests";

    private static final String SUCCESSAUTH_DESCRIPTION = "Пользователь отправляет POST-запрос с указанием " +
            "корректных параметров в теле (Body) и заголовках (Headers). В ответ возвращается JSON " +
            "с телом успешного ответа (result: OK). Происходит завершение сессии пользователя (logout)";

    private static final String DO_ACTION_DESCRIPTION = "Пользователь успешно аутентифицируется, совершает действие." +
            " В ответ возвращается JSON с телом успешного ответа (result: OK). " +
            "Происходит завершение сессии пользователя (logout)";

    private static final String MANY_DO_ACTION_DESCRIPTION = "Пользователь успешно аутентифицируется, совершает " +
            "действие несколько раз. В ответ возвращается JSON с телом успешного ответа (result: OK).Происходит " +
            "завершение сессии пользователя (logout)";

    private ApiClient apiClient;
    private boolean loginPerformedInTest = false;

    @BeforeEach
    void setUp() {
        apiClient = new ApiClient();
    }

    @AfterEach
    void tearDown() {
        if (loginPerformedInTest) {
            Allure.step("Завершение сессии пользователя", () -> {
                apiClient.logout();
            });
        }
        loginPerformedInTest = false;
    }

    private void performLogin() {
        Allure.step("Аутентификация пользователя", () -> {
            apiClient.login();
            loginPerformedInTest = true;
        });
    }

    @Test
    @Order(1)
    @DisplayName("Успешная аутентификация пользователя")
    @Description(SUCCESSAUTH_DESCRIPTION)
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void authUser() {
        Allure.step("Выполнение логина", () -> {
            apiClient.login();
            loginPerformedInTest = true;
        });
    }

    @Test
    @Order(2)
    @DisplayName("Выполнение действия пользователем")
    @Description(DO_ACTION_DESCRIPTION)
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void doAction() {
        performLogin();
        Allure.step("Выполнение действия", () -> {
            apiClient.doAction();
        });
    }

    @Test
    @Order(3)
    @DisplayName("Выполнение действия пользователем несколько раз")
    @Description(MANY_DO_ACTION_DESCRIPTION)
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void manyDoAction() {
        performLogin();
        Allure.step("Выполнение действия несколько раз", () -> {
            int numberOfAttempts = 3;
            for (int i = 1; i <= numberOfAttempts; i++) {
                final int attemptNumber = i;
                Allure.step(String.format("Попытка выполнения действия №%d", attemptNumber), () -> {
                    apiClient.doAction();
                });
            }
        });
        Allure.step("Завершение сессии пользователя", () -> {
            apiClient.logout();
            loginPerformedInTest = false;
        });
    }
}














