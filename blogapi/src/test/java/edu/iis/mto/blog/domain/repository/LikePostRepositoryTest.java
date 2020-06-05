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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository likePostRepository;

    private User user;
    private LikePost likePost;
    private BlogPost blogPost;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        blogPost.setEntry("New post");
        blogPost.setUser(user);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);

        entityManager.persist(user);
        entityManager.persist(blogPost);
    }

    @Test
    public void shouldFindNoLikePostIfLikePostRepositoryIsEmpty() {

        List<LikePost> likePostList = likePostRepository.findAll();

        assertThat(likePostList, hasSize(0));
    }

    @Test
    public void shouldFindOneLikePostIfLikePostRepositoryContainsOneLikePostEntity() {

        LikePost persistedLikePost = entityManager.persist(likePost);
        List<LikePost> likePostList = likePostRepository.findAll();

        assertThat(likePostList, hasSize(1));
        assertThat(likePostList.get(0)
                        .getPost(),
                equalTo(persistedLikePost.getPost()));
    }

    @Test
    public void shouldStoreANewLikePost() {
        LikePost persistedLikePost = likePostRepository.save(likePost);

        assertThat(persistedLikePost.getId(), notNullValue());
    }

    @Test
    public void shouldFindLikePostWhenUserAndBLogPostAreCorrect() {
        likePostRepository.save(likePost);
        Optional<LikePost> likePostOptional = likePostRepository.findByUserAndPost(user, blogPost);

        assertThat(likePostOptional.get(), is(likePost));
    }

    @Test
    public void shouldNotFindLikePostWhenBlogPostIsIncorrect() {
        BlogPost otherBlogPost = new BlogPost();
        otherBlogPost.setEntry("Other post");
        otherBlogPost.setUser(user);
        entityManager.persist(otherBlogPost);
        likePostRepository.save(likePost);
        Optional<LikePost> likePostOptional = likePostRepository.findByUserAndPost(user, otherBlogPost);

        assertThat(likePostOptional, is(Optional.empty()));
    }

    @Test
    public void shouldNotFindLikePostWhenUserIsIncorrect() {
        User otherUser = new User();
        otherUser.setFirstName("Matthew");
        otherUser.setLastName("Noname");
        otherUser.setEmail("noname@gmail.com");
        otherUser.setAccountStatus(AccountStatus.NEW);
        entityManager.persist(otherUser);
        likePostRepository.save(likePost);
        Optional<LikePost> likePostOptional = likePostRepository.findByUserAndPost(otherUser, blogPost);

        assertThat(likePostOptional, is(Optional.empty()));
    }
}
