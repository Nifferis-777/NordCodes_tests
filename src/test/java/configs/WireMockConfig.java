package configs;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockConfig {

    private static final String AUTH_ENDPOINT = "/auth";
    private static final String DO_ACTION_ENDPOINT = "/doAction";

    public static void setupSuccessStubs(WireMockServer wireMockServer) {
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

    }

    public static void setupDefaultStubs(WireMockServer wireMockServer) {
        setupSuccessStubs(wireMockServer);
        setupNegativeStubs(wireMockServer);
    }

    public static void setupNegativeStubs(WireMockServer wireMockServer) {
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

        wireMockServer.stubFor(post(urlPathMatching("/incorrect.*"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Not Found\"}")));

        wireMockServer.stubFor(post(urlPathMatching("/"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Not Found\"}")));

        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withHeader("X-Api-Key", equalTo("incorrect-key"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Unauthorized\"}")));

        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withHeader("X-Api-Key", equalTo(""))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Unauthorized\"}")));

        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withHeader("Content-Type", equalTo(""))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Bad Request\"}")));

        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withRequestBody(containing("token=&action="))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Bad Request\"}")));

        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withRequestBody(containing("token=AAAABBBBCCCCDDDDEEEEFFFF12345678&action="))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Bad Request\"}")));

        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withRequestBody(matching("token=incorrect.*"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Unauthorized\"}")));

        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withRequestBody(matching(".*action=INCORRECT.*"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Bad Request\"}")));

        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withRequestBody(containing("action=ACTION"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Unauthorized\"}")));

        wireMockServer.stubFor(post(urlPathMatching("/endpoint.*"))
                .withRequestBody(containing("action=LOGOUT"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Unauthorized\"}")));
    }
}

