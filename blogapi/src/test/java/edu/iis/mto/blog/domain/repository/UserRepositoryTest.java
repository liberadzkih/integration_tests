package edu.iis.mto.blog.domain.repository;

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
    private String anythingOtherThanWhatAnyOfTheValuesContains = "================================";

    @Before
    public void setUp() {
        user = new UserBuilder().withFirstName("Jan").withEmail("john1@domain.com").build();
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        repository.deleteAll();
        List<User> users = repository.findAll();

        assertThat(users, hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        List<User> users = repository.findAll();

        assertThat(users, hasSize(1));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), notNullValue());
    }

    @Test
    public void shouldFindAllUsersWithSameName() {
        repository.save(new UserBuilder().withFirstName("Jan").withLastName("Testowy").withEmail("student@p.lodz.pl").build());
        repository.save(new UserBuilder().withFirstName("Janek").withLastName("Bananowy").withEmail("banan@p.lodz.pl").build());
        repository.save(new UserBuilder().withFirstName("Jan").withLastName("Jabłkowy").withEmail("jabłko@p.lodz.pl").build());
        repository.save(new UserBuilder().withFirstName("Dzban").withLastName("Pomarańczowy").withEmail("pomarańcza@p.lodz.pl").build());
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", anythingOtherThanWhatAnyOfTheValuesContains, anythingOtherThanWhatAnyOfTheValuesContains);
        assertThat(foundUsersList, hasSize(3));
    }

    @Test
    public void shouldFindAllUsersWithSameLastName() {
        repository.save(new UserBuilder().withFirstName("Jabłko").withLastName("Student").withEmail("jabłko@p.lodz.pl").build());
        repository.save(new UserBuilder().withFirstName("Banan").withLastName("Studentowy").withEmail("banan@p.lodz.pl").build());
        repository.save(new UserBuilder().withFirstName("Szaman").withLastName("Szamanowy").withEmail("szaman@p.lodz.pl").build());
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(anythingOtherThanWhatAnyOfTheValuesContains, "Student", anythingOtherThanWhatAnyOfTheValuesContains);
        assertThat(foundUsersList, hasSize(2));
    }

    @Test
    public void shouldFindAllUsersWithSameEmail() {
        repository.save(new UserBuilder().withFirstName("Biała").withLastName("Owca").withEmail("owce@p.lodz.pl").build());
        repository.save(new UserBuilder().withFirstName("Zielona").withLastName("Owca1").withEmail("owce1@p.lodz.pl").build());
        repository.save(new UserBuilder().withFirstName("Żółta").withLastName("Owca2").withEmail("owce2@p.lodz.pl").build());
        repository.save(new UserBuilder().withFirstName("Czarna").withLastName("Owca4").withEmail("nieztejrodziny@p.lodz.pl").build());
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(anythingOtherThanWhatAnyOfTheValuesContains, anythingOtherThanWhatAnyOfTheValuesContains, "owce");
        assertThat(foundUsersList, hasSize(3));
    }

    @Test
    public void shouldFindNone() {
        repository.save(new UserBuilder().withFirstName("Jan").withLastName("Testowy").withEmail("student@p.lodz.pl").build());
        repository.save(new UserBuilder().withFirstName("Janek").withLastName("Bananowy").withEmail("banan@p.lodz.pl").build());
        repository.save(new UserBuilder().withFirstName("Jan").withLastName("Jabłkowy").withEmail("jabłko@p.lodz.pl").build());
        repository.save(new UserBuilder().withFirstName("Dzban").withLastName("Pomarańczowy").withEmail("pomarańcza@p.lodz.pl").build());
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(anythingOtherThanWhatAnyOfTheValuesContains, anythingOtherThanWhatAnyOfTheValuesContains, anythingOtherThanWhatAnyOfTheValuesContains);
        assertThat(foundUsersList, hasSize(0));
    }

}
