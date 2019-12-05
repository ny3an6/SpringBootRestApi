package com.ndmitrenko.dinospringbootapp.repository;

import com.ndmitrenko.dinospringbootapp.model.User;
import com.ndmitrenko.dinospringbootapp.model.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Long> {
    Optional<UserContact> findByContactId(Long contactId);
    Optional<UserContact> findByContactLastName(String lastName);
    Optional<UserContact> findByContactNumber(Long number);
    List<UserContact> findAllByUser(User user);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_contacts u WHERE u.id = :id", nativeQuery = true)
    void deleteContact(@Param("id") Long id);
}
