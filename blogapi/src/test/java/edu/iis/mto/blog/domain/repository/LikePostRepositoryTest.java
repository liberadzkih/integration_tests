package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {


	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private LikePostRepository likePostRepository;


	private User user;
	private BlogPost blogPost;
	private LikePost likePost;

	@Before
	public void setUp() {
		user = buildUser("Jan", null, "john@domain.com");

		blogPost = buildBlogPost(user, "test entry");

		likePost = new LikePost();
		likePost.setPost(blogPost);
		likePost.setUser(user);

	}

	@Test
	public void shouldFindNoLikePostsIfRepositoryIsEmpty() {

		List<LikePost> posts = likePostRepository.findAll();

		assertThat(posts, hasSize(0));
	}

	@Test
	public void shouldFindOneLikePostIfRepositoryContainsOneLikePostEntity() {
		LikePost persistedLikePost = entityManager.persist(likePost);
		List<LikePost> posts = likePostRepository.findAll();

		assertThat(posts, hasSize(1));
		assertThat(posts.get(0)
						.getUser(),
				equalTo(persistedLikePost.getUser()));
	}

	@Test
	public void shouldStoreANewLikePost() {
		LikePost persistedLikePost = likePostRepository.save(likePost);
		assertThat(persistedLikePost.getId(), notNullValue());
	}

	@Test
	public void shouldFindNoLikePostIfUserIsWrong() {
		User user2 = buildUser("Andrzej", "Dokimuk", "andrzej@domain.pl");

		Optional<LikePost> result = likePostRepository.findByUserAndPost(user2, blogPost);
		assertFalse(result.isPresent());

	}

	@Test
	public void shouldFindNoLikePostIfBlogPostIsWrong() {
		User user2 = buildUser("Andrzej", "Dokimuk", "andrzej@domain.pl");
		BlogPost blogPost2 = buildBlogPost(user2, "test entry");

		Optional<LikePost> result = likePostRepository.findByUserAndPost(user, blogPost2);
		assertFalse(result.isPresent());

	}

	@Test
	public void shouldFindLikePost() {
		LikePost persistedLikePost = likePostRepository.save(likePost);
		Optional<LikePost> result = likePostRepository.findByUserAndPost(user, blogPost);
		assertTrue(result.isPresent());
		assertEquals(persistedLikePost, result.get());
	}

	private User buildUser(String firstName, String lastName, String email) {
		User user = new User();
		user.setAccountStatus(AccountStatus.NEW);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		entityManager.persist(user);
		return user;
	}

	private BlogPost buildBlogPost(User user, String entry) {
		BlogPost blogPost = new BlogPost();
		blogPost.setUser(user);
		blogPost.setEntry(entry);
		entityManager.persist(blogPost);
		return blogPost;
	}
}