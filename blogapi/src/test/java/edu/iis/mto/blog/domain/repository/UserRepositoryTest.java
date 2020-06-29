package edu.iis.mto.blog.domain.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class) @DataJpaTest public class UserRepositoryTest {

    @Autowired private TestEntityManager entityManager;

    @Autowired private UserRepository repository;

    private User user;

    @Before public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();
        assertThat(users, hasSize(0));
    }

    @Test public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        assertThat(users, hasSize(1));
        assertThat(users.get(0).getEmail(), equalTo(persistedUser.getEmail()));
    }

    @Test public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), notNullValue());
    }

    @Test public void findUserByAllCorrectData_shouldEndInSuccess() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", "Kowalski",
                "john@domain.com");
        assertTrue("users list should contain the user", users.contains(user));
        assertEquals(1, users.size());
    }

    @Test public void findUserOnlyCorrectFirstName_shouldEndInSuccess() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", "-", "-");
        assertTrue("users list should contain the user", users.contains(user));
        assertEquals(1, users.size());
    }

    @Test public void findUserOnlyCorrectLastName_shouldEndInSuccess() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("-", "Kowalski", "-");
        assertTrue("users list should contain the user", users.contains(user));
        assertEquals(1, users.size());
    }

    @Test public void findUserOnlyCorrectEmail_shouldEndInSuccess() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("-", "-",
                "john@domain.com");
        assertTrue("users list should contain the user", users.contains(user));
        assertEquals(1, users.size());
    }

    @Test public void findUserIncorrectInformation_shouldNotEndInSuccess() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("-", "-", "-");
        assertFalse("users list should not contain the user", users.contains(user));
        assertEquals(0, users.size());
    }

    @Test public void findUserByAllCorrectData_mixedLetterCase_shouldEndInSuccess() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("JAN", "KoWaLSkI",
                "JOHn@dOmAiN.COm");
        assertTrue("users list should contain the user", users.contains(user));
        assertEquals(1, users.size());
    }

    @Test public void findUserEmptyQuery_shouldFindAllUsers() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", "",
                "");
        assertTrue("users list should contain the user", users.contains(user));
        assertEquals(1, users.size());
    }

}
