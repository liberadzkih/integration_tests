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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private ArgumentCaptor<LikePost> likePostCaptor;
    private User confirmedUser;
    private User notConfirmedUser;
    private User nonRelevantUser;
    private BlogPost blogPost;

    private final String placeholderName = "name";
    private final String placeholderLastName = "lastName";
    private final String placeholderEmail = "email";

    @Before
    public void setUp() {
        confirmedUser = createUser(placeholderName, placeholderLastName, placeholderEmail, 1L, AccountStatus.CONFIRMED);

        notConfirmedUser = createUser(placeholderName, placeholderLastName, placeholderEmail, 2L, AccountStatus.NEW);

        nonRelevantUser = createUser(placeholderName, placeholderLastName, placeholderEmail, 3L, AccountStatus.NEW);

        blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setUser(notConfirmedUser);

        when(userRepository.findById(1L)).thenReturn(Optional.of(confirmedUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(notConfirmedUser));
        when(userRepository.findById(3L)).thenReturn(Optional.of(nonRelevantUser));
        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(blogPost));
        when(likePostRepository.findByUserAndPost(nonRelevantUser, blogPost)).thenReturn(Optional.empty());
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void confirmedUserTryToLikePost_shouldAllow() {
        blogService.addLikeToPost(blogPost.getId(), confirmedUser.getId());
        verify(likePostRepository).save(likePostCaptor.capture());

        LikePost likePost = likePostCaptor.getValue();

        assertEquals(likePost.getPost(), blogPost);
        assertEquals(likePost.getUser(), confirmedUser);
    }

    @Test
    public void shouldThrowDomainErrorWhenNotConfirmedUserWantToLikeBlogPost() {
        assertThrows(DomainError.class,()->blogService.addLikeToPost(notConfirmedUser.getId(), blogPost.getId()));
    }

    private User createUser(String firstName, String lastName, String email, Long id, AccountStatus accountStatus) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAccountStatus(accountStatus);
        user.setId(id);
        return user;
    }

}
