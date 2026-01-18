package api_tests.client;

import configs.LoaderConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import static api_tests.constants.ApiConstants.*;
import static io.restassured.RestAssured.given;

public class ApiClient {

    private static final LoaderConfig config = LoaderConfig.getInstance();
    private final String baseUrl;
    private final String apiKey;
    private final String token;

    public ApiClient() {
        this.baseUrl = config.getUrl();
        this.apiKey = config.getApiKey();
        this.token = config.getToken();
    }

    public Response sendPostRequest(String action) {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put(RequestParams.TOKEN, token);
        bodyParams.put(RequestParams.ACTION, action);

        RequestSpecification request = given()
                .filter(new AllureRestAssured())
                .baseUri(baseUrl)
                .basePath(Endpoints.BASE_ENDPOINT)
                .headers(
                        Headers.CONTENT_TYPE, Headers.APPLICATION_FORM_URLENCODED,
                        Headers.API_KEY, apiKey,
                        Headers.ACCEPT, Headers.APPLICATION_JSON
                )
                .formParams(bodyParams);
        Response response = request.post();
        return response;
    }

    public Response sendPostRequestWithSuccessValidation(String action) {
        Response response = sendPostRequest(action);
        response.then()
                .assertThat()
                .statusCode(StatusCodes.OK)
                .body(ResponseFields.RESULT, org.hamcrest.Matchers.equalTo(ResponseFields.OK));
        return response;
    }

    public Response sendRequest(String method, String action) {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put(RequestParams.TOKEN, token);
        bodyParams.put(RequestParams.ACTION, action);
        RequestSpecification request = given()
                .filter(new AllureRestAssured())
                .baseUri(baseUrl)
                .basePath(Endpoints.BASE_ENDPOINT)
                .headers(
                        Headers.CONTENT_TYPE, Headers.APPLICATION_FORM_URLENCODED,
                        Headers.API_KEY, apiKey,
                        Headers.ACCEPT, Headers.APPLICATION_JSON
                )
                .formParams(bodyParams);
        Response response;
        switch (method.toUpperCase()) {
            case "GET":
                response = request.get();
                break;
            case "PUT":
                response = request.put();
                break;
            case "DELETE":
                response = request.delete();
                break;
            case "PATCH":
                response = request.patch();
                break;
            default:
                response = request.post();
        }
        return response;
    }

    public Response sendPostRequestToUrl(String url, String action) {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put(RequestParams.TOKEN, token);
        bodyParams.put(RequestParams.ACTION, action);
        RequestSpecification request = given()
                .filter(new AllureRestAssured())
                .baseUri(url)
                .basePath(Endpoints.BASE_ENDPOINT)
                .headers(
                        Headers.CONTENT_TYPE, Headers.APPLICATION_FORM_URLENCODED,
                        Headers.API_KEY, apiKey,
                        Headers.ACCEPT, Headers.APPLICATION_JSON
                )
                .formParams(bodyParams);
        Response response = request.post();
        return response;
    }

    public Response sendPostRequestWithHeaders(String action, Map<String, String> headers) {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put(RequestParams.TOKEN, token);
        bodyParams.put(RequestParams.ACTION, action);
        RequestSpecification request = given()
                .filter(new AllureRestAssured())
                .baseUri(baseUrl)
                .basePath(Endpoints.BASE_ENDPOINT);
        if (headers != null && !headers.isEmpty()) {
            request.headers(headers);
        }
        request.formParams(bodyParams);
        Response response = request.post();
        return response;
    }

    public Response sendPostRequestWithBodyParams(String tokenValue, String actionValue) {
        Map<String, String> bodyParams = new HashMap<>();
        if (tokenValue != null) {
            bodyParams.put(RequestParams.TOKEN, tokenValue);
        }
        if (actionValue != null) {
            bodyParams.put(RequestParams.ACTION, actionValue);
        }
        RequestSpecification request = given()
                .filter(new AllureRestAssured())
                .baseUri(baseUrl)
                .basePath(Endpoints.BASE_ENDPOINT)
                .headers(
                        Headers.CONTENT_TYPE, Headers.APPLICATION_FORM_URLENCODED,
                        Headers.API_KEY, apiKey,
                        Headers.ACCEPT, Headers.APPLICATION_JSON
                )
                .formParams(bodyParams);
        Response response = request.post();
        return response;
    }

    public Response sendPostRequestToUrlWithHeaders(String url, String action, Map<String, String> headers) {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put(RequestParams.TOKEN, token);
        bodyParams.put(RequestParams.ACTION, action);
        RequestSpecification request = given()
                .filter(new AllureRestAssured())
                .baseUri(url)
                .basePath(Endpoints.BASE_ENDPOINT);
        if (headers != null && !headers.isEmpty()) {
            request.headers(headers);
        }
        request.formParams(bodyParams);
        Response response = request.post();
        return response;
    }
}

