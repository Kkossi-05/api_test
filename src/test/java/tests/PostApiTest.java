package tests;

import config.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.TestDataHelper;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PostApiTest extends BaseTest {

    // ──────────────────────────────────────────────
    // POST /posts  →  Yeni gönderi oluştur (Request Body ile)
    // ──────────────────────────────────────────────
    @Test
    @DisplayName("POST /posts - Yeni gönderi oluşturulur (201 Created)")
    public void testCreatePost() {
        Map<String, Object> requestBody = TestDataHelper.getDefaultPostBody();

        long startTime = System.currentTimeMillis();

        Response response =
            given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
            .when()
                .post("/posts")
            .then()
                // ✅ 1) Status Code: 201 Created
                .statusCode(201)
                // ✅ 2) Response Body Kontrolleri
                .body("title",  equalTo("Test Başlığı"))
                .body("body",   equalTo("Bu bir otomatik test gönderisidir."))
                .body("userId", equalTo(1))
                .body("id",     notNullValue())
                .extract().response();

        // ✅ 3) Yanıt Süresi Kontrolü
        long elapsed = System.currentTimeMillis() - startTime;
        assertThat("Yanıt süresi " + getTimeout() + "ms altında olmalı",
                elapsed, lessThan(getTimeout()));

        int createdId = response.jsonPath().getInt("id");
        System.out.println("Oluşturulan post ID: " + createdId);
    }

    // ──────────────────────────────────────────────
    // GET /posts/1  →  Gönderiyi getir
    // ──────────────────────────────────────────────
    @Test
    @DisplayName("GET /posts/1 - Gönderi detayı döner")
    public void testGetPostById() {
        long startTime = System.currentTimeMillis();

        given()
            .accept(ContentType.JSON)
        .when()
            .get("/posts/1")
        .then()
            // ✅ Status Code
            .statusCode(200)
            // ✅ Body
            .body("id",     equalTo(1))
            .body("userId", notNullValue())
            .body("title",  not(emptyString()))
            .body("body",   not(emptyString()));

        // ✅ Yanıt Süresi
        long elapsed = System.currentTimeMillis() - startTime;
        assertThat(elapsed, lessThan(getTimeout()));
    }

    // ──────────────────────────────────────────────
    // POST /posts  →  REST Assured'un kendi .time() metodu ile
    // ──────────────────────────────────────────────
    @Test
    @DisplayName("POST /posts - .time() ile yanıt süresi kontrolü")
    public void testCreatePostWithBuiltInTimer() {
        Map<String, Object> requestBody = TestDataHelper.createPostBody(
                "Farklı Başlık", "Farklı içerik.", 2
        );

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .body("userId", equalTo(2))
            // ✅ REST Assured'un built-in zaman kontrolü
            .time(lessThan(getTimeout()));
    }
}