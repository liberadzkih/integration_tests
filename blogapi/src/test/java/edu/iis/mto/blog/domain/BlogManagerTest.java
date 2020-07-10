package edu.iis.mto.blog.domain;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    private final String unrelevantName = "name";
    private final String unrelevantLastName = "lastName";
    private final String unrelevantEmail = "email";

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BlogPostRepository postRepository;

    @MockBean
    private LikePostRepository likeRepository;

    @Autowired
    private BlogDataMapper dataMapper;

    @Autowired
    private BlogService blogService;

    @Captor
    private ArgumentCaptor<User> userParam;

    @Captor
    private ArgumentCaptor<LikePost> likePostCaptor;
    private User correctUser;
    private User notCorrectUser;
    private User notRelevantUser;
    private BlogPost blogPost;

    @Before
    public void setUp() {
        correctUser = new User();
        correctUser.setId(1L);
        correctUser.setAccountStatus(AccountStatus.CONFIRMED);
        correctUser.setFirstName(unrelevantName);
        correctUser.setLastName(unrelevantLastName);
        correctUser.setEmail(unrelevantEmail);

        notCorrectUser = new User();
        notCorrectUser.setId(2L);
        notCorrectUser.setAccountStatus(AccountStatus.NEW);
        notCorrectUser.setFirstName(unrelevantName);
        notCorrectUser.setLastName(unrelevantLastName);
        notCorrectUser.setEmail(unrelevantEmail);

        notRelevantUser = new User();
        notRelevantUser.setId(3L);
        notRelevantUser.setAccountStatus(AccountStatus.NEW);
        notRelevantUser.setFirstName(unrelevantName);
        notRelevantUser.setLastName(unrelevantLastName);
        notRelevantUser.setEmail(unrelevantEmail);

        blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setUser(notCorrectUser);

        when(userRepository.findById(1L)).thenReturn(Optional.of(correctUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(notCorrectUser));
        when(userRepository.findById(3L)).thenReturn(Optional.of(notRelevantUser));
        when(postRepository.findById(1L)).thenReturn(Optional.of(blogPost));
        when(likeRepository.findByUserAndPost(notRelevantUser, blogPost)).thenReturn(Optional.empty());
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void allowToLikeWhenCorrectUser() {
        blogService.addLikeToPost(blogPost.getId(), correctUser.getId());
        verify(likeRepository).save(likePostCaptor.capture());

        LikePost likePost = likePostCaptor.getValue();

        assertEquals(likePost.getPost(), blogPost);
        assertEquals(likePost.getUser(), correctUser);
    }

    @Test(expected = DomainError.class)
    public void denyToLikeWhenIncorrectUser_throwsDomainError() {
        blogService.addLikeToPost(blogPost.getId(), notCorrectUser.getId());
    }
}
