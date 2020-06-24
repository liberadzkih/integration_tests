package edu.iis.mto.blog.domain.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;
    private User fullDetailsUser;

    @Before
    public void setUp() {

        user = UserBuilder.builder()
                .firstName("Jan")
                .email("john@domain.com")
                .accountStatus(AccountStatus.NEW)
                .build();

        fullDetailsUser = UserBuilder.builder()
                .accountStatus(AccountStatus.NEW)
                .email("215920@edu.p.lodz.pl")
                .firstName("Dawid")
                .lastName("Witaszek")
                .build();

    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        assertThat(users, hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        assertThat(users, hasSize(1));
        assertThat(users.get(0)
                        .getEmail(),
                equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), notNullValue());
    }

    @Test
    public void shouldFindUserByPredicate(){
        repository.save(fullDetailsUser);
        repository.save(user);

        List<User> foundUser = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                fullDetailsUser.getFirstName(), fullDetailsUser.getLastName(), fullDetailsUser.getEmail());

        assertEquals(1, foundUser.size());
    }

    @Test
    public void shouldFindTwoUsers(){
        repository.save(fullDetailsUser);
        User newUser = UserBuilder.builder()
                .accountStatus(AccountStatus.NEW)
                .email("215921@edu.p.lodz.pl")
                .firstName("Dawid")
                .lastName("Witaszek")
                .build();
        repository.save(newUser);

        List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                fullDetailsUser.getFirstName(), fullDetailsUser.getLastName(), fullDetailsUser.getEmail());

        assertEquals(2,foundUsers.size());
    }

    @Test
    public void shouldNotFindEveryUser(){
        repository.save(fullDetailsUser);
        User newUser = UserBuilder.builder()
                .accountStatus(AccountStatus.NEW)
                .email("215921@edu.p.lodz.pl")
                .firstName("Dawid")
                .lastName("Witaszek")
                .build();
        repository.save(newUser);
        repository.save(user);

        List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                "DEADBEEF", "DEADBEEF", "DEADBEEF");

        assertEquals(0,foundUsers.size());
    }

    @Test
    public void shouldFindTwoUsersWithOneLetter(){
        repository.save(fullDetailsUser);
        User newUser = UserBuilder.builder()
                .accountStatus(AccountStatus.NEW)
                .email("215921@edu.p.lodz.pl")
                .firstName("Dawid")
                .lastName("Witaszek")
                .build();
        repository.save(newUser);
        repository.save(user);

        List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                "w", "w", "w");

        assertEquals(2,foundUsers.size());
    }

    @Test
    public void shouldThrowAnExceptionWhenSthIsNull(){

        assertThrows(InvalidDataAccessApiUsageException.class, () ->
                repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                        "w", "w", null)
        );
    }

}
