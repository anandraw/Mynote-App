package org.anand.mynoteapp.repository;

import org.anand.mynoteapp.entity.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileDetails, Long> {
}
