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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
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
        user.setLastName("Blachowicz");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("test");

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
    }

    @Test
    public void ShouldFindOneLikeInRepository() {
        entityManager.persist(user);
        entityManager.persist(blogPost);
        entityManager.persist(likePost);
        List<LikePost> likes = likePostRepository.findAll();
        assertTrue(likes.size() == 1);
    }

    @Test
    public void ShouldFindZeroLikesInRepository() {
        List<LikePost> likes = likePostRepository.findAll();
        assertTrue(likes.size() == 0);
    }
    
    @Test
    public void ShouldFindOneLikePostByFindByUserAndPost(){
        entityManager.persist(user);
        entityManager.persist(blogPost);
        entityManager.persist(likePost);
        Optional<LikePost> likes = likePostRepository.findByUserAndPost(user, blogPost);
        assertEquals(likes.get(), likePost);
    }

    @Test
    public void ShouldFindZeroLikePostByFindByUserAndPost(){
        entityManager.persist(user);
        entityManager.persist(blogPost);
        Optional<LikePost> likes = likePostRepository.findByUserAndPost(user, blogPost);
        assertFalse(likes.isPresent());
    }
}
