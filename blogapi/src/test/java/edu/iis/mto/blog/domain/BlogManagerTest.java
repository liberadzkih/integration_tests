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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

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

    private User confirmedUser1;
    private User confirmedUser2;
    private User newUser;
    private BlogPost blogPost;

    @Before
    public void setup() {
        confirmedUser1 = new User();
        confirmedUser1.setId(11L);
        confirmedUser1.setAccountStatus(AccountStatus.CONFIRMED);

        confirmedUser2 = new User();
        confirmedUser2.setId(22L);
        confirmedUser2.setAccountStatus(AccountStatus.CONFIRMED);

        newUser = new User();
        newUser.setId(33L);
        newUser.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        blogPost.setUser(confirmedUser1);
        blogPost.setId(3L);

        Mockito.when(userRepository.findById(confirmedUser2.getId())).thenReturn(Optional.of(confirmedUser2));
        Mockito.when(blogPostRepository.findById(blogPost.getId())).thenReturn(Optional.of(blogPost));
        Mockito.when(likePostRepository.findByUserAndPost(confirmedUser2, blogPost)).thenReturn(Optional.of(new LikePost()));
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void onlyUserWithCONFIRMEDStatusCanLikePost() {
        blogService.addLikeToPost(confirmedUser2.getId(), blogPost.getId());
    }

    @Test(expected = DomainError.class)
    public void userWithNEWStatusCannotLikePost() {
        blogService.addLikeToPost(newUser.getId(), blogPost.getId());
    }
}
