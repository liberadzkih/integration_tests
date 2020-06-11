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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BlogManager extends DomainService implements BlogService {

    protected BlogManager(UserRepository userRepository, BlogPostRepository blogPostRepository, LikePostRepository likePostRepository,
            BlogDataMapper mapper) {
        super(userRepository, blogPostRepository, likePostRepository, mapper);
    }

    @Override
    public Long createUser(UserRequest userRequest) {
        Optional<User> userList = userRepository.findByEmail(userRequest.getEmail());
        if (userList.isPresent()) {
            throw new DataIntegrityViolationException("Email in use");
        }

        User user = mapper.mapToEntity(userRequest);
        user.setAccountStatus(AccountStatus.NEW);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public Long createPost(Long userId, PostRequest postRequest) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(domainError(DomainError.USER_NOT_FOUND));
        if (!user.getAccountStatus()
                 .equals(AccountStatus.CONFIRMED)) {
            throw new DomainError("Cannot create post - NOT CONFIRMED ACCOUNT");
        }
        BlogPost post = mapper.mapToEntity(postRequest);
        post.setUser(user);
        blogPostRepository.save(post);
        return post.getId();
    }

    @Override
    public boolean addLikeToPost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(domainError(DomainError.USER_NOT_FOUND));
        BlogPost post = blogPostRepository.findById(postId)
                                          .orElseThrow(domainError(DomainError.POST_NOT_FOUND));

        if (!user.getAccountStatus()
                .equals(AccountStatus.CONFIRMED)) {
            throw new DomainError("Cannot like post - NOT CONFIRMED ACCOUNT");
        }

        if (post.getUser()
                .getId()
                .equals(userId)) {
            throw new DomainError(DomainError.SELF_LIKE);
        }
        Optional<LikePost> existingLikeForPost = likePostRepository.findByUserAndPost(user, post);
        if (existingLikeForPost.isPresent()) {
            return false;
        }
        LikePost likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(post);
        likePostRepository.save(likePost);
        return true;
    }

}
