package configs;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


public class WireMockExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private static final Logger logger = LoggerFactory.getLogger(WireMockExtension.class);
    private static final int WIREMOCK_PORT = 8888;
    private static WireMockServer wireMockServer;
    private static volatile boolean serverStarted = false;

    @Override
    public void beforeAll(ExtensionContext context) {
        synchronized (WireMockExtension.class) {
            if (!serverStarted && (wireMockServer == null || !wireMockServer.isRunning())) {
                logger.info("Запуск WireMock сервера на порту: {}", WIREMOCK_PORT);
                wireMockServer = new WireMockServer(
                        wireMockConfig()
                                .port(WIREMOCK_PORT)
                );
                wireMockServer.start();
                serverStarted = true;
                logger.info("WireMock сервер успешно запущен на http://localhost:{}", WIREMOCK_PORT);
            }
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == WireMockServer.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return wireMockServer;
    }

    public static WireMockServer getWireMockServer() {
        return wireMockServer;
    }


    public static void stop() {
        synchronized (WireMockExtension.class) {
            if (wireMockServer != null && wireMockServer.isRunning()) {
                logger.info("Остановка WireMock сервера");
                wireMockServer.stop();
                wireMockServer = null;
                serverStarted = false;
            }
        }
    }
}



