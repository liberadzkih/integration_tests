package edu.iis.mto.blog.domain;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class) @SpringBootTest public class BlogManagerTest {

    @MockBean private UserRepository userRepository;

    @MockBean private BlogPostRepository blogPostRepository;

    @Autowired private BlogDataMapper dataMapper;

    @Autowired private BlogService blogService;

    @Captor private ArgumentCaptor<User> userParam;

    @MockBean private LikePostRepository likePostRepository;

    @Test public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test public void usersWithStatusNewShouldNotBeAbleToLikePosts() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        blogService.createUser(new UserRequest("Jake", "Steward", "jake@domain.com"));

        verify(userRepository, times(2)).save(userParam.capture());

        List<User> users = userParam.getAllValues();
        setUpUser(users.get(0), 1L, AccountStatus.CONFIRMED);
        setUpUser(users.get(1), 2L, AccountStatus.NEW);

        when(userRepository.findById(1L)).thenReturn(Optional.of(users.get(0)));
        when(userRepository.findById(2L)).thenReturn(Optional.of(users.get(1)));

        BlogPost blogPost = createBlogPost(users);

        when(blogPostRepository.findById(any())).thenReturn(Optional.of(blogPost));

        blogService.createPost(users.get(0).getId(), new PostRequest());

        when(likePostRepository.findByUserAndPost(any(), any())).thenReturn(Optional.empty());
        assertThrows(DomainError.class, () -> blogService.addLikeToPost(users.get(1).getId(), blogPost.getId()));
    }

    @Test public void usersWithStatusConfirmedShouldBeAbleToLikePosts() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        blogService.createUser(new UserRequest("Jake", "Steward", "jake@domain.com"));
        verify(userRepository, times(2)).save(userParam.capture());

        List<User> users = userParam.getAllValues();
        setUpUser(users.get(0), 1L, AccountStatus.CONFIRMED);
        setUpUser(users.get(1), 2L, AccountStatus.CONFIRMED);

        when(userRepository.findById(1L)).thenReturn(Optional.of(users.get(0)));
        when(userRepository.findById(2L)).thenReturn(Optional.of(users.get(1)));

        BlogPost blogPost = createBlogPost(users);

        when(blogPostRepository.findById(any())).thenReturn(Optional.of(blogPost));

        blogService.createPost(users.get(0).getId(), new PostRequest());

        when(likePostRepository.findByUserAndPost(any(), any())).thenReturn(Optional.empty());

        boolean result = blogService.addLikeToPost(users.get(1).getId(), blogPost.getId());
        assertTrue(result);
    }

    private void setUpUser(User user, long id, AccountStatus accountStatus) {
        user.setId(id);
        user.setAccountStatus(accountStatus);
    }

    private BlogPost createBlogPost(List<User> users) {
        BlogPost blogPost = new BlogPost();
        blogPost.setUser(users.get(0));
        blogPost.setId(1L);
        blogPost.setEntry("Entry");
        return blogPost;
    }
}
