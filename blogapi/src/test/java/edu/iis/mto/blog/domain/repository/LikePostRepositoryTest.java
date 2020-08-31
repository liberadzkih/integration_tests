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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class) @DataJpaTest public class LikePostRepositoryTest {

    private User user;

    private BlogPost blogPost;

    private LikePost likePost;

    @Autowired private TestEntityManager entityManager;

    @Autowired private LikePostRepository repository;

    @Before public void setUp() {
        user = setUpANewUser("Jan", "Kowalski", "john@domain.com");

        blogPost = setUpANewBlogPost(user, "Entry");

        likePost = setUpANewLikePost(blogPost, user);

    }

    @Test public void likePostShouldContainCorrectBlogPost() {
        assertEquals(blogPost, likePost.getPost());
    }

    @Test public void likePostShouldContainCorrectUser() {
        assertEquals(user, likePost.getUser());
    }

    @Test public void changingLikePostUserAndPostShouldBeCorrectlyUpdated() {
        User newUser = new User();
        BlogPost newBlogPost = new BlogPost();
        likePost.setUser(newUser);
        likePost.setPost(newBlogPost);
        assertEquals(newBlogPost, likePost.getPost());
        assertEquals(newUser, likePost.getUser());
    }

    @Test public void findingUsingCorrectDataShouldReturnCorrectLikePost() {
        saveToRepository();
        Optional<LikePost> found = repository.findByUserAndPost(user, blogPost);
        assertTrue(found.isPresent());
        assertEquals(likePost, found.get());
    }

    @Test public void findingUsingPostNotOwnedByUserShouldNotReturnThePost() {
        saveToRepository();
        User anotherUser = setUpANewUser("Marcin", "Nowak", "martin@domain.com");
        entityManager.persist(anotherUser);
        Optional<LikePost> found = repository.findByUserAndPost(anotherUser, blogPost);
        assertFalse(found.isPresent());
    }

    private void saveToRepository() {
        entityManager.persist(user);
        entityManager.persist(blogPost);
        repository.save(likePost);
    }

    private User setUpANewUser(String firstName, String lastName, String email) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAccountStatus(AccountStatus.NEW);
        return user;
    }

    private BlogPost setUpANewBlogPost(User user, String entry) {
        BlogPost blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry(entry);
        return blogPost;
    }

    private LikePost setUpANewLikePost(BlogPost blogPost, User user) {
        LikePost likePost = new LikePost();
        likePost.setPost(blogPost);
        likePost.setUser(user);
        return likePost;
    }
}
