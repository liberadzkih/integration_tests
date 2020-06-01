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
import static org.junit.Assert.assertTrue;

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
	public void shouldFindOneRecordFoundByFirstNameListHasOneElement() {
		repository.save(user);
		List<User> result = (repository.
				findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
						user.getFirstName(), "X", "X"));
		assertThat(result, hasSize(1));
		assertThat(result.get(0), equalTo(user));
	}

	@Test
	public void shouldFindOneRecordFoundByLastNameListHasOneElement() {
		user.setLastName("Bilant");
		repository.save(user);
		List<User> result = (repository.
				findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
						"X", user.getLastName(), "X"));
		assertThat(result, hasSize(1));
		assertThat(result.get(0), equalTo(user));
	}

	@Test
	public void shouldFindOneRecordFoundByEmailListHasOneElement() {
		repository.save(user);
		List<User> result = (repository.
				findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
						"X", "X", user.getEmail()));
		assertThat(result, hasSize(1));
		assertThat(result.get(0), equalTo(user));
	}

	@Test
	public void shouldFindOneRecordFoundByFirstNameListHasThreeElements() {
		repository.save(user);
		User user2 = new User();
		user2.setLastName("Bilant");
		user2.setEmail("andrzej@domain.pl");
		user2.setFirstName("Andrzej");
		user2.setAccountStatus(AccountStatus.NEW);
		repository.save(user2);
		User user3 = new User();
		user3.setLastName("Potter");
		user3.setEmail("harry@domain.pl");
		user3.setFirstName("Harry");
		user3.setAccountStatus(AccountStatus.NEW);
		repository.save(user3);
		List<User> result = (repository.
				findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
						user.getFirstName(), "X", "X"));
		assertThat(result, hasSize(1));
		assertThat(result.get(0), equalTo(user));
	}

	@Test
	public void shouldFindOneRecordFoundByLastNameListHasThreeElements() {
		user.setLastName("Bilant");
		repository.save(user);
		User user2 = new User();
		user2.setLastName("Dokimuk");
		user2.setEmail("andrzej@domain.pl");
		user2.setFirstName("Andrzej");
		user2.setAccountStatus(AccountStatus.NEW);
		repository.save(user2);
		User user3 = new User();
		user3.setLastName("Potter");
		user3.setEmail("harry@domain.pl");
		user3.setFirstName("Harry");
		user3.setAccountStatus(AccountStatus.NEW);
		repository.save(user3);
		List<User> result = (repository.
				findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
						"X", user.getLastName(), "X"));
		assertThat(result, hasSize(1));
		assertThat(result.get(0), equalTo(user));
	}

	@Test
	public void shouldFindOneRecordFoundByEmailListHasThreeElements() {
		repository.save(user);
		User user2 = new User();
		user2.setLastName("Dokimuk");
		user2.setEmail("andrzej@domain.pl");
		user2.setFirstName("Andrzej");
		user2.setAccountStatus(AccountStatus.NEW);
		repository.save(user2);
		User user3 = new User();
		user3.setLastName("Potter");
		user3.setEmail("harry@domain.pl");
		user3.setFirstName("Harry");
		user3.setAccountStatus(AccountStatus.NEW);
		repository.save(user3);
		List<User> result = (repository.
				findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
						"X", "X", user.getEmail().toLowerCase()));
		assertThat(result, hasSize(1));
		assertThat(result.get(0), equalTo(user));
	}

	@Test
	public void shouldFindTwoRecordsFoundByLastNameListHasTwoElements() {
		user.setLastName("Bilant");
		repository.save(user);
		User user2 = new User();
		user2.setLastName("Bilant");
		user2.setEmail("andrzej@domain.pl");
		user2.setFirstName("Andrzej");
		user2.setAccountStatus(AccountStatus.NEW);
		repository.save(user2);
		List<User> result = (repository.
				findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
						"X", user.getLastName().toUpperCase(), "X"));
		assertThat(result, hasSize(2));
		assertTrue(result.contains(user));
		assertTrue(result.contains(user2));
	}

	@Test
	public void shouldFindTwoRecordsListHasThreeElements() {
		user.setLastName("Bilant");
		repository.save(user);
		User user2 = new User();
		user2.setLastName("Dokimuk");
		user2.setEmail("andrzej@domain.pl");
		user2.setFirstName("Andrzej");
		user2.setAccountStatus(AccountStatus.NEW);
		repository.save(user2);
		User user3 = new User();
		user3.setLastName("Potter");
		user3.setEmail("harry@domain.pl");
		user3.setFirstName("Harry");
		user3.setAccountStatus(AccountStatus.NEW);
		repository.save(user3);
		List<User> result = (repository.
				findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
						user2.getFirstName(), user.getLastName(), "X"));
		assertThat(result, hasSize(2));
		assertTrue(result.contains(user));
		assertTrue(result.contains(user2));
	}

	@Test
	public void shouldFindThreeRecordsListHasThreeElements() {
		user.setLastName("Bilant");
		repository.save(user);
		User user2 = new User();
		user2.setLastName("Dokimuk");
		user2.setEmail("andrzej@domain.pl");
		user2.setFirstName("Andrzej");
		user2.setAccountStatus(AccountStatus.NEW);
		repository.save(user2);
		User user3 = new User();
		user3.setLastName("Potter");
		user3.setEmail("harry@domain.pl");
		user3.setFirstName("Harry");
		user3.setAccountStatus(AccountStatus.NEW);
		repository.save(user3);
		List<User> result = (repository.
				findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
						user2.getFirstName(), user.getLastName(), user3.getEmail()));
		assertThat(result, hasSize(3));
		assertTrue(result.contains(user));
		assertTrue(result.contains(user2));
		assertTrue(result.contains(user3));
	}
}
