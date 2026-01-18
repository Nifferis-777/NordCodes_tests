package api_tests;

import api_tests.client.ApiClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static api_tests.constants.ApiConstants.*;
import static org.hamcrest.Matchers.equalTo;

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
                Response response = apiClient.sendPostRequest(Actions.LOGOUT);
                Allure.step("Валидация ответа", () -> {
                    response.then()
                            .assertThat()
                            .statusCode(StatusCodes.OK)
                            .body(ResponseFields.RESULT, equalTo(ResponseFields.OK));
                });
            });
        }
        loginPerformedInTest = false;
    }

    private void performLogin() {
        Allure.step("Аутентификация пользователя", () -> {
            Response response = apiClient.sendPostRequest(Actions.LOGIN);
            loginPerformedInTest = true;
            Allure.step("Валидация ответа", () -> {
                response.then()
                        .assertThat()
                        .statusCode(StatusCodes.OK)
                        .body(ResponseFields.RESULT, equalTo(ResponseFields.OK));
            });
        });
    }

    @Test
    @Order(1)
    @DisplayName("Успешная аутентификация пользователя")
    @Description(SUCCESSAUTH_DESCRIPTION)
    @Severity(SeverityLevel.CRITICAL)
    @Tag(TAG_NAME)
    void authUser() {
        Allure.step("Аутентификация пользователя", () -> {
            Response response = apiClient.sendPostRequest(Actions.LOGIN);
            loginPerformedInTest = true;
            Allure.step("Валидация ответа", () -> {
                response.then()
                        .assertThat()
                        .statusCode(StatusCodes.OK)
                        .body(ResponseFields.RESULT, equalTo(ResponseFields.OK));
            });
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
            Response response = apiClient.sendPostRequest(Actions.ACTION);
            Allure.step("Валидация ответа", () -> {
                response.then()
                        .assertThat()
                        .statusCode(StatusCodes.OK)
                        .body(ResponseFields.RESULT, equalTo(ResponseFields.OK));
            });
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
                Allure.step(String.format("Выполнение действия №%d", attemptNumber), () -> {
                    Response response = apiClient.sendPostRequest(Actions.ACTION);
                    Allure.step("Валидация ответа", () -> {
                        response.then()
                                .assertThat()
                                .statusCode(StatusCodes.OK)
                                .body(ResponseFields.RESULT, equalTo(ResponseFields.OK));
                    });
                });
            }
        });
        Allure.step("Завершение сессии пользователя", () -> {
            Response response = apiClient.sendPostRequest(Actions.LOGOUT);
            loginPerformedInTest = false;
            Allure.step("Валидация ответа", () -> {
                response.then()
                        .assertThat()
                        .statusCode(StatusCodes.OK)
                        .body(ResponseFields.RESULT, equalTo(ResponseFields.OK));
            });
        });
    }
}














