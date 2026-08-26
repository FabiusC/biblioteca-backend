package com.library.domain.repositories;

import com.library.domain.entities.Copy;
import com.library.domain.enums.CopyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CopyRepository extends JpaRepository<Copy, Long> {

    List<Copy> findByBookIsbnAndStatus(String isbn, CopyStatus status);
}