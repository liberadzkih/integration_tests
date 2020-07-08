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
        user.setLastName("Nowak");
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
    public void returnCorrectUserWhenAddedToPost() {
        List<LikePost> likeList = repository.findAll();
        assertEquals(likeList.get(0)
                .getUser(), user);
    }

    @Test
    public void returnEmptyListWhenNoRecord() {
        repository.deleteAll();
        List<LikePost> likeList = repository.findAll();
        assertEquals(0, likeList.size());
    }

    @Test
    public void containsCorrectPost() {
        List<LikePost> likeList = repository.findAll();
        assertEquals(likeList.get(0)
                .getPost(), blogPost);
    }

    @Test
    public void containsOneLikeWhenAddedOne() {
        List<LikePost> likeList = repository.findAll();
        assertTrue(likeList.contains(likePost));
    }

    @Test
    public void containsOnePostWhenAddedOne() {
        List<LikePost> likeList = repository.findAll();
        assertEquals(1, likeList.size());
    }

    @Test
    public void findLikeWhenUserAndPostCorrect() {
        Optional<LikePost> likeList = repository.findByUserAndPost(user, blogPost);
        assertEquals(likeList.get(), likePost);
    }

    @Test
    public void notFindLikeWhenUserAndPostIncorrect() {
        user = new User();
        user.setFirstName("Adam");
        user.setLastName("Kowalski");
        user.setEmail("adamkowalski@gmail.com");
        user.setAccountStatus(AccountStatus.NEW);
        entityManager.persist(user);
        Optional<LikePost> likeList = repository.findByUserAndPost(user, blogPost);
        assertFalse(likeList.isPresent());
    }

    @Test
    public void notFindLikeWhenBlogPostIncorrect() {
        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("WRONG_ENTRY");
        entityManager.persist(blogPost);
        Optional<LikePost> likeList = repository.findByUserAndPost(user, blogPost);
        assertFalse(likeList.isPresent());
    }
}