package org.anand.mynoteapp.repository;

import org.anand.mynoteapp.entity.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotesRepository extends JpaRepository<Notes, Integer> {
}
