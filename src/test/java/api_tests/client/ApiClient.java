package api_tests.client;

import configs.LoaderConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static api_tests.constants.ApiConstants.*;
import static io.restassured.RestAssured.given;


public class ApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
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
        logger.debug("Отправка POST запроса с действием: {}", action);

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

        logger.debug("Получен ответ со статусом: {} для действия: {}", response.getStatusCode(), action);
        return response;
    }


    public Response sendPostRequestWithSuccessValidation(String action) {
        Response response = sendPostRequest(action);
        response.then()
                .assertThat()
                .statusCode(StatusCodes.OK)
                .body(ResponseFields.RESULT, org.hamcrest.Matchers.equalTo(ResponseFields.OK));

        logger.info("Успешно выполнено действие: {}", action);
        return response;
    }


    public Response login() {
        logger.info("Выполнение логина пользователя");
        return sendPostRequestWithSuccessValidation(Actions.LOGIN);
    }


    public Response logout() {
        logger.info("Выполнение логаута пользователя");
        return sendPostRequestWithSuccessValidation(Actions.LOGOUT);
    }


    public Response doAction() {
        logger.info("Выполнение действия ACTION");
        return sendPostRequestWithSuccessValidation(Actions.ACTION);
    }


    public Response sendRequest(String method, String action) {
        logger.debug("Отправка {} запроса с действием: {}", method, action);

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

        logger.debug("Получен ответ со статусом: {} для метода: {} и действия: {}", 
                     response.getStatusCode(), method, action);
        return response;
    }


    public Response sendPostRequestToUrl(String url, String action) {
        logger.debug("Отправка POST запроса на URL: {} с действием: {}", url, action);

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
        logger.debug("Получен ответ со статусом: {} для URL: {}", response.getStatusCode(), url);
        return response;
    }


    public Response sendPostRequestWithHeaders(String action, Map<String, String> headers) {
        logger.debug("Отправка POST запроса с кастомными заголовками и действием: {}", action);

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
        logger.debug("Получен ответ со статусом: {} для действия: {}", response.getStatusCode(), action);
        return response;
    }


    public Response sendPostRequestWithBodyParams(String tokenValue, String actionValue) {
        logger.debug("Отправка POST запроса с token: {} и action: {}", tokenValue, actionValue);

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
        logger.debug("Получен ответ со статусом: {} для token: {} и action: {}", 
                     response.getStatusCode(), tokenValue, actionValue);
        return response;
    }


    public Response sendPostRequestToUrlWithHeaders(String url, String action, Map<String, String> headers) {
        logger.debug("Отправка POST запроса на URL: {} с кастомными заголовками и действием: {}", url, action);

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
        logger.debug("Получен ответ со статусом: {} для URL: {}", response.getStatusCode(), url);
        return response;
    }
}

