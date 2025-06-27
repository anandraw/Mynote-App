package org.anand.mynoteapp.repository;

import org.anand.mynoteapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
