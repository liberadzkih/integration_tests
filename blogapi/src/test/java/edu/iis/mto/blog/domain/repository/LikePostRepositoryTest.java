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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikePostRepository likePostRepository;

    private User user;
    private BlogPost blogPost;
    private LikePost likePost;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        userRepository.save(user);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("Some post");
        entityManager.persist(blogPost);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
    }

    @Test
    public void shouldFindNoPostsIfRepositoryIsEmpty() {

        List<LikePost> listPosts = likePostRepository.findAll();

        assertThat(listPosts, hasSize(0));
    }

    @Test
    public void shouldFindOneLikePostIfRepositoryContainsOneLikePostEntity() {
        LikePost persistedPost = entityManager.persist(likePost);
        List<LikePost> likePosts = likePostRepository.findAll();

        assertThat(likePosts, hasSize(1));
        assertThat(likePosts.get(0), equalTo(persistedPost));
    }

    @Test
    public void shouldStoreANewLikePost() {
        LikePost persistedPost = likePostRepository.save(likePost);

        assertThat(persistedPost.getPost(), notNullValue());
    }

    @Test
    public void shouldChangeDataOfLikePost() {
        likePostRepository.save(likePost);
        BlogPost newBlogPost = new BlogPost();
        newBlogPost.setUser(user);
        newBlogPost.setEntry("Other post");
        entityManager.persist(newBlogPost);
        likePost.setPost(newBlogPost);
        likePostRepository.save(likePost);
        assertThat(likePostRepository.findAll().get(0).getPost().getEntry(), equalTo("Other post"));
    }

    @Test
    public void shouldNotFindLikePostByUserAndBlogPostWhenDoesNotExist() {
        Optional<LikePost> likePosts = likePostRepository.findByUserAndPost(user, blogPost);
        assertThat(likePosts.isPresent(), is(equalTo(false)));
    }

    @Test
    public void shouldFindLikePostByUserAndBlogPostWhenExists() {
        likePostRepository.save(likePost);
        Optional<LikePost> likePosts = likePostRepository.findByUserAndPost(user, blogPost);
        assertThat(likePosts.isPresent(), is(equalTo(true)));
        assertThat(likePosts.get(), is(equalTo(likePost)));
    }

}
