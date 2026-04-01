package org.example.dao;

import org.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByUseremail(String email);

    Optional<User> findByUseremail(String email);
}
