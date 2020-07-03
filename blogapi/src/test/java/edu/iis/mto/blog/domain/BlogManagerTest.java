package edu.iis.mto.blog.domain;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
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

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BlogPostRepository blogPostRepository;

    @MockBean
    private LikePostRepository likePostRepository;

    @Autowired
    private BlogDataMapper dataMapper;

    @Autowired
    private BlogService blogService;

    @Captor
    private ArgumentCaptor<User> userParam;

    @Captor
    private ArgumentCaptor<BlogPost> blogParam;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void addLikeToPostShouldReturnAccountNotConfirmedDomainError() {
        Long userId = blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        blogService.createUser(new UserRequest("Jan", "Nowak", "jan@domain.com"));
        verify(userRepository, times(2)).save(userParam.capture());
        List<User> userList = userParam.getAllValues();
        User user0 = userList.get(0);
        user0.setAccountStatus(AccountStatus.CONFIRMED);
        User user1 = userList.get(1);
        user1.setId(2L);
        Mockito.when(userRepository.findById(user0.getId())).thenReturn(java.util.Optional.ofNullable(user0));
        Long postId = blogService.createPost(userId, new PostRequest());
        verify(blogPostRepository).save(blogParam.capture());
        BlogPost blogPost = blogParam.getValue();
        Mockito.when(blogPostRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(blogPost));

        expectedException.expect(DomainError.class);
        expectedException.expectMessage("user account has not yet been confirmed");
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(java.util.Optional.ofNullable(user1));

        blogService.addLikeToPost(user1.getId(), postId);
    }

    @Test
    public void addLikeToPostShouldAllowGivingLikeAndReturnTrue() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        blogService.createUser(new UserRequest("Jan", "Nowak", "jan@domain.com"));
        verify(userRepository, times(2)).save(userParam.capture());
        List<User> userList = userParam.getAllValues();
        User user = userList.get(0);
        user.setId(1L);
        user.setAccountStatus(AccountStatus.CONFIRMED);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        PostRequest postRequest = new PostRequest();
        postRequest.setEntry("test");
        blogService.createPost(user.getId(),postRequest);
        verify(blogPostRepository).save(blogParam.capture());
        BlogPost blogPost =blogParam.getValue();
        blogPost.setId(3L);
        Mockito.when(blogPostRepository.findById(blogPost.getId())).thenReturn(Optional.of(blogPost));
        User user1 = userList.get(1);
        user1.setId(2L);
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        Mockito.when(likePostRepository.findByUserAndPost(user,blogPost)).thenReturn(Optional.of(new LikePost()));

        user1.setAccountStatus(AccountStatus.CONFIRMED);
        assertTrue(blogService.addLikeToPost(user1.getId(),blogPost.getId()));
    }


}
