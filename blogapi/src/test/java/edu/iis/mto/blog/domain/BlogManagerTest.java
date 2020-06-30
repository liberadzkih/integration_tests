package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.api.request.PostRequest;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
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

	@Captor
	private ArgumentCaptor<BlogPost> blogPostParam;

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Test
	public void creatingNewUserShouldSetAccountStatusToNEW() {
		blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
		verify(userRepository).save(userParam.capture());
		User user = userParam.getValue();
		assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
	}

	@Test
	public void shouldAllowToLikePostWithConfirmedUserStatus() {
		blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
		blogService.createUser(new UserRequest("Jacek", "Wrobel", "arr@domain.com"));

		verify(userRepository, times(2)).save(userParam.capture());
		List<User> temp = userParam.getAllValues();
		User user = temp.get(0);
		user.setId(1L);
		user.setAccountStatus(AccountStatus.CONFIRMED);
		Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		PostRequest postRequest = new PostRequest();
		postRequest.setEntry("test entry");
		blogService.createPost(user.getId(), postRequest);
		verify(blogPostRepository).save(blogPostParam.capture());
		BlogPost blogPost = blogPostParam.getValue();
		blogPost.setId(3L);
		Mockito.when(blogPostRepository.findById(blogPost.getId())).thenReturn(Optional.of(blogPost));

		User user2 = temp.get(1);
		user2.setId(2L);
		Mockito.when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

		Mockito.when(likePostRepository.findByUserAndPost(user, blogPost)).thenReturn(Optional.of(new LikePost()));

		user2.setAccountStatus(AccountStatus.CONFIRMED);
		assertTrue(blogService.addLikeToPost(user2.getId(), blogPost.getId()));
	}

	@Test
	public void shouldNotAllowToLikePostWithoutConfirmedUserStatus() {
		blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
		blogService.createUser(new UserRequest("Jacek", "Wrobel", "arr@domain.com"));

		verify(userRepository, times(2)).save(userParam.capture());
		List<User> temp = userParam.getAllValues();
		User user = temp.get(0);
		user.setId(1L);
		user.setAccountStatus(AccountStatus.CONFIRMED);
		Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		PostRequest postRequest = new PostRequest();
		postRequest.setEntry("test entry");
		blogService.createPost(user.getId(), postRequest);
		verify(blogPostRepository).save(blogPostParam.capture());
		BlogPost blogPost = blogPostParam.getValue();
		blogPost.setId(3L);
		Mockito.when(blogPostRepository.findById(blogPost.getId())).thenReturn(Optional.of(blogPost));

		User user2 = temp.get(1);
		user2.setId(2L);
		Mockito.when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

		exceptionRule.expect(DomainError.class);
		exceptionRule.expectMessage("user account is not confirmed");

		assertTrue(blogService.addLikeToPost(user2.getId(), blogPost.getId()));

	}
}
