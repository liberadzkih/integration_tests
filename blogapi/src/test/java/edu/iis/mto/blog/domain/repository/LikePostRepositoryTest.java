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
    private LikePost likePost;
    private BlogPost blogPost;
    private BlogPost anotherBlogPost;

    private String firstName = "Jan", lastName="Komasa", email = "john@domain.com";

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("Entry");

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);

        entityManager.persist(user);
        entityManager.persist(blogPost);
    }

    //creating modifying

    @Test public void emptyRepository_shouldHaveZeroLikePosts() {
        List<LikePost> post = repository.findAll();
        assertEquals(0, post.size());
    }

    @Test public void properlyCreatedLikePost_shouldHaveNotNullId() {
        repository.save(likePost);
        assertNotNull(likePost.getId());
    }

    @Test public void properlyCreatedLikePost_shouldBeProperlySaved() {
        repository.save(likePost);
        List<LikePost> post = repository.findAll();
        assertEquals(1, post.size());
    }

    @Test public void changedBlogPostInLikePost_shouldBeSavedInDatabase() {
        repository.save(likePost);
        Optional<LikePost> post = repository.findByUserAndPost(user,blogPost);
        createAnotherBlogPost();
        post.get().setPost(anotherBlogPost);
        repository.save(likePost);
        assertSame(post.get().getPost(), anotherBlogPost);
    }

    //findByUserAndPost

    @Test public void searchingByProperUserAndProperBlogPost_shouldFindPostLike() {
        repository.save(likePost);
        Optional<LikePost> post = repository.findByUserAndPost(user,blogPost);
        assertTrue(post.isPresent());
        assertEquals(post.get().getUser(), user);
        assertEquals(post.get().getPost(), blogPost);
    }

    @Test public void searchingByOnlyProperUser_shouldNotFindPostLike() {
        repository.save(likePost);
        createAnotherBlogPost();
        Optional<LikePost> post = repository.findByUserAndPost(user,anotherBlogPost);
        assertFalse(post.isPresent());
    }

    @Test public void searchingWhenUserIsNull_shouldNotFindPostLike() {
        repository.save(likePost);
        Optional<LikePost> post = repository.findByUserAndPost(null,blogPost);
        assertFalse(post.isPresent());
    }

    private void createAnotherBlogPost(){
        anotherBlogPost = new BlogPost();
        anotherBlogPost.setUser(user);
        anotherBlogPost.setEntry("Entry222");
        entityManager.persist(anotherBlogPost);
    }
}
