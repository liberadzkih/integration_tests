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
    private LikePostRepository likePostRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private LikePost likePost;
    private BlogPost blogPost;

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
    }

    @Test
    public void emptyRepository_shouldReturnZeroPosts() {
        List<LikePost> post = likePostRepository.findAll();
        assertEquals(0, post.size());
    }

    @Test
    public void savingCorrectPost_shouldBeProperlySavedInRepository() {
        LikePost persistedLikePost = likePostRepository.save(likePost);
        assertNotEquals(persistedLikePost.getId(), null);
    }

    @Test
    public void findByCorrectUserAndCorrectPost_shouldReturnPost() {
        LikePost persistedLikePost = likePostRepository.save(likePost);
        Optional<LikePost> post = likePostRepository.findByUserAndPost(user, blogPost);
        assertTrue(post.isPresent());
        assertEquals(persistedLikePost, post.get());
    }

    @Test
    public void findByNotCorrectUserAndCorrectPost_shouldNotReturnPost() {
        User newUser = new User();
        newUser.setFirstName("Irrelevant");
        newUser.setLastName("Irrelevant");
        newUser.setEmail("example@example.com");
        newUser.setAccountStatus(AccountStatus.NEW);

        entityManager.persist(newUser);
        Optional<LikePost> post = likePostRepository.findByUserAndPost(newUser, blogPost);
        assertFalse(post.isPresent());
    }

    @Test
    public void findByCorrectUserAndNotCorrectPost_shouldNotReturnPost() {
        BlogPost newBlogPost = new BlogPost();
        newBlogPost.setUser(user);
        newBlogPost.setEntry("placeholder");
        entityManager.persist(newBlogPost);
        Optional<LikePost> list = likePostRepository.findByUserAndPost(user, newBlogPost);
        assertFalse(list.isPresent());
    }
}
