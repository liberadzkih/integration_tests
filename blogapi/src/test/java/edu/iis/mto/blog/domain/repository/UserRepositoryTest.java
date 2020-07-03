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

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
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
    public void findByShouldReturnOneRecordFoundByFirstName() {
        repository.save(user);
        List<User> result = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(), "Nonexistent", "Nonexistent");
        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(user));
    }

    @Test
    public void findByShouldReturnOneRecordFoundByLastName() {
        repository.save(user);
        List<User> result = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Nonexistent", user.getLastName(), "Nonexistent");
        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(user));
    }

    @Test
    public void findByShouldReturnOneRecordFoundByEmail() {
        repository.save(user);
        List<User> result = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Nonexistent", "Nonexistent", user.getEmail());
        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(user));
    }

    @Test
    public void findByShouldReturnZeroRecords() {
        List<User> result = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Nonexistent", "Nonexistent", user.getEmail());
        assertThat(result, hasSize(0));
        result = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Nonexistent", user.getLastName(), "Nonexistent");
        assertThat(result, hasSize(0));
        result = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(), "Nonexistent", "Nonexistent");
        assertThat(result, hasSize(0));
    }

    @Test
    public void findByShouldReturnMultipleRecords() {
        int howManyRecords = 10;
        String firstName = "jan";
        String lastName = "kowalski";
        String email = "email@domain.com";

        for (int i = 0; i < howManyRecords; i++) {
            User user = new User();
            user.setFirstName(firstName + i);
            user.setLastName(lastName + i);
            user.setEmail(i + email);
            user.setAccountStatus(AccountStatus.NEW);
            repository.save(user);
        }
        List<User> result = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(firstName, lastName, email);
        assertThat(result, hasSize(howManyRecords));
    }

    @Test
    public void findByLowerAndUpperCase() {
        repository.save(user);
        List<User> result = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Nonexistent", "Nonexistent", user.getEmail().toLowerCase());
        assertThat(result, hasSize(1));
        result = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Nonexistent", user.getLastName().toLowerCase(), "Nonexistent");
        assertThat(result, hasSize(1));
        result = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName().toLowerCase(), "Nonexistent", "Nonexistent");
        assertThat(result, hasSize(1));

        repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Nonexistent", "Nonexistent", user.getEmail().toUpperCase());
        assertThat(result, hasSize(1));
        result = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Nonexistent", user.getLastName().toUpperCase(), "Nonexistent");
        assertThat(result, hasSize(1));
        result = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName().toUpperCase(), "Nonexistent", "Nonexistent");
        assertThat(result, hasSize(1));

    }
}
