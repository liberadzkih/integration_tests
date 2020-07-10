package edu.iis.mto.blog.domain.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    public static final String unrelevant = "unrelevant";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Nowak");
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
                        .getEmail(),
                equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), notNullValue());
    }


    @Test
    public void shouldFindCorrectUserWithFirstName() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(),
                unrelevant, unrelevant);
        assertThat(users.size(), is(1));
    }

    @Test
    public void shouldFindCorrectUserWithFirstNameUpperCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName()
                        .toUpperCase(),
                unrelevant, unrelevant);
        assertThat(users.size(), is(1));
    }

    @Test
    public void shouldFindCorrectUserWithLastName() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(unrelevant,
                user.getLastName(), unrelevant);
        assertThat(users.size(), is(1));
    }

    @Test
    public void shouldFindCorrectUserWithLastNameUpperCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(unrelevant,
                user.getLastName()
                        .toUpperCase(), unrelevant);
        assertThat(users.size(), is(1));
    }

    @Test
    public void shouldFindCorrectUserWithEmail() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(unrelevant,
                unrelevant, user.getEmail());
        assertThat(users.size(), is(1));
    }

    @Test
    public void shouldFindCorrectUserWithEmailUpperCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(unrelevant,
                unrelevant, user.getEmail()
                        .toUpperCase());
        assertThat(users.size(), is(1));
    }

    @Test
    public void shouldNotFindIncorrectUser() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(unrelevant,
                unrelevant, unrelevant);
        assertThat(users.size(), is(0));
    }

    @Test
    public void shouldNotFindIncorrectUserUpperCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                unrelevant.toUpperCase(), unrelevant.toUpperCase(), unrelevant.toUpperCase());
        assertThat(users.size(), is(0));
    }

    @Test
    public void shouldFindCorrectUserWithFirstNameAndLastNameOnly() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(),
                user.getLastName(), unrelevant);
        assertThat(users.size(), is(1));
    }

    @Test
    public void shouldFindCorrectUserWithFirstNameAndEmailOnly() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(),
                unrelevant, user.getEmail());
        assertThat(users.size(), is(1));
    }
}
