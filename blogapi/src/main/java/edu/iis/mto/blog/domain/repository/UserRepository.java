package edu.iis.mto.blog.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.iis.mto.blog.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(String firstName,
            String lastName, String email);

    Optional<User> findByEmail(String email);
}
