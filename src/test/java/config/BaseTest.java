package config;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BaseTest {

    protected static Properties props = new Properties();

    @BeforeAll
    public static void setup() throws IOException {
        // test.properties dosyasını yükle
        InputStream input = BaseTest.class
                .getClassLoader()
                .getResourceAsStream("test.properties");
        props.load(input);

        // REST Assured global ayarları
        RestAssured.baseURI = props.getProperty("base.url");

        // Her istek ve yanıtı konsola yaz (debug için)
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }

    protected long getTimeout() {
        return Long.parseLong(props.getProperty("response.timeout.ms", "3000"));
    }
}