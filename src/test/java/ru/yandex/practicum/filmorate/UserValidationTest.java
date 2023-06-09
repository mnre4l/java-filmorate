package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Marker;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserValidationTest {
    User user;

    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @BeforeEach
    public void createCorrectUserObject() {
        user = new User();
        user.setName("User name");
        user.setEmail("email@mail.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1996, 8, 17));
    }

    @Test
    public void shouldCreateUserDefault() {
        Set<ConstraintViolation<User>> validates = validator.validate(user, Marker.OnCreate.class);
        System.out.println(validates);

        assertEquals(validates.size(), 0);
    }

    @Test
    public void shouldNotCreateUserIfNotNullId() {
        user.setId(1);

        Set<ConstraintViolation<User>> validates = validator.validate(user, Marker.OnCreate.class);

        assertEquals(validates.size(), 1);
    }

    @Test
    public void shouldNotUpdateUserIfNegativeId() {
        user.setId(-1);

        Set<ConstraintViolation<User>> validates = validator.validate(user, Marker.OnUpdate.class);

        assertEquals(validates.size(), 1);
    }

    @Test
    public void shouldNotCreateUserWithEmptyEmail() {
        user.setEmail("");

        Set<ConstraintViolation<User>> validates = validator.validate(user);

        assertEquals(validates.size(), 0);
    }

    @Test
    public void shouldNotCreateUserWithNullEmail() {
        user.setEmail(null);

        Set<ConstraintViolation<User>> validates = validator.validate(user);

        assertEquals(validates.size(), 1);
    }

    @Test
    public void shouldNotCreateUserWithBadEmail() {
        user.setEmail("@yandex.mail.ru");

        Set<ConstraintViolation<User>> validates = validator.validate(user);

        assertEquals(validates.size(), 1);
    }

    @Test
    public void shouldNotCreateUserWithBlankLogin() {
        user.setLogin("");

        Set<ConstraintViolation<User>> validates = validator.validate(user);

        assertEquals(validates.size(), 1);
    }

    @Test
    public void shouldNotCreateUserWithBadBirthday() {
        user.setBirthday(LocalDate.of(2030, 5, 2));

        Set<ConstraintViolation<User>> validates = validator.validate(user);

        assertEquals(validates.size(), 1);
    }
}
