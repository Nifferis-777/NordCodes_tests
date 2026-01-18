package configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoaderConfig {

    private static final Logger logger = LoggerFactory.getLogger(LoaderConfig.class);
    private static final String CONFIG_FILE = "test_params.properties";
    private static volatile LoaderConfig instance;
    private final Properties properties;

    private LoaderConfig() {
        properties = new Properties();
        loadProperties();
    }


    public static LoaderConfig getInstance() {
        if (instance == null) {
            synchronized (LoaderConfig.class) {
                if (instance == null) {
                    instance = new LoaderConfig();
                }
            }
        }
        return instance;
    }


    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                String errorMsg = String.format("Не удалось найти файл конфигурации: %s", CONFIG_FILE);
                logger.error(errorMsg);
                throw new IllegalStateException(errorMsg);
            }
            properties.load(input);
            logger.info("Конфигурация успешно загружена из файла: {}", CONFIG_FILE);
            validateProperties();
        } catch (IOException ex) {
            String errorMsg = String.format("Ошибка при загрузке конфигурации из файла: %s", CONFIG_FILE);
            logger.error(errorMsg, ex);
            throw new IllegalStateException(errorMsg, ex);
        }
    }


    private void validateProperties() {
        String[] requiredProperties = {"URL", "API_KEY", "TOKEN"};
        for (String property : requiredProperties) {
            if (properties.getProperty(property) == null || properties.getProperty(property).trim().isEmpty()) {
                String errorMsg = String.format("Обязательное свойство '%s' отсутствует или пусто в файле конфигурации", property);
                logger.error(errorMsg);
                throw new IllegalStateException(errorMsg);
            }
        }
    }


    public String getUrl() {
        return getRequiredProperty("URL");
    }

    public String getApiKey() {
        return getRequiredProperty("API_KEY");
    }

    public String getToken() {
        return getRequiredProperty("TOKEN");
    }

    private String getRequiredProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(String.format("Свойство '%s' отсутствует или пусто", key));
        }
        return value.trim();
    }
}
