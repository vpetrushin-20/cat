package ServiceTest;

import api.models.CreateUserPayload;
import api.models.RegisterUsersPayload;
import api.models.User;

import io.restassured.response.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import steps.ReqresSteps;


public class RestTest {


    private final ReqresSteps reqresSteps = new ReqresSteps();


    /**
     * Позитивный Тест  на получения юзера с
     * существующем id и сравнение id в запросе и id в ответе GET
     */
    @ParameterizedTest
    @ValueSource(strings = {"1"})
    public void successGetUser(String id) {
        User user = reqresSteps.getUserIdSuccess(id);

        reqresSteps.checkId(user, id);
    }

    /**
     * Негативный Тест  на получение user с несуществующем id GET
     */
    @ParameterizedTest
    @ValueSource(strings = {"13"})
    public void unSuccessGetUser(String id) {
        User user = reqresSteps.getUserIdUnSuccess(id);

    }

    /**
     * Позитивный тест с успешной регистрацией пользователя и проверкой почты при регистрации
     * и почты у юзера в списке пользователей POST
     */
    @Test
    public void successRegisterUser() {
        RegisterUsersPayload payload = new RegisterUsersPayload("eve.holt@reqres.in", "pistol");
        reqresSteps.postUsersRegisterSuccess(payload);

        User user = reqresSteps.getUserIdSuccess1();

        reqresSteps.checkEmail(payload, user);
    }

    /**
     * Негативный тест с регистрацией пользователя с некорректной почтой POST
     */
    @Test
    public void unSuccessRegisterUser() {
        RegisterUsersPayload payload = new RegisterUsersPayload("kxkxkxldl@mail.ru", "pistol");
        Response response = reqresSteps.postUsersRegisterUnSuccess(payload);

    }

    /**
     * Позитивный тест с удалением пользователя с корректным id DELETE
     */
    @ParameterizedTest
    @ValueSource(strings = {"2"})
    public void successDeleteUser(String id) {
        Response response = reqresSteps.successDeleteUser(id);

    }

    /**
     * Негативный тест с удалением пользователя с некорректным id DELETE (БАГ на сайте, ожидаем ошибку - выдает 204)
     */
    @ParameterizedTest
    @ValueSource(strings = {"asdasd"})
    public void unSuccessDeleteUser(String id) {
        Response response = reqresSteps.unSuccessDeleteUser(id);
        Assertions.assertEquals(400, response.statusCode());
    }

    /**
     * Позитивный тест с обновлением данных юзера PUT
     */
    @Test
    public void successUpdatePutUser() {
        CreateUserPayload payload = new CreateUserPayload("michael", "driver");
        Response response = reqresSteps.successPutUser(payload);

        reqresSteps.checkUpdateUser(response, payload);
    }

    /**
     * Негативный тест с обновлением данных юзера
     * Оставляем поля пустыми PUT (БАГ на сайте ожидаем ошибку - выдает 200)
     */
    @Test
    public void unSuccessUpdatePutUser() {
        CreateUserPayload payload = new CreateUserPayload("", "");
        Response response = reqresSteps.unSuccessPutUser(payload);
        Assertions.assertEquals(400, response.statusCode());
    }
}
