package api_tests;
import com.github.tomakehurst.wiremock.WireMockServer;
import configs.WireMockConfig;
import configs.WireMockExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(WireMockExtension.class)
public abstract class BaseApiTest {
    @BeforeAll
    static void setupWireMockStubs() {
        WireMockServer wireMockServer = WireMockExtension.getWireMockServer();
        if (wireMockServer != null) {
            WireMockConfig.setupDefaultStubs(wireMockServer);
        }
    }
}



