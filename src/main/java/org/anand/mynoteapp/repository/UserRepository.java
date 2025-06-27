package org.anand.mynoteapp.repository;

import org.anand.mynoteapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByEmail(String email);
    User findByEmail(String email);
}
