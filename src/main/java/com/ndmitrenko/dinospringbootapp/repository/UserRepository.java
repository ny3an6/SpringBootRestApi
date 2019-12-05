package com.ndmitrenko.dinospringbootapp.repository;

import com.ndmitrenko.dinospringbootapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserId(Long userId);
    Optional<User> findUserByLastName(String lastName);
    Optional<List<User>> findUserByLastNameContaining(String partOfLastName);
}
