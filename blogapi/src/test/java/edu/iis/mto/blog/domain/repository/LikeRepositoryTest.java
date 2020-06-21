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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    private User user, user2;
    private BlogPost blogPost;
    private LikePost likePost;

    @Before public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);

        user2 = new User();
        user2.setFirstName("Anna");
        user2.setLastName("Nowak");
        user2.setEmail("anna@domain.com");
        user2.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("Pierwszy post Jana");
    }

    @Test public void shouldFindOneLikeIfRepositoryContainsOneLikeEntity() {
        giveLikeFromUser2();
        List<LikePost> likes = likePostRepository.findAll();

        assertThat(likes, hasSize(1));
        assertThat(likes.get(0).getUser(), equalTo(user2));
    }

    @Test
    public void shouldDeleteLikeIfRepositoryContainsOneLikeEntity() {
        giveLikeFromUser2();
        likePostRepository.delete(likePost);
        List<LikePost> likes = likePostRepository.findAll();
        System.out.println(likes);

        assertThat(likes, hasSize(0));
    }

    @Test
    public void shouldFindLikeForCorrectArguments() {
        giveLikeFromUser2();
        Optional<LikePost> likeOptional = likePostRepository.findByUserAndPost(user2, blogPost);
        assertTrue(likeOptional.isPresent());
    }

    @Test
    public void shouldNotFindLikeForWrongArguments() {
        giveLikeFromUser2();
        Optional<LikePost> likeOptional = likePostRepository.findByUserAndPost(user, blogPost);
        assertFalse(likeOptional.isPresent()); //problem with isEmpty()
    }

    public void giveLikeFromUser2() {
        likePost = new LikePost();
        likePost.setPost(blogPost);
        likePost.setUser(user2);     //Anna lajkuje post Jana

        entityManager.persist(user);
        entityManager.persist(user2);
        entityManager.persist(blogPost);
        entityManager.persist(likePost);
    }

}

