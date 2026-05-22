package utils;

import java.util.HashMap;
import java.util.Map;

public class TestDataHelper {

    /**
     * POST /posts için örnek request body döner
     */
    public static Map<String, Object> createPostBody(String title, String body, int userId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", title);
        requestBody.put("body", body);
        requestBody.put("userId", userId);
        return requestBody;
    }

    /**
     * Test için varsayılan bir post body döner
     */
    public static Map<String, Object> getDefaultPostBody() {
        return createPostBody(
                "Test Başlığı",
                "Bu bir otomatik test gönderisidir.",
                1
        );
    }

    /**
     * Geçerli bir userId döner (test verisi)
     */
    public static int getValidUserId() {
        return 1;
    }

    /**
     * Geçersiz bir userId döner (negatif test için)
     */
    public static int getInvalidUserId() {
        return 9999;
    }
}