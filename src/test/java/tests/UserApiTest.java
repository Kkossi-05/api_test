package tests;

import config.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.TestDataHelper;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserApiTest extends BaseTest {

    
    // GET /users/1  →  //Kullanıcıyı ID ile getir
    
    @Test
    @DisplayName("GET /users/1 - Geçerli kullanıcı döner")
    public void testGetUserById() {
        long startTime = System.currentTimeMillis();

        Response response =
            given()
                .accept(ContentType.JSON)
            .when()
                .get("/users/" + TestDataHelper.getValidUserId())
            .then()
                // ✅ 1) Status Code Kontrolü
                .statusCode(200)
                // ✅ 2) Response Body Kontrolleri
                .body("id",       equalTo(1))
                .body("name",     equalTo("Leanne Graham"))
                .body("username", equalTo("Bret"))
                .body("email",    notNullValue())
                .body("email",    containsString("@"))
                .extract().response();

        // 3) Yanıt Süresi Kontrolü
        long elapsed = System.currentTimeMillis() - startTime;
        assertThat("Yanıt süresi " + getTimeout() + "ms altında olmalı",
                elapsed, lessThan(getTimeout()));

        System.out.println("Dönen kullanıcı: " + response.jsonPath().getString("name"));
    }

    // ──────────────────────────────────────────────
    // GET /users  →  Tüm kullanıcıları getir
    // ──────────────────────────────────────────────
    @Test
    @DisplayName("GET /users - Kullanıcı listesi döner")
    public void testGetAllUsers() {
        long startTime = System.currentTimeMillis();

        given()
            .accept(ContentType.JSON)
        .when()
            .get("/users")
        .then()
            // ✅ Status Code
            .statusCode(200)
            // ✅ Body: liste boş olmamalı
            .body("",        hasSize(greaterThan(0)))
            // ✅ Body: her kullanıcıda id alanı olmalı
            .body("id",      everyItem(notNullValue()))
            .body("email",   everyItem(notNullValue()));

        // ✅ Yanıt Süresi
        long elapsed = System.currentTimeMillis() - startTime;
        assertThat(elapsed, lessThan(getTimeout()));
    }

    // ──────────────────────────────────────────────
    // GET /users/9999  →  Geçersiz ID - 404 beklenir
    // ──────────────────────────────────────────────
    @Test
    @DisplayName("GET /users/9999 - Geçersiz ID için 404 döner")
    public void testGetUserByInvalidId() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/users/" + TestDataHelper.getInvalidUserId())
        .then()
            // ✅ 404 Not Found bekliyoruz
            .statusCode(404);
    }
}