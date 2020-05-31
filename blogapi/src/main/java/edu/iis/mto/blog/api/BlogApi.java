package edu.iis.mto.blog.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.dto.Id;
import edu.iis.mto.blog.dto.PostData;
import edu.iis.mto.blog.dto.UserData;
import edu.iis.mto.blog.services.BlogService;
import edu.iis.mto.blog.services.DataFinder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/blog", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "blog", description = "blog API")
public class BlogApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogApi.class);

    @Autowired
    private BlogService blogService;

    @Autowired
    private DataFinder finder;

    @Operation(summary = "Creates new user")
    @PostMapping(path = "/user")
    @ResponseStatus(HttpStatus.CREATED)
    public Id createUser(@RequestBody UserRequest userRequest) {
        LOGGER.debug("create user endpoint called for data '{}'", userRequest);
        Long userId = blogService.createUser(userRequest);
        return id(userId);
    }

    @Operation(summary = "get user info based on user id")
    @GetMapping(path = "/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserData getUser(@PathVariable("id") Long userId) {
        LOGGER.debug("get user endpoint called for user id '{}'", userId);
        return finder.getUserData(userId);
    }

    @Operation(summary = "find users based on email or first name or last name")
    @GetMapping(path = "/user/find")
    public List<UserData> findUser(@RequestParam String searchString) {
        LOGGER.debug("find users endpoint called for searchString '{}'", searchString);
        return finder.findUsers(searchString);
    }

    @Operation(summary = "Creates new blog post")
    @PostMapping(path = "/user/{userid}/post")
    @ResponseStatus(HttpStatus.CREATED)
    public Id createPost(@PathVariable("userid") Long userId, @RequestBody PostRequest postRequest) {
        LOGGER.debug("creates post endpoint called for data '{}'", postRequest);

        Long postId = blogService.createPost(userId, postRequest);
        return id(postId);
    }

    @Operation(summary = "Add like to blog post")
    @PostMapping(path = "/user/{userId}/like/{postId}")
    public boolean addLikeToPost(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId) {
        LOGGER.debug("add like to post endpoint called for userId '{}' and postId '{}'", userId, postId);
        return blogService.addLikeToPost(userId, postId);
    }

    @Operation(summary = "get user posts based on user id")
    @GetMapping(path = "/user/{id}/post")
    public List<PostData> getUserPosts(@PathVariable("id") Long userId) {
        LOGGER.debug("get user posts endpoint called for user id '{}'", userId);
        return finder.getUserPosts(userId);
    }

    @Operation(summary = "get single post based on post id")
    @GetMapping(path = "/post/{id}")
    public PostData getPosts(@PathVariable("id") Long postId) {
        LOGGER.debug("get post by id '{}'", postId);
        return finder.getPost(postId);
    }

    private Id id(Long id) {
        return new Id(id);
    }

}
