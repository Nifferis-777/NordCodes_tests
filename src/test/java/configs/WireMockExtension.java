package configs;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.extension.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WireMockExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {
    private static final int WIREMOCK_PORT = 8888;
    private static WireMockServer wireMockServer;
    private static volatile boolean serverStarted = false;

    @Override
    public void beforeAll(ExtensionContext context) {
        synchronized (WireMockExtension.class) {
            if (!serverStarted && (wireMockServer == null || !wireMockServer.isRunning())) {
                wireMockServer = new WireMockServer(
                        wireMockConfig()
                                .port(WIREMOCK_PORT)
                );
                wireMockServer.start();
                serverStarted = true;
            }
        }
    }

    @Override
    public void afterAll(ExtensionContext context) { }

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

}



