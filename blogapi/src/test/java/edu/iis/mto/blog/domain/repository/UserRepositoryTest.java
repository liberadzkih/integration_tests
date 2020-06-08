package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    private final static String unrelevantData = "unrelevant";
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository repository;
    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Smith");
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
        assertThat(users.get(0)
                        .getEmail(), equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), notNullValue());
    }

    @Test
    public void shouldFindOneUserWithCorrectFirstName() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(),
                unrelevantData, unrelevantData);
        assertThat(users.size(), is(1));
    }

    @Test
    public void shouldFindUserWhenFirstNameWithCapitalLetters() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName()
                                                                                                                      .toUpperCase(),
                unrelevantData, unrelevantData);
        assertThat(users.size(), is(1));
    }

    @Test
    public void shouldFindUserWhenCorrectLastName() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(unrelevantData,
                user.getLastName(), unrelevantData);
        assertThat(users.size(), is(1));
    }

    @Test
    public void shouldFindUserWhenCorrectLastNameWithCapitalLetters() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(unrelevantData,
                user.getLastName()
                    .toUpperCase(), unrelevantData);
        assertThat(users.size(), is(1));
    }

    @Test
    public void shouldFindUserWhenCorrectEmail() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(unrelevantData,
                unrelevantData, user.getEmail());
        assertThat(users.size(), is(1));
    }

    @Test
    public void shouldFindUserWhenCorrectEmailWithCapitalLetters() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(unrelevantData,
                unrelevantData, user.getEmail()
                                    .toUpperCase());
        assertThat(users.size(), is(1));
    }

    @Test
    public void shouldNotFindUserWhenIncorrectAllData() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(unrelevantData,
                unrelevantData, unrelevantData);
        assertThat(users.size(), is(0));
    }

    @Test
    public void shouldNotFindUserWhenIncorrectAllDataWithCapitalLetters() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                unrelevantData.toUpperCase(), unrelevantData.toUpperCase(), unrelevantData.toUpperCase());
        assertThat(users.size(), is(0));
    }

    @Test
    public void shouldFindUserWithCorrectFirstAndLastNameWhenIncorrectOnlyEmail() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(),
                user.getLastName(), unrelevantData);
        assertThat(users.size(), is(1));
    }

    @Test
    public void shouldFindUserWhenLastNameIncorrect() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(),
                unrelevantData, user.getEmail());
        assertThat(users.size(), is(1));
    }

}
