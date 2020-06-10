package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private LikePostRepository repository;
    private User user;
    private BlogPost blogPost;
    private LikePost likePost;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Smith");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("BlogEntry");

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);

        entityManager.persist(user);
        entityManager.persist(blogPost);
        repository.save(likePost);
    }

    @Test
    public void shouldReturnCorrectUserWhenAddedToPost() {
        List<LikePost> list = repository.findAll();
        assertTrue(list.get(0)
                       .getUser()
                       .equals(user));
    }

    @Test
    public void shoudlContainsCorrctPostWhenAdded() {
        List<LikePost> list = repository.findAll();
        assertTrue(list.get(0)
                       .getPost()
                       .equals(blogPost));
    }

    @Test
    public void shouldContainsOneLikePostWhenOnlyOneAdded() {
        List<LikePost> list = repository.findAll();
        assertTrue(list.contains(likePost));
    }

    @Test
    public void shouldContainsOnlyOnePostWhenOnlyOneAdded() {
        List<LikePost> list = repository.findAll();
        assertEquals(1, list.size());
    }

    @Test
    public void shouldFindCreatedLikePostWhenUserAndPostProvied() {
        Optional<LikePost> list = repository.findByUserAndPost(user, blogPost);
        assertTrue(list.get()
                       .equals(likePost));
    }

    @Test
    public void shouldNotFindLikePostWhenWrongUserProvided() {
        user = new User();
        user.setFirstName("Kamil");
        user.setLastName("Z");
        user.setEmail("kamilZ@gmail.com");
        user.setAccountStatus(AccountStatus.NEW);
        entityManager.persist(user);
        Optional<LikePost> list = repository.findByUserAndPost(user, blogPost);
        assertFalse(list.isPresent());
    }

    @Test
    public void shouldNotFindLikePostWhenWrongBlogPostProvided() {
        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("WRONG_ENTRY");
        entityManager.persist(blogPost);
        Optional<LikePost> list = repository.findByUserAndPost(user, blogPost);
        assertFalse(list.isPresent());
    }

    @Test
    public void shouldReturnEmptyListWhenNoRecordProvided() {
        repository.deleteAll();
        List<LikePost> list = repository.findAll();
        assertEquals(0, list.size());
    }
}
