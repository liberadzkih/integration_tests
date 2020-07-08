package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.iis.mto.blog.domain.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(String firstName,
            String lastName, String email);

    Optional<User> findByEmail(String email);
}
