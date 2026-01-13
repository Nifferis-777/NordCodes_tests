package configs;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoaderConfig {

    private static volatile LoaderConfig instance;
    private final Properties properties;

    private LoaderConfig() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("test_params.properties")) {
            if (input == null) {
                System.out.println("Не удалось найти test_params.properties");
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

    public String getUrl() {
        return properties.getProperty("URL");
    }

}
