package edu.iis.mto.blog.api.configuration;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ConfigureE2ETests {

    @Value("${e2e}")
    private boolean e2eOn;
    private UserRepository userRepository;

    public ConfigureE2ETests(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void addConfirmedUserOnE2ETest() {
        if (e2eOn) {
            addUser(1, AccountStatus.CONFIRMED);
            addUser(2, AccountStatus.CONFIRMED);
            addUser(3, AccountStatus.REMOVED);
        }
    }

    public void addUser(int nr, AccountStatus status) {
        User user = new User();
        user.setFirstName("testUser" + nr);
        user.setLastName("testUser" + nr);
        user.setEmail("testUser@domain.com" + nr);
        user.setAccountStatus(status);

        userRepository.save(user);
    }


}
