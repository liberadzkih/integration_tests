package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class LikePostRepositoryTest {

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private LikePost likePost;
    private BlogPost blogPost;


    @BeforeEach
    public void setUp() {
        likePost = new LikePost();
        user = new User();
        blogPost = new BlogPost();

        AccountStatus accountStatus = AccountStatus.NEW;

        setNewUser(accountStatus);
        setBlogPost(user, "testowe entry");
        setLikePost(blogPost, user);
    }

    @Test
    public void getUserTestShouldReturnUser() {
        assertEquals(user, likePost.getUser());
    }

    @Test
    public void getUserTestShouldReturnNull() {
        likePost.setUser(null);
        assertNull(likePost.getUser());
    }

    @Test
    public void getBlogPostShouldReturnBlogPost() {
        assertEquals(blogPost,likePost.getPost());
    }

    @Test
    public void getBlogPostShouldReturnNull() {
        likePost.setPost(null);
        assertNull(likePost.getPost());
    }

    @Test
    public void findByUserAndPostShouldResultNotBePresent() {
        entityManager.persist(user);
        entityManager.persist(blogPost);

        Optional<LikePost> result = likePostRepository.findByUserAndPost(user, blogPost);
        assertFalse(result.isPresent());
    }

    @Test
    public void findByUserAndPostShouldReturnLikePost() {
        entityManager.persist(user);
        entityManager.persist(blogPost);
        likePostRepository.save(likePost);

        Optional<LikePost> result = likePostRepository.findByUserAndPost(user, blogPost);
        assertEquals(likePost, result.get());
    }

    private void setLikePost(BlogPost blogPost, User user) {
        likePost.setPost(blogPost);
        likePost.setUser(user);
    }

    private void setBlogPost(User user, String entry) {
        blogPost.setUser(user);
        blogPost.setEntry(entry);
    }

    private void setNewUser(AccountStatus accountStatus) {
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("email@domain.com");
        user.setAccountStatus(accountStatus);
    }
}