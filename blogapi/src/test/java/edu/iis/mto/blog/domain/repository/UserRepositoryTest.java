package edu.iis.mto.blog.domain.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Blachowicz");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
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
        assertThat(users.get(0).getEmail(), equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldFindOneUsersByUserFirstName() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(), "",
                "");
        assertThat(users, hasSize(1));
        assertThat(users.get(0), equalTo(persistedUser));
    }

    @Test
    public void shouldFindOneUsersByUserLastName() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", user.getLastName(),
                "");
        assertThat(users, hasSize(1));
        assertThat(users.get(0), equalTo(persistedUser));
    }

    @Test
    public void shouldFindOneUsersByUserEmail() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", "", user.getEmail());
        assertThat(users, hasSize(1));
        assertThat(users.get(0), equalTo(persistedUser));
    }

    @Test
    public void shouldFindOneThousandUsers() {
        for (int i = 0; i <= 999; i++) {
            user = new User();
            user.setFirstName("JaASFSDn");
            user.setLastName("ASAsASAsaDASF");
            user.setEmail("sasdasdas" + i + "@easdasdas.pl");
            user.setAccountStatus(AccountStatus.NEW);
            repository.save(user);
        }
        List<User> users = repository.findAll();
        assertThat(users, hasSize(1000));
    }

    @Test
    public void shouldNotAllowToSaveSecondUserWithTheSameEmailAndThrowException() {
        user = new User();
        user.setFirstName("Adam");
        user.setLastName("Malysz");
        user.setEmail("skoki@narciarskie.pl");
        user.setAccountStatus(AccountStatus.NEW);
        repository.save(user);

        user = new User();
        user.setFirstName("Jane");
        user.setLastName("Ahonen");
        user.setEmail("skoki@narciarskie.pl");
        user.setAccountStatus(AccountStatus.NEW);

        assertThrows(DataIntegrityViolationException.class, () -> repository.save(user));
    }

    @Test
    public void shouldThrowExceptionWhenNullIsFindMethodParameter() {
        assertThrows(InvalidDataAccessApiUsageException.class,
                () -> repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", "", null));
        assertThrows(InvalidDataAccessApiUsageException.class,
                () -> repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", null, ""));
        assertThrows(InvalidDataAccessApiUsageException.class,
                () -> repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(null, "", ""));
    }

    @Test
    public void shouldStoreANewUser() {
        User persistedUser = repository.save(user);
        assertThat(persistedUser.getId(), notNullValue());
    }

}
