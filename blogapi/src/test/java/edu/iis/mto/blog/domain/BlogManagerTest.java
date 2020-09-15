package edu.iis.mto.blog.domain;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
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
    private LikePostRepository likePostRepository;

    @MockBean
    private BlogPostRepository blogPostRepository;

    @Autowired
    private BlogDataMapper dataMapper;

    @Autowired
    private BlogService blogService;

    @Captor
    private ArgumentCaptor<User> userParam;

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void likePostWithNotConfirmedUserShouldThrowDomainErrorException() {
        User user = createUserForTest("Lee", "Xong", 1L, "abc@123.com", AccountStatus.CONFIRMED);
        BlogPost blogPost = createBlogPostForTest(user);
        User user2 = createUserForTest("Lel", "Xong", 2L, "cbd@123.com", AccountStatus.NEW);
        assertThrows(DomainError.class, () -> blogService.addLikeToPost(user2.getId(), blogPost.getId()));
    }

    @Test
    public void likePostWithConfirmedUserShouldAllowToLike() {
        User user = createUserForTest("Lee", "Xong", 1L, "abc@123.com", AccountStatus.CONFIRMED);
        BlogPost blogPost = createBlogPostForTest(user);
        User user2 = createUserForTest("Lel", "Xong", 2L, "cbd@123.com", AccountStatus.CONFIRMED);
        when(likePostRepository.findByUserAndPost(any(), any())).thenReturn(Optional.empty());
        when(blogPostRepository.findById(anyLong())).thenReturn(Optional.of(blogPost));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));
        assertTrue(blogService.addLikeToPost(user2.getId(), blogPost.getId()));
    }

    @Test
    public void likingOwnPostShouldThrowDomainErrorException() {
        User user = createUserForTest("Lee", "Xong", 1L, "abc@123.com", AccountStatus.CONFIRMED);
        BlogPost blogPost = createBlogPostForTest(user);
        assertThrows(DomainError.class, () -> blogService.addLikeToPost(user.getId(), blogPost.getId()));
    }

    private User createUserForTest(String name, String surname, long id, String email, AccountStatus accStatus) {
        User user = new User();
        user.setEmail(email);
        user.setId(id);
        user.setLastName(surname);
        user.setFirstName(name);
        user.setAccountStatus(accStatus);
        return user;
    }

    private BlogPost createBlogPostForTest(User user) {
        BlogPost blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("lorem ipsum");
        blogPost.setId(1L);
        return blogPost;
    }

}
