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
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    private LikePost likePost;
    private BlogPost post;
    private User user;

    @Autowired
    private LikePostRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void setUp(){
        post = new BlogPost();
        post.setEntry("DEADBEEF");
        user = UserBuilder.builder()
                .accountStatus(AccountStatus.NEW)
                .email("215920@edu.p.lodz.pl")
                .firstName("Dawid")
                .lastName("Witaszek")
                .build();
        likePost = new LikePost();
       user = userRepository.save(user);
       post.setUser(user);
       post = blogPostRepository.save(post);
    }

    @Test
    public void shouldPersistEntity(){
        likePost.setUser(user);
        likePost.setPost(post);
        entityManager.persist(likePost);
        List<LikePost> likePostList =  repository.findAll();
        assertEquals(1,likePostList.size());
        likePost = likePostList.get(0);
        assertEquals(user.getId(), likePost.getUser().getId());
        assertEquals(post.getId(), likePost.getPost().getId());
    }

    @Test
    public void shouldEditEntity(){
        likePost.setUser(user);
        likePost.setPost(post);
        likePost = repository.save(likePost);
        User newUser = UserBuilder.builder()
                .accountStatus(AccountStatus.NEW)
                .email("215921@edu.p.lodz.pl")
                .firstName("Krzysztof")
                .lastName("Jarzyna")
                .build();
        newUser = userRepository.save(newUser);

        likePost.setUser(newUser);
        likePost = repository.save(likePost);
        assertEquals(newUser.getId(), likePost.getUser().getId());
    }


    @Test
    public void shouldFindUser(){
        likePost.setUser(user);
        likePost.setPost(post);
        likePost = repository.save(likePost);
        Optional<LikePost> optionalLikePost = repository.findByUserAndPost(likePost.getUser(),likePost.getPost());
        assertTrue(optionalLikePost.isPresent());
        LikePost foundLikePost = optionalLikePost.get();
        assertEquals(likePost.getId(), foundLikePost.getId());
    }

    @Test
    public void shouldNotFindUser(){
        likePost.setUser(user);
        likePost.setPost(post);
        likePost = repository.save(likePost);
        User newUser = UserBuilder.builder()
                .accountStatus(AccountStatus.NEW)
                .email("215921@edu.p.lodz.pl")
                .firstName("Krzysztof")
                .lastName("Jarzyna")
                .build();
        newUser = userRepository.save(newUser);

        Optional<LikePost> optionalLikePost = repository.findByUserAndPost(newUser,likePost.getPost());
        assertFalse(optionalLikePost.isPresent());
    }


}
