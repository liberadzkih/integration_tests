package edu.iis.mto.blog.domain;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
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

    private User user,user2;
    private BlogPost blogPost;

    private String firstName = "Jan", lastName="Komasa", email = "john@domain.com";
    private String  firstName2 = "Piotr", lastName2 = "Adamczyk", email2 = "ddddddddd@wp.com";
    private Long userId = 10L, user2Id = 20L, postId = 30L;

    @Before
    public void setUp() {
        user = createUser(firstName,lastName,email,userId,AccountStatus.NEW);
        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("Entry");
        blogPost.setId(postId);
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test public void userWithConfirmedStatusTryLikePost_shouldNotThrowDomainError() {
        user2 = createUser(firstName2,lastName2,email2,user2Id,AccountStatus.CONFIRMED);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(user2Id)).thenReturn(Optional.of(user2));
        when(blogPostRepository.findById(postId)).thenReturn(Optional.of(blogPost));
        blogService.addLikeToPost(user2Id,blogPost.getId());
        assertSame(user2.getAccountStatus(), AccountStatus.CONFIRMED);
        assertDoesNotThrow(()->blogService.addLikeToPost(user2.getId(),blogPost.getId()));
    }

    @Test public void userWithNotConfirmedStatusTryLikePost_shouldThrowDomainError() {
        user2 = createUser(firstName2,lastName2,email2,user2Id,AccountStatus.NEW);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(user2Id)).thenReturn(Optional.of(user2));
        when(blogPostRepository.findById(postId)).thenReturn(Optional.of(blogPost));

        assertNotSame(user2.getAccountStatus(), AccountStatus.CONFIRMED);
        assertThrows(DomainError.class,()->blogService.addLikeToPost(user2.getId(),blogPost.getId()));
    }

    private User createUser(String firstName, String lastName,String email,Long id,AccountStatus accountStatus){
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAccountStatus(accountStatus);
        user.setId(id);
        return user;
    }
}
