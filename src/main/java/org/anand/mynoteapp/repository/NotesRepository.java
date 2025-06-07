package org.anand.mynoteapp.repository;

import org.anand.mynoteapp.entity.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotesRepository extends JpaRepository<Notes, Integer> {

    Page<Notes> findByCreatedBy(Integer userId, Pageable pageable);
    List<Notes> findByCreatedByAndIsDeletedTrue(Integer userId);
    Page<Notes> findByCreatedByAndIsDeletedFalse(Integer userId, Pageable pageable);
    List<Notes> findAllByIsDeletedAndDeletedOnBefore(boolean b, LocalDateTime cutOffDate);
}
