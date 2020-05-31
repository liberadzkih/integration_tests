package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.domain.repository.UserBuilder;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private BlogDataMapper dataMapper;

    @MockBean
    private LikePostRepository likePostRepository;

    @Autowired
    private BlogService blogService;

    @MockBean
    private BlogPostRepository blogPostRepository;

    @Captor
    private ArgumentCaptor<User> userParam;
    private long usersIds = 1L;
    private long blogPostsIds = 1L;
    private long likePostIds = 1L;

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void shouldAllowLikingPost() {
        User user = createPersistedUser();
        BlogPost blogPost = createBlogPostWithUser(user);
        User userToLike = createPersistedUser();
        userToLike.setAccountStatus(AccountStatus.CONFIRMED);
        when(likePostRepository.findByUserAndPost(any(), any())).thenReturn(Optional.empty());
        when(blogPostRepository.findById(anyLong())).thenReturn(Optional.of(blogPost));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userToLike));
        boolean res = blogService.addLikeToPost(userToLike.getId(), blogPost.getId());
        assertTrue(res);
    }

    @Test
    public void shouldThrrowAnDomainErrorWhenUserNotConfirmed() {
        User user = createPersistedUser();
        BlogPost blogPost = createBlogPostWithUser(user);
        User userToLike = createPersistedUser();
        userToLike.setAccountStatus(AccountStatus.NEW);
        when(likePostRepository.findByUserAndPost(any(), any())).thenReturn(Optional.empty());
        when(blogPostRepository.findById(anyLong())).thenReturn(Optional.of(blogPost));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userToLike));
        assertThrows(DomainError.class,
                () -> blogService.addLikeToPost(userToLike.getId(), blogPost.getId()));
    }


    public User createPersistedUser() {
        User user = UserBuilder.builder()
                .lastName("Witaszek")
                .firstName("Dawid")
                .email("215920@edu.p.lodz.pl")
                .accountStatus(AccountStatus.CONFIRMED)
                .build();
        user.setId(usersIds++);
        return user;
    }

    public BlogPost createBlogPostWithUser(User user) {
        BlogPost blogPost = new BlogPost();
        blogPost.setEntry("DEADBEEF");
        blogPost.setUser(user);
        blogPost.setId(blogPostsIds++);
        return blogPost;
    }

    public LikePost createLikePost(User user, BlogPost blogPost){
        LikePost likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
        likePost.setId(likePostIds++);
        return likePost;
    }


}
