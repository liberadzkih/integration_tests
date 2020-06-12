package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
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
        repository.deleteAll();
        user = new UserBuilder().withFirstName("Jan").withEmail("john@domain.com").build();
        blogPost = new BlogPost();
        blogPost.setEntry("Some entry");
        blogPost.setUser(user);
        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
    }

    @Test
    public void shouldNotFindLikesOnNewPost() {
        List<LikePost> likesFound = repository.findAll();
        assertThat(likesFound, hasSize(0));
    }

    @Test
    public void shouldFindOneLikeOnPostLikedByOnePerson() {
        entityManager.persist(user);
        entityManager.persist(blogPost);
        entityManager.persist(likePost);
        List<LikePost> likesFound = repository.findAll();

        assertThat(likesFound, hasSize(1));
    }

    @Test
    public void shouldStoreANewLikePost() {
        entityManager.persist(user);
        entityManager.persist(blogPost);
        LikePost persistedLikePost = repository.save(likePost);

        assertThat(persistedLikePost.getId(), notNullValue());
    }

    @Test
    public void shouldFindLikeByUserAndBlogPost() {
        entityManager.persist(user);
        entityManager.persist(blogPost);
        LikePost persistedLikePost = repository.save(likePost);

        Optional<LikePost> likesFound = repository.findByUserAndPost(user, blogPost);
        assertTrue(likesFound.isPresent());
        assertEquals(persistedLikePost, likesFound.get());
    }

    @Test
    public void modifiedLikeShouldBeSaved() {
        entityManager.persist(user);
        entityManager.persist(blogPost);
        LikePost persistedLikePost = repository.save(likePost);

        User newUser = new UserBuilder().withFirstName("Casper").withEmail("casper@domain.com").build();
        Optional<LikePost> likeFound = repository.findById(persistedLikePost.getId());
        assertTrue(likeFound.isPresent());
        likeFound.get().setUser(newUser);
        repository.save(likeFound.get());
        assertEquals(persistedLikePost.getUser(), newUser);
    }
}