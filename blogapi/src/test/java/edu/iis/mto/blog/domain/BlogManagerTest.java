package edu.iis.mto.blog.domain;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.iis.mto.blog.domain.errors.DomainError;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
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

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private BlogDataMapper dataMapper;

    @Autowired
    private BlogService blogService;

    @Captor
    private ArgumentCaptor<User> userParam;

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void canNotAddLikeForUserWithStatusNotConfirmed() {
        User user = new User("John", "Steward", "john@domain.com",AccountStatus.NEW);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Assertions.assertThrows(DomainError.class,()->blogService.addLikeToPost(1L,1L));

    }


}
