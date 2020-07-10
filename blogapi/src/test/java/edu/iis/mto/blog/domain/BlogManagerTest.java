package edu.iis.mto.blog.domain;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
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
    private ArgumentCaptor<BlogPost> blogPostParam;

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void addingLikeByUserWithoutStatusConfirmedThrowsDomainErrorWithProperMessage() {
        User user = new User();
        user.setId(21L);
        user.setAccountStatus(AccountStatus.NEW);

        User otherUser = new User();
        otherUser.setId(22L);
        otherUser.setAccountStatus(AccountStatus.CONFIRMED);

        BlogPost blogPost = new BlogPost();
        blogPost.setUser(otherUser);
        blogPost.setId(37L);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(blogPostRepository.findById(blogPost.getId())).thenReturn(Optional.of(blogPost));
        ;

        Exception exception = assertThrows(DomainError.class,
                () -> blogService.addLikeToPost(user.getId(), blogPost.getId()));

        assertThat(exception.getMessage(), Matchers.equalTo(DomainError.USER_NOT_CONFIRMED));
    }

}
