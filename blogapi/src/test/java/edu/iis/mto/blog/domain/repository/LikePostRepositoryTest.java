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
        user.setLastName("Przepracowany");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        userRepository.save(user);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("ExampleData");
        entityManager.persist(blogPost);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
    }

    @Test
    public void shouldFindNoLikePostsIfRepositoryIsEmpty() {

        List<LikePost> likePosts = likePostRepository.findAll();

        assertThat(likePosts, hasSize(0));
    }

    @Test
    public void shouldFindOnePostIfRepositoryContainsOneLikePostEntity() {
        LikePost persistedLikePost = entityManager.persist(likePost);
        List<LikePost> likePosts = likePostRepository.findAll();

        assertThat(likePosts, hasSize(1));
        assertThat(likePosts.get(0).getPost(), equalTo(persistedLikePost.getPost()));
    }

    @Test
    public void shouldStoreANewLikePost() {
        LikePost post = likePostRepository.save(likePost);

        assertThat(post.getPost(), notNullValue());
    }

    @Test
    public void shouldChangeTheDataOfPost() {
        likePostRepository.save(likePost);
        LikePost updatedLikePost = likePostRepository.findAll().get(0);
        updatedLikePost.getPost().setEntry("NewData");
        likePostRepository.save(updatedLikePost);
        assertThat(likePostRepository.findAll().get(0).getPost().getEntry(), equalTo(updatedLikePost.getPost().getEntry()));
    }

    @Test
    public void shouldFindLikePostByUserAndBlogPost() {
        likePostRepository.save(likePost);
        Optional<LikePost> likePosts = likePostRepository.findByUserAndPost(user, blogPost);
        assertThat(likePosts.get(), is(equalTo(likePost)));
    }
}
