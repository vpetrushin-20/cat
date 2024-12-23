package steps;

import api.models.CreateUserPayload;

import api.models.RegisterUsersPayload;
import api.models.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;

public class ReqresSteps {


    /**
     * Первый запрос get на получение пользователя с корректным id
     * ожидаемый статус - 200
     */
    public static User getUserIdSuccess(String id) {

        return given()
                .spec(SpecHelper.getRequestSpec())
                .when()
                .get(String.format("users/%s", id))
                .then()
                .spec(SpecHelper.getResponseSpec(200))
                .extract().body().jsonPath().getObject("data", User.class);
    }

    /**
     * запрос необходимый для реализации проверки в первом запросе post
     * ожидаемый статус - 200
     */
    public static User getUserIdSuccess1() {

        return given()
                .spec(SpecHelper.getRequestSpec())
                .when()
                .get("users/4")
                .then()
                .spec(SpecHelper.getResponseSpec(200))
                .extract().body().jsonPath().getObject("data", User.class);
    }

    /**
     * Второй запрос get на получение пользователя с некорректным id
     * ожидаемый статус - 404
     */
    public static User getUserIdUnSuccess(String id) {

        return given()
                .spec(SpecHelper.getRequestSpec())
                .when()
                .get(String.format("users/%s", id))
                .then()
                .spec(SpecHelper.getResponseSpec(404))
                .extract().body().jsonPath().getObject("data", User.class);
    }

    /**
     * Первый запрос post на регистрацию пользователя
     * ожидаемый статус - 200
     */
    public static Response postUsersRegisterSuccess(RegisterUsersPayload payload) {

        return given()
                .spec(SpecHelper.getRequestSpec())
                .when()
                .body(payload)
                .post("register")
                .then()
                .spec(SpecHelper.getResponseSpec(200))
                .extract().response();
    }

    /**
     * Второй запрос post на регистрацию пользователя с неккоректной почтой
     * ожидаемый статус - 400
     */
    public static Response postUsersRegisterUnSuccess(RegisterUsersPayload payload) {

        return given()
                .spec(SpecHelper.getRequestSpec())
                .when()
                .body(payload)
                .post("register")
                .then()
                .spec(SpecHelper.getResponseSpec(400))
                .extract().response();
    }

    /**
     * Первый запрос delete на удаления пользователя с правильным id
     * ожидаемый статус 204
     */
    public static Response successDeleteUser(String id) {

        return given()
                .spec(SpecHelper.getRequestSpec())
                .when()
                .delete(String.format("users/%s", id))
                .then()
                .spec(SpecHelper.getResponseSpec(204))
                .extract().response();

    }

    /**
     * Второй запрос delete на удаления пользователя с некорректынм id
     * ожидаемый статус 400
     */
    public static Response unSuccessDeleteUser(String id) {

        return given()
                .spec(SpecHelper.getRequestSpec())
                .when()
                .delete(String.format("users/%s", id))
                .then()
                .log().all()
                .extract().response();

    }


    /**
     * Первый запрос put на обновление данных юзера
     * ожидаемый статус 200
     */

    public static Response successPutUser(CreateUserPayload payload) {
        return given()
                .spec(SpecHelper.getRequestSpec())
                .when()
                .body(payload)
                .put("users/2")
                .then()
                .spec(SpecHelper.getResponseSpec(200))
                .extract().response();
    }

    /**
     * Второй запрос put на обновление данных юзера с пусстыми полями
     * ожидаемый статус 400
     */

    public static Response unSuccessPutUser(CreateUserPayload payload) {
        return given()
                .spec(SpecHelper.getRequestSpec())
                .when()
                .body(payload)
                .put("users/2")
                .then()
                .log().all()
                .extract().response();
    }


    /**
     * Проверка соответсвтия id в запросе и id в ответе
     */
    public void checkId(User user, String id) {
        String expectedId = id;
        Assertions.assertEquals(expectedId, user.getId().toString());
    }

    /**
     * Проверка соответствия почты при регистрации и почты у пользователя в списке юзеров
     */
    public void checkEmail(RegisterUsersPayload payload, User user) {
        Assertions.assertEquals(payload.getEMail(), user.getEMail());
    }


    /**
     * Проверка обновления данных после метода PUT соответсвтие данных в запросе и ответе
     */
    public void checkUpdateUser(Response response, CreateUserPayload payload) {
        Assertions.assertEquals(payload.getName(), response.jsonPath().get("name"));
        Assertions.assertEquals(payload.getJob(), response.jsonPath().get("job"));
    }

}
