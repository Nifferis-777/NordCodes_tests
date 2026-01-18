package configs;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockConfig {

    private static final Logger logger = LoggerFactory.getLogger(WireMockConfig.class);
    private static final String AUTH_ENDPOINT = "/auth";
    private static final String DO_ACTION_ENDPOINT = "/doAction";


    public static void setupSuccessStubs(WireMockServer wireMockServer) {
        logger.info("Настройка стабов для успешных ответов");

        // Стаб для /endpoint с action=LOGIN - успешный ответ
        wireMockServer.stubFor(post(urlEqualTo("/endpoint"))
                .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
                .withHeader("Accept", containing("application/json"))
                .withHeader("X-Api-Key", equalTo("qazWSXedc"))
                .withRequestBody(containing("action=LOGIN"))
                .withRequestBody(matching("token=.+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"result\":\"OK\"}")));

        // Стаб для /endpoint с action=LOGOUT - успешный ответ
        wireMockServer.stubFor(post(urlEqualTo("/endpoint"))
                .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
                .withHeader("Accept", containing("application/json"))
                .withHeader("X-Api-Key", equalTo("qazWSXedc"))
                .withRequestBody(containing("action=LOGOUT"))
                .withRequestBody(matching("token=.+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"result\":\"OK\"}")));

        // Стаб для /endpoint с action=ACTION (после LOGIN) - успешный ответ
        wireMockServer.stubFor(post(urlEqualTo("/endpoint"))
                .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
                .withHeader("Accept", containing("application/json"))
                .withHeader("X-Api-Key", equalTo("qazWSXedc"))
                .withRequestBody(containing("action=ACTION"))
                .withRequestBody(matching("token=.+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"result\":\"OK\"}")));

        // Оставляем старые стабы для обратной совместимости
        wireMockServer.stubFor(post(urlEqualTo(AUTH_ENDPOINT))
                .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
                .withHeader("Accept", containing("application/json"))
                .withRequestBody(matching("token=.+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"success\"}")));

        wireMockServer.stubFor(post(urlEqualTo(DO_ACTION_ENDPOINT))
                .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
                .withHeader("Accept", containing("application/json"))
                .withRequestBody(matching("token=.+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"success\"}")));

        logger.info("Стабы для успешных ответов настроены");
    }


    public static void setupAuthFailureStub(WireMockServer wireMockServer) {
        logger.info("Настройка стаба для неуспешной аутентификации");

        wireMockServer.stubFor(post(urlEqualTo(AUTH_ENDPOINT))
                .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
                .withHeader("Accept", containing("application/json"))
                .withRequestBody(matching("token=.+"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Unauthorized\"}")));

        logger.info("Стаб для неуспешной аутентификации настроен");
    }


    public static void setupDoActionFailureStub(WireMockServer wireMockServer) {
        logger.info("Настройка стаба для неуспешного выполнения действия");

        wireMockServer.stubFor(post(urlEqualTo(DO_ACTION_ENDPOINT))
                .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
                .withHeader("Accept", containing("application/json"))
                .withRequestBody(matching("token=.+"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Internal Server Error\"}")));

        logger.info("Стаб для неуспешного выполнения действия настроен");
    }

    public static void setupTimeoutStub(WireMockServer wireMockServer, String endpoint) {
        logger.info("Настройка стаба для таймаута на эндпоинте: {}", endpoint);

        wireMockServer.stubFor(post(urlEqualTo(endpoint))
                .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
                .withHeader("Accept", containing("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withFixedDelay(10000) // Задержка 10 секунд
                        .withBody("{\"status\":\"success\"}")));

        logger.info("Стаб для таймаута настроен на эндпоинте: {}", endpoint);
    }

    public static void resetStubs(WireMockServer wireMockServer) {
        logger.info("Сброс всех стабов WireMock");
        wireMockServer.resetAll();
        setupSuccessStubs(wireMockServer);
    }

    public static void setupDefaultStubs(WireMockServer wireMockServer) {
        setupSuccessStubs(wireMockServer);
        setupNegativeStubs(wireMockServer);
    }


    public static void setupNegativeStubs(WireMockServer wireMockServer) {
        logger.info("Настройка стабов для негативных сценариев");

        // Стаб для некорректного метода (GET, PUT, DELETE и т.д.) - ошибка 405 Method Not Allowed
        wireMockServer.stubFor(get(urlPathMatching("/endpoint.*"))
                .willReturn(aResponse()
                        .withStatus(405)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Method Not Allowed\"}")));

        wireMockServer.stubFor(put(urlPathMatching("/endpoint.*"))
                .willReturn(aResponse()
                        .withStatus(405)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Method Not Allowed\"}")));

        wireMockServer.stubFor(delete(urlPathMatching("/endpoint.*"))
                .willReturn(aResponse()
                        .withStatus(405)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Method Not Allowed\"}")));

        // Стаб для некорректного URL - ошибка 404 Not Found
        wireMockServer.stubFor(post(urlPathMatching("/incorrect.*"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Not Found\"}")));

        // Стаб для пустого URL - ошибка 404 Not Found
        wireMockServer.stubFor(post(urlPathMatching("/"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Not Found\"}")));

        // Стаб для запросов без X-Api-Key заголовка или с некорректным - ошибка 401 Unauthorized
        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withHeader("X-Api-Key", equalTo("incorrect-key"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Unauthorized\"}")));

        // Стаб для запросов с пустым X-Api-Key - ошибка 401 Unauthorized
        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withHeader("X-Api-Key", equalTo(""))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Unauthorized\"}")));

        // Стаб для запросов без заголовков - ошибка 400 Bad Request
        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withHeader("Content-Type", equalTo(""))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Bad Request\"}")));

        // Стаб для запросов с пустым token - ошибка 400 Bad Request
        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withRequestBody(containing("token=&action="))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Bad Request\"}")));

        // Стаб для запросов с пустым action - ошибка 400 Bad Request
        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withRequestBody(containing("token=AAAABBBBCCCCDDDDEEEEFFFF12345678&action="))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Bad Request\"}")));

        // Стаб для запросов с некорректным token - ошибка 401 Unauthorized
        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withRequestBody(matching("token=incorrect.*"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Unauthorized\"}")));

        // Стаб для запросов с некорректным action - ошибка 400 Bad Request
        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withRequestBody(matching(".*action=INCORRECT.*"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Bad Request\"}")));

        // Стаб для запросов ACTION без предварительного LOGIN - ошибка 401 Unauthorized
        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withRequestBody(containing("action=ACTION"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Unauthorized\"}")));

        // Стаб для запросов LOGOUT без предварительного LOGIN - ошибка 401 Unauthorized
        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withRequestBody(containing("action=LOGOUT"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Unauthorized\"}")));

        logger.info("Стабы для негативных сценариев настроены");
    }
}

