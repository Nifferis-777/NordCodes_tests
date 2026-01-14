package api_tests;
import configs.LoaderConfig;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("API Тесты")
@Feature("Позитивные тесты")
@DisplayName("Позитивные тесты API c проверкой разных action")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class PositiveTests {

    private static final LoaderConfig config = LoaderConfig.getInstance();

    private static final String BASE_URL = config.getUrl();
    private static final String API_KEY = config.getApiKey();
    private static final String TOKEN = config.getToken();

    private boolean loginPerformedInTest = false;

    @AfterEach
    public void logoutAfterEachTest() {
        if (loginPerformedInTest) {
            Allure.step("Завершение сессии пользователя", () -> {
                sendPostRequest("LOGOUT");
            });
        }
        loginPerformedInTest = false;
    }

    private void sendPostRequest(String action) {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("token", TOKEN);
        bodyParams.put("action", action);

        given()
                .baseUri(BASE_URL)
                .basePath("/endpoint")
                .headers(
                        "Content-Type", "application/x-www-form-urlencoded",
                        "X-Api-Key", API_KEY,
                        "Accept", "application/json"
                )
                .formParams(bodyParams)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(200)
                .body("result", equalTo("OK"));

    }

    private void performLogin() {
        sendPostRequest("LOGIN");
        loginPerformedInTest = true;
    }

    @Test
    @Order(1)
    @DisplayName("Успешная аутентификация пользователя")
    @Description("Пользователь отправляет 'Post-запрос' с указанием корректных параметров в теле (Body) " +
            "и заголовках (Headers). В ответ возвращается json c телом успешного ответа.")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("/auth")
    void authUser() {
        sendPostRequest("LOGIN");
        loginPerformedInTest = true;
    }

    @Test
    @Order(2)
    @DisplayName("Выполнение действия")
    @Description("Пользователь успешно аутентифицируется и посылает Post-запрос на выполнение действия. " +
            "В ответ возвращается json c телом успешного ответа.")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("/doAction")
    void doAction() {
        Allure.step("Аутентификация пользователя", this::performLogin);
        Allure.step("Выполнение действия", () -> {
            sendPostRequest("ACTION");
        });
    }

    @Test
    @Order(3)
    @DisplayName("Завершение сессии пользователя")
    @Description("Пользователь успешно аутентифицируется и посылает Post-запрос на завершение сессии." +
            "В ответ возвращается json c телом успешного ответа. ")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("/logout")
    void logoutUser() {
        Allure.step("Предварительный логин", this::performLogin);
        Allure.step("Основное действие LOGOUT", () -> {
            sendPostRequest("LOGOUT");
        });
        loginPerformedInTest = false;
    }
}














