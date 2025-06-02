package org.anand.mynoteapp.repository;

import org.anand.mynoteapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByIsActiveTrueAndIsDeletedFalse();
    Optional<Category> findByIdAndIsDeletedFalse(Integer id);
    List<Category> findByIsDeletedFalse();
    Boolean existsByName(String name);
}
