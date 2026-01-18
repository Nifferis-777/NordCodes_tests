package api_tests.constants;


public final class ApiConstants {

    private ApiConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final class Actions {
        public static final String LOGIN = "LOGIN";
        public static final String LOGOUT = "LOGOUT";
        public static final String ACTION = "ACTION";

        private Actions() {
            throw new UnsupportedOperationException("Utility class");
        }
    }

    public static final class Endpoints {
        public static final String BASE_ENDPOINT = "/endpoint";

        private Endpoints() {
            throw new UnsupportedOperationException("Utility class");
        }
    }

    public static final class Headers {
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String API_KEY = "X-Api-Key";
        public static final String ACCEPT = "Accept";
        public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
        public static final String APPLICATION_JSON = "application/json";

        private Headers() {
            throw new UnsupportedOperationException("Utility class");
        }
    }

    public static final class RequestParams {
        public static final String TOKEN = "token";
        public static final String ACTION = "action";

        private RequestParams() {
            throw new UnsupportedOperationException("Utility class");
        }
    }

    public static final class ResponseFields {
        public static final String RESULT = "result";
        public static final String OK = "OK";

        private ResponseFields() {
            throw new UnsupportedOperationException("Utility class");
        }
    }

    public static final class StatusCodes {
        public static final int OK = 200;
        public static final int BAD_REQUEST = 400;
        public static final int UNAUTHORIZED = 401;
        public static final int FORBIDDEN = 403;
        public static final int NOT_FOUND = 404;
        public static final int INTERNAL_SERVER_ERROR = 500;

        private StatusCodes() {
            throw new UnsupportedOperationException("Utility class");
        }
    }
}



