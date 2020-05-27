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

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    private String firstName = "Jan", lastName="Komasa", email = "john@domain.com";
    private String firstName2 = "Piotr", lastName2 = "Adamczyk", email2 = "ddddddddd@wp.com";

    private String badFirstName="Alicja", badLastName = "Dziuda", badEmail ="djjdnjd@wp.pl";

    private User user2;


    @Before
    public void setUp() {
        user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAccountStatus(AccountStatus.NEW);
    }

    private void createSecondUser(){
        user2 = new User();
        user2.setFirstName(firstName);
        user2.setLastName(lastName2);
        user2.setEmail(email2);
        user2.setAccountStatus(AccountStatus.NEW);
    }
    //@Ignore a collection with size <0>
    //     but: collection size was <1>
    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();
        assertThat(users, hasSize(0));
    }

   // @Ignore
   //Unique index or primary key violation
    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        assertThat(users, hasSize(1));
        assertThat(users.get(0)
                        .getEmail(),
                equalTo(persistedUser.getEmail()));
    }

   // @Ignore
    //Unique index or primary key violation
    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), notNullValue());
    }

    @Test public void searchingByProperFirstName_shouldFindOneUser() {
        //wywołanie perist powoduje dołączenie przekazanego obiektu do zbioru obiektów zarządzanych
        // oraz wywołanie INSERT w momencie zakończenia transakcji
        entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(firstName,badLastName,badEmail);
        assertTrue(users.contains(user));
        assertEquals(1, users.size());
    }

    @Test public void searchingByProperLastName_shouldFindOneUser() {
        entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(badFirstName,lastName,badEmail);
        assertTrue(users.contains(user));
        assertEquals(1, users.size());
    }

    @Test public void searchingByProperFirstOrLastName_shouldFindTwoUsers() {
        createSecondUser();
        entityManager.persist(user);
        entityManager.persist(user2);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(firstName,lastName2,badEmail);
        assertTrue(users.contains(user));
        assertTrue(users.contains(user2));
        assertEquals(2, users.size());
    }

    @Test public void searchingByNotProperData_shouldFindZeroUsers() {
        entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(badFirstName,badLastName,badEmail);
        assertFalse(users.contains(user));
        assertEquals(0, users.size());
    }

    @Test public void shouldFindTwoUsers() {
        createSecondUser();
        entityManager.persist(user);
        entityManager.persist(user2);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("","", "");
        assertTrue(users.contains(user));
        assertTrue(users.contains(user2));
        assertEquals(2, users.size());
    }

    @Test public void searchingByPartOfProperEmail_shouldFindTwoUsers() {
        createSecondUser();
        entityManager.persist(user);
        entityManager.persist(user2);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(badFirstName,badLastName, ".com");
        assertTrue(users.contains(user));
        assertTrue(users.contains(user2));
        assertEquals(2, users.size());
    }
}
