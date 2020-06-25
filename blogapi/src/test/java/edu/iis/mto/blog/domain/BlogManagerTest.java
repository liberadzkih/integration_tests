package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    private static final long USER_ID = 1L;
    private static final long USER_ID_TWO = 2L;
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
    public void userWithAccountStatusOtherThanConfirmedShouldNotBeAbleLikeAPost() {
        User postCreatorUser = createUserWithIdAndStatus(USER_ID_TWO, AccountStatus.CONFIRMED);
        User newAccountUser = createUserWithIdAndStatus(USER_ID, AccountStatus.NEW);
        BlogPost blogPost = createPostWithUserId(postCreatorUser.getId());

        assertThat(newAccountUser.getAccountStatus(), Matchers.not(AccountStatus.CONFIRMED));
        assertThrows(DomainError.class, () -> {
            blogService.addLikeToPost(newAccountUser.getId(), blogPost.getId());
        });
    }

    @Test
    public void userWithAccountStatusConfirmedShouldBeAbleToLikePost() {
        User postCreatorUser = createUserWithIdAndStatus(USER_ID_TWO, AccountStatus.CONFIRMED);
        User confirmedAccountUser = createUserWithIdAndStatus(USER_ID, AccountStatus.CONFIRMED);
        BlogPost blogPost = createPostWithUserId(postCreatorUser.getId());

        when(likePostRepository.findByUserAndPost(confirmedAccountUser, blogPost)).thenReturn(Optional.empty());
        assertEquals(AccountStatus.CONFIRMED, confirmedAccountUser.getAccountStatus());
        assertDoesNotThrow(() -> blogService.addLikeToPost(confirmedAccountUser.getId(), blogPost.getId()));
    }

    private User createUserWithIdAndStatus(Long id, AccountStatus accountStatus) {
        User user = new User();
        user.setAccountStatus(accountStatus);
        user.setId(id);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        return user;
    }

    private BlogPost createPostWithUserId(Long userId) {
        blogService.createPost(userId, new PostRequest());
        verify(blogPostRepository).save(blogPostParam.capture());
        BlogPost blogPost = blogPostParam.getValue();
        when(blogPostRepository.findById(blogPost.getId())).thenReturn(Optional.of(blogPost));
        return blogPost;
    }

}
