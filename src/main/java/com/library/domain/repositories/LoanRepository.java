package com.library.domain.repositories;

import com.library.domain.entities.Loan;
import com.library.domain.entities.User;
import com.library.domain.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    long countByUserAndStatus(User user, LoanStatus status);

    List<Loan> findByUserIdAndBookId(Long userId, Long bookId);

    List<Loan> findByStatusAndReturnDateBefore(LoanStatus status, java.time.LocalDate returnDate);
}