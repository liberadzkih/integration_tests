package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;
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

import java.util.Optional;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    private final String unrelevantName = "name";
    private final String unrelevantLastName = "lastname";
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
    private User confirmedUser;
    private User notConfirmedUser;
    private User notRelevantUser;
    private BlogPost blogPost;

    @Before
    public void setUp() {
        confirmedUser = new User();
        confirmedUser.setId(1L);
        confirmedUser.setAccountStatus(AccountStatus.CONFIRMED);
        confirmedUser.setFirstName(unrelevantName);
        confirmedUser.setLastName(unrelevantLastName);
        confirmedUser.setEmail(unrelevantEmail);

        notConfirmedUser = new User();
        notConfirmedUser.setId(2L);
        notConfirmedUser.setAccountStatus(AccountStatus.NEW);
        notConfirmedUser.setFirstName(unrelevantName);
        notConfirmedUser.setLastName(unrelevantLastName);
        notConfirmedUser.setEmail(unrelevantEmail);

        notRelevantUser = new User();
        notRelevantUser.setId(3L);
        notRelevantUser.setAccountStatus(AccountStatus.NEW);
        notRelevantUser.setFirstName(unrelevantName);
        notRelevantUser.setLastName(unrelevantLastName);
        notRelevantUser.setEmail(unrelevantEmail);

        blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setUser(notConfirmedUser);

        when(userRepository.findById(1L)).thenReturn(Optional.of(confirmedUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(notConfirmedUser));
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
    public void shouldAllowToLikePostWhenUserWithConfirmedAccout() {
        blogService.addLikeToPost(blogPost.getId(), confirmedUser.getId());
        verify(likeRepository).save(likePostCaptor.capture());

        LikePost likePost = likePostCaptor.getValue();

        assertTrue(likePost.getPost()
                           .equals(blogPost));
        assertTrue(likePost.getUser()
                           .equals(confirmedUser));
    }

    @Test(expected = DomainError.class)
    public void shouldThrowDomainErrorWhenNotConfirmedUserWantToLikeBlogPost() {
        blogService.addLikeToPost(blogPost.getId(), notConfirmedUser.getId());
    }
}
