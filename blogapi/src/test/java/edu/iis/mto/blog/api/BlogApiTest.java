package edu.iis.mto.blog.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.dto.Id;
import edu.iis.mto.blog.services.BlogService;
import edu.iis.mto.blog.services.DataFinder;

import javax.persistence.EntityNotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogApi.class)
public class BlogApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BlogService blogService;

    @MockBean
    private DataFinder finder;

    @Test
    public void postBlogUserShouldResponseWithStatusCreatedAndNewUserId() throws Exception {
        UserRequest user = new UserRequest();
        user.setEmail("john@domain.com");
        user.setFirstName("John");
        user.setLastName("Steward");
        String content = writeJson(user);

        Long newUserId = 21L;
        when(blogService.createUser(user)).thenReturn(newUserId);

        mvc.perform(post("/blog/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                                      .content(content)).andExpect(status().isCreated())
           .andExpect(content().string(writeJson(new Id(newUserId))));
    }

    @Test
    public void throwingDataIntegrityViolationExceptionShouldResponseWithStatusConflict() throws Exception {
        UserRequest user = new UserRequest();
        user.setEmail("john@domain.com");
        user.setFirstName("John");
        user.setLastName("Steward");
        String content = writeJson(user);

        when(blogService.createUser(user)).thenThrow(DataIntegrityViolationException.class);

        mvc.perform(post("/blog/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                                      .content(content)).andExpect(status().isConflict());
    }

    @Test
    public void callingNotExistingUserShouldResponseWithStatusNotFound() throws Exception {
        Long notExistingUserId = 37L;
        when(finder.getUserData(notExistingUserId)).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/blog/user/{id}", notExistingUserId)).andExpect(status().isNotFound());
    }

    private String writeJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writer().writeValueAsString(obj);
    }

}
